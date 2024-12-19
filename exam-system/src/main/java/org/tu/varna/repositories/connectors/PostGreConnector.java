package org.tu.varna.repositories.connectors;

import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ApplicationScoped
public class PostGreConnector implements ConnectionProvider {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/Electronic_Exam_Db?user=postgres&password=MyStrongPass1!";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
