package org.tu.varna.entities;

import java.util.Set;

public class QuestionSet {
    private Long id;
    private String name;
    private String info;
    private Long disciplineId;
    private Long parentQuestionSetId;
    private Set<Question> questions;
    private Set<QuestionSet> childQuestionSets;

    public QuestionSet() {}

    public QuestionSet(Long id, String name, String info, Long disciplineId, Long parentQuestionSetId) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.disciplineId = disciplineId;
        this.parentQuestionSetId = parentQuestionSetId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Long getParentQuestionSetId() {
        return parentQuestionSetId;
    }

    public void setParentQuestionSetId(Long parentQuestionSetId) {
        this.parentQuestionSetId = parentQuestionSetId;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<QuestionSet> getChildQuestionSets() {
        return childQuestionSets;
    }

    public void setChildQuestionSets(Set<QuestionSet> childQuestionSets) {
        this.childQuestionSets = childQuestionSets;
    }
}
