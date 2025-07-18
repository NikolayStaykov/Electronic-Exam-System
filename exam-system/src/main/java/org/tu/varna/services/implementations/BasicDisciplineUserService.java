package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.User;
import org.tu.varna.repositories.connectors.ConnectionProvider;
import org.tu.varna.services.DisciplineUserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class BasicDisciplineUserService implements DisciplineUserService {

    @Inject
    ConnectionProvider connectionProvider;

    private static final String INSERT_QUERY = "insert into user_discipline (user_id, discipline_id) values(?,?);";

    private static final String DELETE_QUERY = "delete from user_discipline where user_id = ? and discipline_id = ?;";

    private static final String DELETE_ALL_QUERY = "delete from user_discipline where discipline_id = ?;";

    private static final String FIND_USERS_QUERY = "select * from \"user\" join user_discipline on user_id = university_id where discipline_id = ?;";

    private static final String FIND_DISCIPLINES_QUERY = "select * from discipline join user_discipline on id = discipline_id where user_id = ?;";

    @Override
    public void addUserToDiscipline(Long disciplineId, String userUniversityId) {
        executeAction(disciplineId, userUniversityId, INSERT_QUERY);
    }

    @Override
    public void removeUserFromDiscipline(Long disciplineId, String userUniversityId) {
        executeAction(disciplineId, userUniversityId, DELETE_QUERY);
    }

    @Override
    public void removeAllUsersFromDiscipline(Long disciplineId) {
        executeDeleteAll(disciplineId);
    }

    @Override
    public Collection<User> getUsersForDiscipline(Long disciplineId) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_USERS_QUERY);
            preparedStatement.setLong(1, disciplineId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while(resultSet.next()){
                User user = new User();
                user.setUniversityId(resultSet.getString("university_id"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Discipline> getDisciplinesForUser(String userUniversityId) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_DISCIPLINES_QUERY);
            preparedStatement.setString(1, userUniversityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Discipline> disciplines = new ArrayList<>();
            while(resultSet.next()){
                disciplines.add(new Discipline(
                        resultSet.getLong("id"),
                        resultSet.getString("name")));
            }
            return disciplines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeAction(Long disciplineId, String userUniversityId, String query) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userUniversityId);
            statement.setLong(2, disciplineId);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeDeleteAll(Long disciplineId) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_ALL_QUERY);
            statement.setLong(1, disciplineId);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
