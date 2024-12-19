package org.tu.varna.objects;

import java.beans.JavaBean;
import java.util.Set;

@JavaBean
public class Question {
    private Long Id;
    private String questionText;
    private QuestionType questionType;
    private Discipline discipline;
    private Set<Answer> answers;

    public Question(Long id, String questionText, QuestionType questionType) {
        Id = id;
        this.questionText = questionText;
        this.questionType = questionType;
    }

    public Question() {
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Long getId() {
        return Id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
