package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Question;
import org.tu.varna.repositories.QuestionRepository;
import org.tu.varna.services.QuestionService;

import java.util.Collection;

@ApplicationScoped
public class BasicQuestionService implements QuestionService {

    @Inject
    QuestionRepository questionRepository;

    @Override
    public void createQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        Question toDelete = new Question();
        toDelete.setId(questionId);
        questionRepository.delete(toDelete);
    }

    @Override
    public void updateQuestion(Question question) {
        questionRepository.update(question);
    }

    @Override
    public Question getQuestion(Long questionId) {
        Question searchTemplate = new Question();
        searchTemplate.setId(questionId);
        Collection<Question> result = questionRepository.find(searchTemplate);
        return result.isEmpty() ? null : result.iterator().next();
    }

    @Override
    public Collection<Question> getQuestions(Question searchTemplate) {
        return questionRepository.find(searchTemplate);
    }
}
