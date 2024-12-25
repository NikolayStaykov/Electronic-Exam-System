package org.tu.varna.services;

import org.tu.varna.objects.Question;

import java.util.Collection;

public interface QuestionService {
    void createQuestion(Question question);

    void deleteQuestion(Long questionId);

    void updateQuestion(Question question);

    Question getQuestion(Long questionId, boolean loadAnswers);

    Collection<Question> getQuestions(Question searchTemplate, boolean loadAnswers);

    Collection<Question> getQuestionsForQuestionSet(Long questionSetId, boolean loadAnswers);
}
