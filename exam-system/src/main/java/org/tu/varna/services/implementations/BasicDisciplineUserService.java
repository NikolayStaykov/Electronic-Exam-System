package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.User;
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

    private static final String INSERT_QUERY = "insert into \"UserDiscipline\" (\"UserId\" , \"DisciplineId\") values(?,?);";

    private static final String DELETE_QUERY = "delete from \"UserDiscipline\" where \"UserId\" = ? and \"DisciplineId\" = ?;";

    private static final String FIND_USERS_QUERY = "select * from \"User\" join \"UserDiscipline\" on \"UserId\" = \"UniversityID\" where \"DisciplineId\" = ?;";

    private static final String FIND_DISCIPLINES_QUERY = "select * from \"Discipline\" join \"UserDiscipline\" on \"Id\" = \"DisciplineId\" where \"UserId\" = ?;";

    @Override
    public void addUserToDiscipline(Long disciplineId, String userUniversityId) {
        executeAction(disciplineId, userUniversityId, INSERT_QUERY);
    }

    @Override
    public void removeUserFromDiscipline(Long disciplineId, String userUniversityId) {
        executeAction(disciplineId, userUniversityId, DELETE_QUERY);
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
                user.setUniversityId(resultSet.getString("UniversityID"));
                user.setEmail(resultSet.getString("Email"));
                user.setRole(resultSet.getString("Role"));
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
                        resultSet.getLong("Id"),
                        resultSet.getString("DisciplineName")));
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
}
