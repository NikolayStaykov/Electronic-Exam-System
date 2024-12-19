package org.tu.varna.services;

import org.tu.varna.objects.Question;

import java.util.Collection;

public interface QuestionService {
    void createQuestion(Question question);

    void deleteQuestion(Long questionId);

    void updateQuestion(Question question);

    Question getQuestion(Long questionId);

    Collection<Question> getQuestions(Question searchTemplate);
}
