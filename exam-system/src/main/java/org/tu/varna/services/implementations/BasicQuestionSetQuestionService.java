package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.repositories.connectors.ConnectionProvider;
import org.tu.varna.services.QuestionSetQuestionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ApplicationScoped
public class BasicQuestionSetQuestionService implements QuestionSetQuestionService {

    private static final String ADD_QUERY = "INSERT INTO \"QuestionSetQuestion\" (\"QuestionId\", \"QuestionSetId\") VALUES (?, ?);";

    private static final String DELETE_QUERY = "DELETE FROM \"QuestionSetQuestion\" WHERE \"QuestionId\" = ? and \"QuestionSetId\" = ?;";

    @Inject
    ConnectionProvider connectionProvider;

    @Override
    public void addQuestion(Long questionSetId, Long questionId) {
        executeAction(questionSetId, questionId, ADD_QUERY);
    }

    @Override
    public void removeQuestion(Long questionSetId, Long questionId) {
        executeAction(questionSetId, questionId, DELETE_QUERY);
    }

    private void executeAction(Long questionSetId, Long questionId, String deleteQuery) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setLong(1, questionId);
            preparedStatement.setLong(2, questionSetId);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
