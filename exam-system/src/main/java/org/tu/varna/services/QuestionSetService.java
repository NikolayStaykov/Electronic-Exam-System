package org.tu.varna.services;

import org.tu.varna.objects.QuestionSet;

import java.util.Collection;

public interface QuestionSetService {
    void save(QuestionSet questionSet);
    void update(QuestionSet questionSet);
    QuestionSet getQuestionSet(Long id, boolean loadChildSets, boolean loadQuestions);
    void delete(Long id);
    Collection<QuestionSet> getQuestionSets(QuestionSet searchTemplate, boolean loadChildSets, boolean loadQuestions);

}
