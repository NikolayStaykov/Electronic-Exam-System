package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.ExamAttempt;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class ExamAttemptRepository implements Repository<ExamAttempt> {

    private static final String CREATE_QUERY = "insert into exam_attempt (student_id, start_date,exam_id) values (?, ?, ?) returning id;";

    private static final String UPDATE_QUERY = "update exam_attempt set student_id = ? , start_date = ?, exam_id =?  where id = ?;";

    private static final String DELETE_QUERY = "delete from exam_attempt where id = ?;";

    private static final String FIND_QUERY = "select * from exam_attempt where 1 = 1";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(ExamAttempt object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getStudentId());
            statement.setTimestamp(2,object.getStartTime());
            statement.setLong(3,object.getExamId());
            statement.execute();
            statement.getResultSet().next();
            object.setId(statement.getResultSet().getLong(1));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ExamAttempt object) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, object.getStudentId());
            statement.setTimestamp(2,object.getStartTime());
            statement.setLong(3,object.getExamId());
            statement.setLong(4,object.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(ExamAttempt object) {
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
    public Collection<ExamAttempt> find(ExamAttempt template) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));
            formatParameters(statement, template);
            List<ExamAttempt> attempts = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                attempts.add(new ExamAttempt(resultSet.getLong("id"),
                        resultSet.getString("student_id"),
                        resultSet.getLong("exam_id"),
                        resultSet.getTimestamp("start_date")));
            }
            resultSet.close();
            statement.close();
            return attempts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatQuery(ExamAttempt template) {
        String query = FIND_QUERY;
        if(template.getId() != null){
            query += " AND id = ?";
        }
        if(template.getStudentId() != null){
            query += " AND student_id = ?";
        }
        if(template.getExamId() != null){
            query += " AND exam_id = ?";
        }
        if(template.getStartTime() != null){
            query += " AND start_date = ?";
        }
        return query;
    }

    private void formatParameters(PreparedStatement statement, ExamAttempt template) throws SQLException {
        int paramIndex = 1;
        if(template.getId() != null){
            statement.setLong(paramIndex++, template.getId());
        }
        if(template.getStudentId() != null){
            statement.setString(paramIndex++, template.getStudentId());
        }
        if(template.getExamId() != null){
            statement.setLong(paramIndex++, template.getExamId());
        }
        if(template.getStartTime() != null){
            statement.setTimestamp(paramIndex, template.getStartTime());
        }
    }
}
