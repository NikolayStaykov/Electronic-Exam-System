package org.tu.varna.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.User;
import org.tu.varna.repositories.connectors.ConnectionProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@ApplicationScoped
public class DisciplineRepository implements Repository<Discipline> {

    private static final String CREATE_QUERY = "insert into \"Discipline\" (\"DisciplineName\") Values(?) Returning \"Id\";";

    private static final String UPDATE_QUERY = "update \"Discipline\" set \"DisciplineName\" = ? where \"Id\" = ?";

    private static final String DELETE_QUERY = "delete from \"Discipline\" where \"Id\" = ?";

    private static final String FIND_QUERY = "select * from \"Discipline\" where 1 = 1";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void save(Discipline object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, object.getDisciplineName());
            statement.execute();
            statement.getResultSet().next();
            object.setDisciplineId(statement.getResultSet().getLong(1));
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Discipline object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, object.getDisciplineName());
            statement.setLong(2, object.getDisciplineId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Discipline object) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setLong(1, object.getDisciplineId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Discipline> find(Discipline template) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(template));

            formatParameters(statement, template);

            ResultSet resultSet = statement.executeQuery();

            ArrayList<Discipline> disciplines = new ArrayList<>();
            while (resultSet.next()) {
                disciplines.add(new Discipline(
                        resultSet.getLong("Id"),
                        resultSet.getString("DisciplineName")));
            }
            statement.close();
            return disciplines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatQuery(Discipline template) {
        String query = FIND_QUERY;
        if(template.getUsers() != null && !template.getUsers().isEmpty()) {
            query = query.replace("where 1 = 1", "join \"UserDiscipline\" on \"DisciplineId\" = \"Id\" where \"UserId\" = ?");
        }
        if(template.getDisciplineId() != null) {
            query += " and \"Id\" = ?";
        }
        if(template.getDisciplineName() != null) {
            query += " and \"DisciplineName\" like ?";
        }
        query += ";";
        return query;
    }

    private void formatParameters(PreparedStatement statement, Discipline template) throws SQLException {
        int criteriaIndex = 1;
        if(template.getUsers() != null && !template.getUsers().isEmpty()) {
            statement.setString(criteriaIndex, template.getUsers().stream().findFirst().get().getUniversityId());
            criteriaIndex++;
        }
        if(template.getDisciplineId() != null) {
            statement.setLong(criteriaIndex, template.getDisciplineId());
            criteriaIndex++;
        }
        if(template.getDisciplineName() != null) {
            statement.setString(criteriaIndex, "%" + template.getDisciplineName() + "%");
        }
    }
}
