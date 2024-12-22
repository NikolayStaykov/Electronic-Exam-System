package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Answer;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
public class AnswerRepository implements Repository<Answer> {

    @Inject
    ConnectionProvider connectionProvider;

    private static final String CREATE_QUERY = "insert into \"Answer\" (\"QuestionId\", \"AnswerText\", \"Answer_Order\", \"Fraction\") Values(?, ?, ?, ?) Returning \"Id\";";

    private static final String UPDATE_QUERY = "update \"Answer\" set \"QuestionId\" = ?, \"AnswerText\" = ?, \"Answer_Order\" = ?, \"Fraction\" = ? where \"Id\" = ?;";

    private static final String DELETE_QUERY = "delete from \"Answer\" where \"Id\" = ?;";

    private static final String FIND_QUERY = "select * from \"Answer\" where 1 = 1";

    @Override
    public void save(Answer object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setLong(1, object.getQuestionId());
            statement.setString(2, object.getAnswerText());
            statement.setInt(3, object.getAnswerOrder());
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
            statement.setInt(3, object.getAnswerOrder());
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
                        resultSet.getLong("Id"),
                        resultSet.getString("AnswerText"),
                        resultSet.getLong("QuestionId"),
                        resultSet.getDouble("Fraction"),
                        resultSet.getInt("AnswerOrder")));
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
            query += " and \"Id\" = ?";
        }
        if(template.getQuestionId() != null) {
            query += " and \"QuestionId\" = ?";
        }
        if(template.getAnswerText() != null) {
            query += " and \"AnswerText\" like ?";
        }
        if(template.getAnswerOrder() != null) {
            query += " and \"Answer_Order\" = ?";
        }
        if(template.getFraction() != null) {
            query += " and \"Fraction\" = ?";
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
