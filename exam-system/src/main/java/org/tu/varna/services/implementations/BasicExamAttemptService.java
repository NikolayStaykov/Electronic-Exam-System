package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.common.ExamAttemptUtils;
import org.tu.varna.dto.ExamResultDto;
import org.tu.varna.entities.*;
import org.tu.varna.repositories.ExamAttemptRepository;
import org.tu.varna.repositories.connectors.ConnectionProvider;
import org.tu.varna.services.ExamAttemptService;
import org.tu.varna.services.ExamService;
import org.tu.varna.services.QuestionService;
import org.tu.varna.services.QuestionSetService;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class BasicExamAttemptService implements ExamAttemptService {

    private static final String FIND_EXAM_ATTEMPTS_QUERY = "select * from exam_attempt where 1 = 1;";

    private static final String ADD_QUESTION_TO_EXAM_ATTEMPT = "insert into exam_attempt_question( exam_attempt_id, question_id)  values (?, ?);";

    private static final String ADD_ANSWER_TO_EXAM_ATTEMPT_QUESTION = "insert into exam_attempt_question_answer (exam_attempt_id, question_id, answer_id) values (?, ?, ?);";

    private static final String FIND_SUBMITTED_ANSWERS_FOR_EXAM_ATTEMPT = "SELECT * FROM answer JOIN  exam_attempt_question_answer ON id = answer_id WHERE exam_attempt_id = ? AND question_id = ?;";

    @Inject
    ConnectionProvider connectionProvider;

    @Inject
    QuestionSetService questionSetService;

    @Inject
    QuestionService questionService;

    @Inject
    ExamAttemptRepository examAttemptRepository;

    @Inject
    ExamService examService;

    @Override
    public ExamAttempt initateExamAttempt(Exam exam, String studentId) {
        ExamAttempt attempt = new ExamAttempt();
        attempt.setExamId(exam.getId());
        attempt.setStudentId(studentId);
        attempt.setStartTime(Timestamp.from(Instant.now()));
        examAttemptRepository.save(attempt);
        attempt.setQuestionIds(generateQuestionIdsForAttempt(exam));
        addQuestionToExamAttempt(attempt.getId(),attempt.getQuestionIds());
        return attempt;
    }

    @Override
    public String answerQuestion(Question question, Long examAttemptId) {
        try (Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(ADD_ANSWER_TO_EXAM_ATTEMPT_QUESTION);
            statement.setLong(1,examAttemptId);
            statement.setLong(2,question.getId());
            for(Answer answer : question.getAnswers()){
                statement.setLong(3,answer.getId());
                statement.execute();
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Answer submitted.";
    }

    @Override
    public Collection<ExamResultDto> getExamResults(String userId, Long examId) {
        try(Connection connection = connectionProvider.getConnection()){
            PreparedStatement statement = connection.prepareStatement(formatQuery(userId, examId));
            int parameterIndex = 1;
            if(userId != null){
                statement.setString(parameterIndex++, userId);
            }
            if(examId != null){
                statement.setLong(parameterIndex, examId);
            }
            ResultSet resultSet = statement.executeQuery();
            List<ExamResultDto> examResults = new ArrayList<>();
            while(resultSet.next()) {
                ExamAttempt attempt = new ExamAttempt(
                        resultSet.getLong("id"),
                        resultSet.getString("student_id"),
                        resultSet.getLong("exam_id"),
                        resultSet.getTimestamp("start_date")
                );
                ExamResultDto dto = new ExamResultDto();
                dto.setExamAttempt(attempt);
                dto.setExam(examService.getExam(attempt.getExamId()));
                dto.setQuestions(getQuestionsForExamAttempt(attempt));
                examResults.add(dto);
            }
            return examResults;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatQuery(String userId, Long examId){
        String query = FIND_EXAM_ATTEMPTS_QUERY;
        if(userId != null){
            query += " AND student_id = ?";
        }
        if(examId != null){
            query += " AND exam_id = ?";
        }
        return query;
    }

    private Set<Long> generateQuestionIdsForAttempt(Exam exam) {
        QuestionSet parentQuestionSet = questionSetService.getQuestionSet(exam.getId(),true,true);
        List<QuestionSet> questionSets = getQuestionSets(parentQuestionSet);
        Set<Question> allQuestions = new HashSet<>();
        Set<Question> currentQuestions = new HashSet<>();
        questionSets.forEach(set -> allQuestions.addAll(set.getQuestions()));
        if(allQuestions.size() < exam.getNumberOfQuestions()){
            throw new RuntimeException("Unable to start exam due to insufficient number of questions.");
        }
        if(allQuestions.stream().mapToDouble(Question::getDefaultGrade).sum() < exam.getTotalPoints()){
            throw new RuntimeException("Unable to start exam due to insufficient sum of points.");
        }
        Map<Double,List<Question>> questionsMap = new HashMap<>();
        for(Question question : allQuestions){
            if(!questionsMap.containsKey(question.getDefaultGrade())){
                questionsMap.put(question.getDefaultGrade(), new ArrayList<>());
            }
            questionsMap.get(question.getDefaultGrade()).add(question);
        }
        ArrayList<ArrayList<Double>> variations = ExamAttemptUtils.findPointCombinations(questionsMap,exam.getTotalPoints(),exam.getNumberOfQuestions());
        if(variations.isEmpty()){
            throw new RuntimeException("Unable to generate exam with current question set.");
        }
        Random random = new Random();
        ArrayList<Double> variation = variations.get(random.nextInt(variations.size()));
        for (Double point : variation) {
            Question question = questionsMap.get(point).get(random.nextInt(questionsMap.get(point).size()));
            currentQuestions.add(question);
            questionsMap.get(point).remove(question);
        }
        return currentQuestions.stream().map(Question::getId).collect(Collectors.toSet());
    }

    private List<QuestionSet> getQuestionSets(QuestionSet questionSet) {
        Set<QuestionSet> questionSets = new HashSet<>();
        questionSets.add(questionSet);
        questionSet.getChildQuestionSets().forEach(childQuestionSet -> questionSets.addAll(getQuestionSets(childQuestionSet)));
        return questionSets.stream().toList();
    }

    private void addQuestionToExamAttempt(Long examAttemptId, Set<Long> questionIds) {
        try(Connection connection = connectionProvider.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUESTION_TO_EXAM_ATTEMPT);
            statement.setLong(1, examAttemptId);
            for(Long questionId : questionIds){
                statement.setLong(2, questionId);
                statement.execute();
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Question,Set<Answer>> getQuestionsForExamAttempt(ExamAttempt attempt) {
        Map<Question,Set<Answer>> result = new HashMap<>();
        attempt.getQuestionIds().forEach(questionId -> result.put(questionService.getQuestion(questionId,true), getSubmittedAnswers(attempt.getId(),questionId)));
        return result;
    }

    private Set<Answer> getSubmittedAnswers(Long examAttemptId, Long questionId) {
        try(Connection connection = connectionProvider.getConnection()){
            Set<Answer> submittedAnswers = new HashSet<>();
            PreparedStatement statement = connection.prepareStatement(FIND_SUBMITTED_ANSWERS_FOR_EXAM_ATTEMPT);
            statement.setLong(1, examAttemptId);
            statement.setLong(2, questionId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                submittedAnswers.add(new Answer(
                        resultSet.getLong("id"),
                        resultSet.getString("text"),
                        resultSet.getLong("question_id"),
                        resultSet.getDouble("fraction"),
                        resultSet.getInt("order")));
            }
            return submittedAnswers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
