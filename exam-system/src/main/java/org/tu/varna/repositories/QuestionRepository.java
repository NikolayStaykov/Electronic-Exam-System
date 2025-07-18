package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.Question;
import org.tu.varna.common.QuestionType;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
public class QuestionRepository implements Repository<Question>{

    private static final String CREATE_QUERY = "insert into question (text, type, discipline_id,default_grade,penalty) Values(?, ?, ?, ?, ?) returning id;";

    private static final String UPDATE_QUERY = "update question set text = ?, type = ?, discipline_id = ?, default_grade = ?, penalty = ? where id = ?";

    private static final String DELETE_QUERY = "delete from question where id = ?";

    private static final String FIND_QUERY = "select * from question where 1 = 1";

    private static final String FIND_QUERY_QUESTION_SET = "select * from question join question_set_question on question_id = id where question_set_id = ?";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(Question object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getQuestionText());
            statement.setString(2, object.getQuestionType().toString());
            statement.setLong(3, object.getDiscipline().getDisciplineId());
            statement.setDouble(4,object.getDefaultGrade());
            statement.setDouble(5,object.getPenalty());
            statement.execute();
            statement.getResultSet().next();
            object.setId(statement.getResultSet().getLong(1));
            statement.close();
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
            statement.setDouble(4,object.getDefaultGrade());
            statement.setDouble(5,object.getPenalty());
            statement.setLong(6, object.getId());
            statement.execute();
            statement.close();
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
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Question> find(Question template) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));

            formatParameters(statement, template);

            Collection<Question> result = processSearch(statement);
            statement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatQuery(Question template) {
        String query = FIND_QUERY;
        if(template.getId() != null) {
            query += " and id = ?";
        }
        if(template.getQuestionText() != null) {
            query += " and text like ?";
        }
        if(template.getDiscipline() != null) {
            query += " and discipline_id = ?";
        }
        if(template.getQuestionType() != null) {
            query += " and type = ?";
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

    public Collection<Question> findAllQuestionsForQuestionSet(Long questionSetId) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(FIND_QUERY_QUESTION_SET);
            statement.setLong(1, questionSetId);
            Collection<Question> result = processSearch(statement);
            statement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<Question> processSearch(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Question> questions = new ArrayList<>();
        while (resultSet.next()) {
            questions.add(new Question(
                    resultSet.getLong("id"),
                    resultSet.getString("text"),
                    QuestionType.valueOf(resultSet.getString("type")),
                    new Discipline(resultSet.getLong("discipline_id"),null),
                    null,
                    resultSet.getDouble("default_grade"),
                    resultSet.getDouble("penalty")));
        }
        return questions;
    }
}
