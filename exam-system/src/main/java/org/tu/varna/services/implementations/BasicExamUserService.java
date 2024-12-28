package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Exam;
import org.tu.varna.objects.User;
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
    private static final String ADD_QUERY = "INSERT INTO \"ExamUser\" (\"ExamId\", \"UserId\") VALUES (?, ?);";

    private static final String REMOVE_QUERY = "DELETE FROM \"ExamUser\" WHERE \"ExamId\" = ? and \"UserId\" = ?;";

    private static final String FIND_USERS_QUERY = "SELECT * FROM \"User\" join \"ExamUser\" on \"UniversityID\" = \"UserId\" WHERE \"ExamId\" = ?;";

    private static final String FIND_EXAMS_QUERY = "SELECT * FROM \"Exam\" join \"ExamUser\" on \"Id\" = \"ExamId\" WHERE \"UserId\" = ?;";

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
                exams.add((new Exam(resultSet.getLong("Id"),
                        resultSet.getString("ExamName"),
                        resultSet.getLong("DisciplineId"),
                        resultSet.getLong("QuestionSetId"),
                        resultSet.getInt("TotalPoints"),
                        resultSet.getInt("Duration"),
                        resultSet.getTimestamp("StartDate"))));
            }
            statement.close();
            return exams;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<User> geeExamUsers(Long examId) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(FIND_USERS_QUERY);
            statement.setLong(1, examId);
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setUniversityId(resultSet.getString("UniversityID"));
                user.setEmail(resultSet.getString("Email"));
                user.setRole(resultSet.getString("Role"));
                users.add(user);
            }
            statement.close();
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
