package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.User;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
public class UserRepository implements Repository<User> {

    @Inject
    ConnectionProvider connectionProvider;

    private static final String CREATE_QUERY = "Insert into \"User\" (UniversityID, Email, Role) values (?, ?, ?) Returning \"UniversityID\"";

    private static final String UPDATE_QUERY = "Update \"User\" set Email = ?, Role = ? where UniversityID = ?";

    private static final String DELETE_QUERY = "Delete from \"User\" where UniversityID = ?";

    private static final String FIND_QUERY = "Select * from \"User\" where 1 = 1";


    @Override
    public void save(User object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getUniversityId());
            statement.setString(2, object.getEmail());
            statement.setString(3, object.getRole());
            statement.execute();
            statement.getResultSet().next();
            object.setUniversityId(statement.getResultSet().getString(1));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, object.getEmail());
            statement.setString(2, object.getRole());
            statement.setString(3, object.getUniversityId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User object) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setString(1, object.getUniversityId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<User> find(User template) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));

            formatParameters(statement, template);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<User> users = new ArrayList<>();
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

    private static String formatQuery(User template) {
        String query = FIND_QUERY;
        if(template.getDisciplines() != null && !template.getDisciplines().isEmpty()) {
            query = query.replace("where 1 = 1", "join \"UserDiscipline\" on \"UserId\" = \"UniversityID\" where \"DisciplineId\" = ?");
        }
        if(template.getUniversityId() != null) {
            query += " and \"UniversityID\" = ?";
        }
        if(template.getEmail() != null) {
            query += " and \"Email\" like ?";
        }
        if(template.getRole() != null) {
            query += " and \"Role\" = ?";
        }
        query += ";";
        return query;
    }

    private void formatParameters(PreparedStatement statement, User template) throws SQLException {
        int criteriaIndex = 1;
        if(template.getDisciplines() != null && !template.getDisciplines().isEmpty()) {
            statement.setLong(criteriaIndex, template.getDisciplines().stream().findFirst().get().getDisciplineId());
            criteriaIndex++;
        }
        if(template.getUniversityId() != null) {
            statement.setString(criteriaIndex, template.getUniversityId());
            criteriaIndex++;
        }
        if(template.getEmail() != null) {
            statement.setString(criteriaIndex, "%" + template.getEmail() + "%");
            criteriaIndex++;
        }
        if(template.getRole() != null) {
            statement.setString(criteriaIndex, template.getRole());
        }
    }
}
