package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Question;
import org.tu.varna.objects.QuestionType;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
public class QuestionRepository implements Repository<Question>{

    private static final String CREATE_QUERY = "insert into \"Question\" (\"QuestionText\", \"QuestionType\", \"DisciplineId\") Values(?, ?, ?);";

    private static final String UPDATE_QUERY = "update \"Question\" set \"QuestionText\" = ?, \"QuestionType\" = ?, \"DisciplineId\" = ? where \"Id\" = ?";

    private static final String DELETE_QUERY = "delete from \"Question\" where \"Id\" = ?";

    private static final String FIND_QUERY = "select * from \"Question\" where 1 = 1";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(Question object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getQuestionText());
            statement.setString(2, object.getQuestionType().toString());
            statement.setLong(3, object.getDiscipline().getDisciplineId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Question object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, object.getQuestionText());
            statement.setString(2, object.getQuestionType().toString());
            statement.setLong(3, object.getDiscipline().getDisciplineId());
            statement.setLong(4, object.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Question object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setLong(1, object.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Question> find(Question template) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));

            formatParameters(statement, template);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<Question> questions = new ArrayList<>();
            while (resultSet.next()) {
                questions.add(new Question(
                        resultSet.getLong("Id"),
                        resultSet.getString("QuestionText"),
                        QuestionType.valueOf(resultSet.getString("QuestionType"))));
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatQuery(Question template) {
        String query = FIND_QUERY;
        if(template.getId() != null) {
            query += " and \"Id\" = ?";
        }
        if(template.getQuestionText() != null) {
            query += " and \"QuestionText\" like ?";
        }
        if(template.getDiscipline() != null) {
            query += " and \"DisciplineId\" = ?";
        }
        if(template.getQuestionType() != null) {
            query += " and \"QuestionType\" = ?";
        }
        query += ";";
        return query;
    }

    private void formatParameters(PreparedStatement statement, Question template) throws SQLException {
        int criteriaIndex = 1;
        if(template.getId() != null) {
            statement.setLong(criteriaIndex, template.getId());
            criteriaIndex++;
        }
        if(template.getQuestionText() != null) {
            statement.setString(criteriaIndex, "%" + template.getQuestionText() + "%");
            criteriaIndex++;
        }
        if(template.getDiscipline() != null) {
            statement.setLong(criteriaIndex, template.getDiscipline().getDisciplineId());
            criteriaIndex++;
        }
        if(template.getQuestionType() != null) {
            statement.setString(criteriaIndex, template.getQuestionType().toString());
        }
    }
}