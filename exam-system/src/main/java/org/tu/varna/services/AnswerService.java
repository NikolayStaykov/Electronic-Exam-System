package org.tu.varna.services;

import org.tu.varna.objects.Answer;

import java.util.Collection;

public interface AnswerService {
    void createAnswer(Answer answer);

    void deleteAnswer(Long answerId);

    void updateAnswer(Answer answer);

    Answer getAnswer(Long answerId);

    Collection<Answer> getAnswers(Answer searchTemplate);
}
