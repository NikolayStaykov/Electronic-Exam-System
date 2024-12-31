package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Answer;
import org.tu.varna.repositories.AnswerRepository;
import org.tu.varna.services.AnswerService;

import java.util.Collection;

@ApplicationScoped
public class BasicAnswerService implements AnswerService {

    @Inject
    AnswerRepository answerRepository;

    @Override
    public void createAnswer(Answer answer) {
        answerRepository.save(answer);
    }

    @Override
    public void deleteAnswer(Long answerId) {
        Answer toDelete = new Answer();
        toDelete.setId(answerId);
        answerRepository.delete(toDelete);
    }

    @Override
    public void updateAnswer(Answer answer) {
        answerRepository.update(answer);
    }

    @Override
    public Answer getAnswer(Long answerId) {
        Answer searchTemplate = new Answer();
        searchTemplate.setId(answerId);
        Collection<Answer> result = answerRepository.find(searchTemplate);
        return result.isEmpty() ? null : result.iterator().next();
    }

    @Override
    public Collection<Answer> getAnswers(Answer searchTemplate) {
       return answerRepository.find(searchTemplate);
    }
}
