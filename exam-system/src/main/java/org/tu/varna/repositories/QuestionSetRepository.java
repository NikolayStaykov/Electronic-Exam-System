package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.QuestionSet;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class QuestionSetRepository implements Repository<QuestionSet> {

    private static final String CREATE_QUERY = "insert into question_set (name,discipline_id,parent_question_set_id,info) values (?, ?, ?, ?) returning id;";

    private static final String UPDATE_QUERY = "update question_set SET name = ?, discipline_id = ?, parent_question_set_id = ?, info = ? where id = ?;";

    private static final String DELETE_QUERY = "delete from question_set where id = ?;";

    private static final String FIND_QUERY = "select * from question_set where 1 = 1 ";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(QuestionSet object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getName());
            statement.setLong(2, object.getDisciplineId());
            if(object.getParentQuestionSetId() != null) {
                statement.setLong(3, object.getParentQuestionSetId());
            } else {
                statement.setNull(3, Types.BIGINT);
            }
            statement.setString(4, object.getInfo());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            object.setId(resultSet.getLong(1));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(QuestionSet object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, object.getName());
            statement.setLong(2, object.getDisciplineId());
            if(object.getParentQuestionSetId() != null){
                statement.setLong(3, object.getParentQuestionSetId());
            } else {
                statement.setNull(3, Types.BIGINT);
            }
            statement.setString(4, object.getInfo());
            statement.setLong(5, object.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(QuestionSet object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setLong(1, object.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<QuestionSet> find(QuestionSet template) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));

            formatParameters(statement, template);

            List<QuestionSet> questionSets = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                questionSets.add(new QuestionSet(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("info"),
                        resultSet.getLong("discipline_id"),
                        resultSet.getLong("parent_question_set_id")));
            }

            return questionSets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatQuery(QuestionSet searchTemplate){
        String query = FIND_QUERY;
        if(searchTemplate.getId() != null){
            query += " AND id = ?";
        }
        if(searchTemplate.getParentQuestionSetId() != null){
            query += " AND parent_question_set_id = ?";
        }
        if(searchTemplate.getInfo() != null){
            query += " AND info like ?";
        }
        if(searchTemplate.getDisciplineId() != null){
            query += " AND discipline_id = ?";
        }
        return query;
    }

    private void formatParameters(PreparedStatement statement, QuestionSet searchTemplate) throws SQLException{
        int parameterIndex = 1;
        if(searchTemplate.getId() != null){
            statement.setLong(parameterIndex, searchTemplate.getId());
            parameterIndex++;
        }
        if(searchTemplate.getParentQuestionSetId() != null){
            statement.setLong(parameterIndex, searchTemplate.getParentQuestionSetId());
            parameterIndex++;
        }
        if(searchTemplate.getInfo() != null){
            statement.setString(parameterIndex, "%" + searchTemplate.getInfo() + "%");
            parameterIndex++;
        }
        if(searchTemplate.getDisciplineId() != null){
            statement.setLong(parameterIndex, searchTemplate.getDisciplineId());
        }
    }
}
