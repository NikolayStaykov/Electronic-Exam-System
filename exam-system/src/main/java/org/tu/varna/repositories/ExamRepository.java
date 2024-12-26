package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Exam;
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
    private static final String CREATE_QUERY = "INSERT INTO \"Exam\"(\"DisciplineId\", \"QuestionSetId\", \"ExamName\", \"TotalPoints\", \"StartDate\", \"Duration\") VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String DELETE_QUERY = "DELETE FROM \"Exam\" WHERE \"Id\" = ?;";

    private static final String FIND_QUERY = "SELECT * FROM \"Exam\" WHERE 1 = 1;";

    private static final String UPDATE_QUERY = "UPDATE \"Exam\" SET  \"DisciplineId\" = ?, \"QuestionSetId\" = ?, \"ExamName\" = ?, \"TotalPoints\" = ?, \"StartDate\" = ?, \"Duration\" = ? WHERE \"Id\" = ?;";

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
            preparedStatement.execute();
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
            preparedStatement.setLong(7, object.getId());
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
                result.add(new Exam(resultSet.getLong("Id"),
                        resultSet.getString("ExamName"),
                        resultSet.getLong("DisciplineId"),
                        resultSet.getLong("QuestionSetId"),
                        resultSet.getInt("TotalPoints"),
                        resultSet.getInt("Duration"),
                        resultSet.getTimestamp("StartDate")));
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
            query.append(" AND \"Id\" = ?");
        }
        if(searchTemplate.getDisciplineId() != null) {
            query.append(" AND \"DisciplineId\" = ?");
        }
        if(searchTemplate.getQuestionSetId() != null) {
            query.append(" AND \"QuestionSetId\" = ?");
        }
        if(searchTemplate.getExamName() != null) {
            query.append(" AND \"ExamName\" = ?");
        }
        if(searchTemplate.getTotalPoints() != null) {
            query.append(" AND \"TotalPoints\" = ?");
        }
        if(searchTemplate.getStartDate() != null) {
            query.append(" AND \"StartDate\" = ?");
        }
        if(searchTemplate.getDurationMinutes() != null) {
            query.append(" AND \"Duration\" = ?");
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
