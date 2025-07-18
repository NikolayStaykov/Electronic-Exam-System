package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Exam;
import org.tu.varna.entities.User;
import org.tu.varna.repositories.connectors.ConnectionProvider;
import org.tu.varna.services.ExamUserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class BasicExamUserService implements ExamUserService {
    private static final String ADD_QUERY = "insert into exam_user (exam_id,user_id) values (?, ?);";

    private static final String REMOVE_QUERY = "delete from exam_user where exam_id = ? and user_id = ?;";

    private static final String FIND_USERS_QUERY = "select * from \"user\" join exam_user on university_id = user_id WHERE exam_id = ?;";

    private static final String FIND_EXAMS_QUERY = "select * from exam join exam_user on id = exam_id where user_id = ?;";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void addUserToExam(String userId, Long examId) {
        executeAction(userId, examId, ADD_QUERY);
    }

    @Override
    public void removeUserFromExam(String userId, Long examId) {
        executeAction(userId, examId, REMOVE_QUERY);
    }

    private void executeAction(String userId, Long examId, String removeQuery) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(removeQuery);
            statement.setLong(1, examId);
            statement.setString(2, userId);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Exam> getUserExams(String userId) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_EXAMS_QUERY);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Exam> exams = new ArrayList<>();
            while (resultSet.next()) {
                exams.add(new Exam(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getLong("discipline_id"),
                        resultSet.getLong("question_set_id"),
                        resultSet.getInt("total_points"),
                        resultSet.getInt("duration"),
                        resultSet.getTimestamp("start_time"),
                        resultSet.getInt("number_of_questions")));
            }
            statement.close();
            return exams;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<User> getExamUsers(Long examId) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_USERS_QUERY);
            statement.setLong(1, examId);
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setUniversityId(resultSet.getString("university_id"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                users.add(user);
            }
            statement.close();
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
