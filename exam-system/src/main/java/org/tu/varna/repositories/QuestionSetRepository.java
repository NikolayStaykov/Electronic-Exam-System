package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.QuestionSet;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class QuestionSetRepository implements Repository<QuestionSet> {

    private static final String CREATE_QUERY = "INSERT INTO \"QuestionSet\" (\"QuestionSetName\", \"DisciplineId\", \"ParentQuestionSetId\", \"Info\") VALUES (?, ?, ?, ?) Returning \"Id\";";

    private static final String UPDATE_QUERY = "UPDATE \"QuestionSet\" SET \"QuestionSetName\" = ?, \"DisciplineId\" = ? \"ParentQuestionSetId\" = ?, \"Info\" = ? where \"Id\" = ?;";

    private static final String DELETE_QUERY = "DELETE FROM \"QuestionSet\" WHERE \"Id\" = ?;";

    private static final String FIND_QUERY = "SELECT * FROM \"QuestionSet\" WHERE 1 = 1 ";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(QuestionSet object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getName());
            statement.setLong(2, object.getDisciplineId());
            statement.setLong(3, object.getParentQuestionSetId());
            statement.setString(4, object.getInfo());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
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
            statement.setLong(3, object.getParentQuestionSetId());
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
                questionSets.add(new QuestionSet(resultSet.getLong("Id"),
                        resultSet.getString("QuestionSetName"),
                        resultSet.getString("Info"),
                        resultSet.getLong("DisciplineId"),
                        resultSet.getLong("ParentQuestionSetId")));
            }

            return questionSets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatQuery(QuestionSet searchTemplate){
        String query = FIND_QUERY;
        if(searchTemplate.getId() != null){
            query += " AND \"Id\" = ?";
        }
        if(searchTemplate.getParentQuestionSetId() != null){
            query += " AND \"ParentQuestionSetId\" = ?";
        }
        if(searchTemplate.getInfo() != null){
            query += " AND \"Info\" like ?";
        }
        if(searchTemplate.getDisciplineId() != null){
            query += " AND \"DisciplineId\" = ?";
        }
        return query;
    }

    private void formatParameters(PreparedStatement statement, QuestionSet searchTemplate) throws SQLException{
        int parameterIndex = 1;
        if(searchTemplate.getParentQuestionSetId() != null){
            statement.setLong(parameterIndex, searchTemplate.getParentQuestionSetId());
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
