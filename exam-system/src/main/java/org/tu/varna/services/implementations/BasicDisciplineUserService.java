package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.repositories.connectors.ConnectionProvider;
import org.tu.varna.services.DisciplineUserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ApplicationScoped
public class BasicDisciplineUserService implements DisciplineUserService {

    @Inject
    ConnectionProvider connectionProvider;

    private static final String INSERT_QUERY = "insert into \"UserDiscipline\" (\"UserId\" , \"DisciplineId\") values(?,?);";

    private static final String DELETE_QUERY = "delete from \"UserDiscipline\" where \"UserId\" = ? and \"DisciplineId\" = ?;";

    @Override
    public void addUserToDiscipline(Long disciplineId, String userUniversityId) {
        executeAction(disciplineId, userUniversityId, INSERT_QUERY);
    }

    @Override
    public void removeUserFromDiscipline(Long disciplineId, String userUniversityId) {
        executeAction(disciplineId, userUniversityId, DELETE_QUERY);
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
