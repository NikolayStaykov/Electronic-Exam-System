package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Answer;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
public class AnswerRepository implements Repository<Answer> {

    @Inject
    ConnectionProvider connectionProvider;

    private static final String CREATE_QUERY = "insert into answer (text, question_id, \"order\", fraction) values (?, ?, ?, ?) returning \"Id\";";

    private static final String UPDATE_QUERY = "update answer set question_id = ?, text = ?, \"order\" = ?, fraction = ? where \"Id\" = ?;";

    private static final String DELETE_QUERY = "delete from answer where id = ?;";

    private static final String FIND_QUERY = "select * from answer where 1 = 1";

    @Override
    public void save(Answer object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getAnswerText());
            statement.setLong(2, object.getQuestionId());
            if(object.getAnswerOrder() != null){
                statement.setInt(3, object.getAnswerOrder());
            } else {
                statement.setNull(3, Types.SMALLINT);
            }
            statement.setDouble(4, object.getFraction());
            statement.execute();
            statement.getResultSet().next();
            object.setId(statement.getResultSet().getLong(1));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Answer object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setLong(1, object.getQuestionId());
            statement.setString(2, object.getAnswerText());
            if(object.getAnswerOrder() != null){
                statement.setInt(3, object.getAnswerOrder());
            } else {
                statement.setNull(3, Types.SMALLINT);
            }
            statement.setDouble(4, object.getFraction());
            statement.setLong(5, object.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Answer object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setLong(1, object.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Answer> find(Answer template) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));

            formatParameters(statement,template);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Answer> answers = new ArrayList<>();
            while (resultSet.next()) {
                answers.add(new Answer(
                        resultSet.getLong("id"),
                        resultSet.getString("text"),
                        resultSet.getLong("question_id"),
                        resultSet.getDouble("fraction"),
                        resultSet.getInt("order")));
            }
            statement.close();
            return answers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatQuery(Answer template) {
        String query = FIND_QUERY;
        if(template.getId() != null) {
            query += " and id = ?";
        }
        if(template.getQuestionId() != null) {
            query += " and question_id = ?";
        }
        if(template.getAnswerText() != null) {
            query += " and text like ?";
        }
        if(template.getAnswerOrder() != null) {
            query += " and order = ?";
        }
        if(template.getFraction() != null) {
            query += " and fraction = ?";
        }
        return query;
    }

    private void formatParameters(PreparedStatement statement, Answer template) throws SQLException {
        int criteriaIndex = 1;
        if(template.getId() != null) {
            statement.setLong(criteriaIndex, template.getId());
            criteriaIndex++;
        }
        if(template.getQuestionId() != null) {
            statement.setLong(criteriaIndex, template.getQuestionId());
            criteriaIndex++;
        }
        if(template.getAnswerText() != null) {
            statement.setString(criteriaIndex, "%" + template.getAnswerText() + "%");
            criteriaIndex++;
        }
        if(template.getAnswerOrder() != null) {
            statement.setInt(criteriaIndex, template.getAnswerOrder());
            criteriaIndex++;
        }
        if(template.getFraction() != null) {
            statement.setDouble(criteriaIndex, template.getFraction());
        }
    }
}
