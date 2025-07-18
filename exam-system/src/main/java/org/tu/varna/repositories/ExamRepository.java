package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Exam;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@ApplicationScoped
public class ExamRepository implements Repository<Exam> {
    private static final String CREATE_QUERY = "insert into exam (discipline_id, question_set_id, name, total_points, start_time, duration,number_of_questions)" +
            " values (?, ?, ?, ?, ?, ?, ?) returning id;";

    private static final String DELETE_QUERY = "delete from exam where id = ?;";

    private static final String FIND_QUERY = "select * from exam where 1 = 1";

    private static final String UPDATE_QUERY = "update exam set  discipline_id = ?, question_set_id = ?, name = ?, total_points = ?, start_time = ?, duration = ?, number_of_questions = ? where id = ?;";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(Exam object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY);
            preparedStatement.setLong(1, object.getDisciplineId());
            preparedStatement.setLong(2, object.getQuestionSetId());
            preparedStatement.setString(3, object.getExamName());
            preparedStatement.setInt(4, object.getTotalPoints());
            preparedStatement.setTimestamp(5, object.getStartDate());
            preparedStatement.setInt(6,object.getDurationMinutes());
            preparedStatement.setInt(7, object.getNumberOfQuestions());
            preparedStatement.execute();
            preparedStatement.getResultSet().next();
            object.setId(preparedStatement.getResultSet().getLong(1));
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Exam object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setLong(1, object.getDisciplineId());
            preparedStatement.setLong(2, object.getQuestionSetId());
            preparedStatement.setString(3, object.getExamName());
            preparedStatement.setInt(4, object.getTotalPoints());
            preparedStatement.setTimestamp(5, object.getStartDate());
            preparedStatement.setInt(6,object.getDurationMinutes());
            preparedStatement.setInt(7, object.getNumberOfQuestions());
            preparedStatement.setLong(8, object.getId());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Exam object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setLong(1,object.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Exam> find(Exam template) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(formatQuery(template));
            formatParameters(preparedStatement, template);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Exam> result = new ArrayList<>();
            while(resultSet.next()){
                result.add(new Exam(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getLong("discipline_id"),
                        resultSet.getLong("question_set_id"),
                        resultSet.getInt("total_points"),
                        resultSet.getInt("duration"),
                        resultSet.getTimestamp("start_time"),
                        resultSet.getInt("number_of_questions")));
            }
            resultSet.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatQuery(Exam searchTemplate) {
        StringBuilder query = new StringBuilder(FIND_QUERY);
        if(searchTemplate.getId() != null) {
            query.append(" AND id = ?");
        }
        if(searchTemplate.getDisciplineId() != null) {
            query.append(" AND discipline_id = ?");
        }
        if(searchTemplate.getQuestionSetId() != null) {
            query.append(" AND question_set_id = ?");
        }
        if(searchTemplate.getExamName() != null) {
            query.append(" AND name = ?");
        }
        if(searchTemplate.getTotalPoints() != null) {
            query.append(" AND total_points = ?");
        }
        if(searchTemplate.getStartDate() != null) {
            query.append(" AND start_time = ?");
        }
        if(searchTemplate.getDurationMinutes() != null) {
            query.append(" AND duration = ?");
        }
        return query.toString();
    }

    private void formatParameters(PreparedStatement preparedStatement, Exam searchTemplate) throws SQLException {
        int parameterIndex = 1;
        if(searchTemplate.getId() != null) {
            preparedStatement.setLong(parameterIndex,searchTemplate.getId());
            parameterIndex++;
        }
        if(searchTemplate.getDisciplineId() != null) {
            preparedStatement.setLong(parameterIndex,searchTemplate.getDisciplineId());
            parameterIndex++;
        }
        if(searchTemplate.getQuestionSetId() != null) {
            preparedStatement.setLong(parameterIndex,searchTemplate.getQuestionSetId());
            parameterIndex++;
        }
        if(searchTemplate.getExamName() != null) {
            preparedStatement.setString(parameterIndex,searchTemplate.getExamName());
            parameterIndex++;
        }
        if(searchTemplate.getTotalPoints() != null) {
            preparedStatement.setInt(parameterIndex,searchTemplate.getTotalPoints());
            parameterIndex++;
        }
        if (searchTemplate.getStartDate() != null) {
            preparedStatement.setTimestamp(parameterIndex,searchTemplate.getStartDate());
        }
        if (searchTemplate.getDurationMinutes() != null) {
            preparedStatement.setInt(parameterIndex,searchTemplate.getDurationMinutes());
        }
    }
}
