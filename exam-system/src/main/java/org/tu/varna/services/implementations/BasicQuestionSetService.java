package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.QuestionSet;
import org.tu.varna.repositories.QuestionSetRepository;
import org.tu.varna.services.QuestionService;
import org.tu.varna.services.QuestionSetService;

import java.util.Collection;
import java.util.HashSet;

@ApplicationScoped
public class BasicQuestionSetService implements QuestionSetService {

    @Inject
    QuestionSetRepository questionSetRepository;

    @Inject
    QuestionService questionService;

    @Override
    public void save(QuestionSet questionSet) {
        questionSetRepository.save(questionSet);
    }

    @Override
    public void update(QuestionSet questionSet) {
        questionSetRepository.update(questionSet);
    }

    @Override
    public QuestionSet getQuestionSet(Long id, boolean loadChildSets, boolean loadQuestions) {
        QuestionSet searchTemplate = new QuestionSet();
        searchTemplate.setId(id);
        Collection<QuestionSet> questionSets = questionSetRepository.find(searchTemplate);
        QuestionSet result = questionSets.isEmpty() ? null : questionSets.iterator().next();
        if(loadChildSets && result != null) {
                QuestionSet childQuestionSetTemplate = new QuestionSet();
                childQuestionSetTemplate.setParentQuestionSetId(result.getId());
                result.setChildQuestionSets(new HashSet<>(getQuestionSets(childQuestionSetTemplate, true,loadQuestions)));
        }
        if(loadQuestions && result != null) {
                result.setQuestions(new HashSet<>(questionService.getQuestionsForQuestionSet(result.getId(),true)));
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        QuestionSet toDelete = new QuestionSet();
        toDelete.setId(id);
        questionSetRepository.delete(toDelete);
    }

    @Override
    public Collection<QuestionSet> getQuestionSets(QuestionSet searchTemplate, boolean loadChildSets, boolean loadQuestions) {
        Collection<QuestionSet> questionSets = questionSetRepository.find(searchTemplate);
        if(loadChildSets){
            for(QuestionSet questionSet : questionSets){
                QuestionSet childQuestionSetTemplate = new QuestionSet();
                childQuestionSetTemplate.setParentQuestionSetId(questionSet.getId());
                questionSet.setChildQuestionSets(new HashSet<>(getQuestionSets(childQuestionSetTemplate, true,loadQuestions)));
            }
        }
        if(loadQuestions){
            for(QuestionSet questionSet : questionSets){
                questionSet.setQuestions(new HashSet<>(questionService.getQuestionsForQuestionSet(questionSet.getId(),true)));
            }
        }
        return questionSets;
    }
}
