package org.tu.varna.services;

public interface QuestionSetQuestionService {
    void addQuestion(Long questionSetId, Long questionId);
    void removeQuestion(Long questionSetId, Long questionId);
    void removeAllQuestions(Long questionSetId);
}
