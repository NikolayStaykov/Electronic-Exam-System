package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Answer;
import org.tu.varna.entities.Question;
import org.tu.varna.repositories.QuestionRepository;
import org.tu.varna.services.AnswerService;
import org.tu.varna.services.QuestionService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class BasicQuestionService implements QuestionService {

    @Inject
    QuestionRepository questionRepository;

    @Inject
    AnswerService answerService;

    @Override
    public void createQuestion(Question question) {

        questionRepository.save(question);
        question.getAnswers().forEach(answer->{
            answer.setQuestionId(question.getId());
            answerService.createAnswer(answer);
        });

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
        question.getAnswers().forEach(answer->{
            answer.setQuestionId(question.getId());
            if(answer.getId() != null){
                answerService.createAnswer(answer);
            } else {
                answerService.updateAnswer(answer);
            }
        });
    }

    @Override
    public Question getQuestion(Long questionId, boolean loadAnswers) {
        Question searchTemplate = new Question();
        searchTemplate.setId(questionId);
        Collection<Question> questions = questionRepository.find(searchTemplate);
        Question result = questions.isEmpty() ? null : questions.iterator().next();
        if(result != null && loadAnswers){
            result.setAnswers(getAnswers(result));
        }
        return result;
    }

    @Override
    public Collection<Question> getQuestions(Question searchTemplate, boolean loadAnswers) {
        Collection<Question> result = questionRepository.find(searchTemplate);
        if(loadAnswers){
            result.forEach(question -> question.setAnswers(getAnswers(question)));
        }
        return result;
    }

    @Override
    public Collection<Question> getQuestionsForQuestionSet(Long questionSetId, boolean loadAnswers) {
        Collection<Question> result = questionRepository.findAllQuestionsForQuestionSet(questionSetId);
        if(loadAnswers){
            result.forEach(question -> question.setAnswers(getAnswers(question)));
        }
        return result;
    }

    private Set<Answer> getAnswers(Question question){
        Answer searchTemplate = new Answer();
        searchTemplate.setQuestionId(question.getId());
        return new HashSet<>(answerService.getAnswers(searchTemplate));
    }
}
