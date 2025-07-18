package org.tu.varna.entities;

import java.sql.Timestamp;

public class Exam {
    private Long id;
    private String name;
    private Long disciplineId;
    private Long questionSetId;
    private Integer totalPoints;
    private Integer durationMinutes;
    private Timestamp startDate;
    private Integer numberOfQuestions;

    public Exam() {}

    public Exam(Long id, String examName, Long disciplineId, Long questionSetId, int totalPoints, int durationMinutes, Timestamp startDate, Integer numberOfQuestions) {
        this.id = id;
        name = examName;
        this.disciplineId = disciplineId;
        this.questionSetId = questionSetId;
        this.totalPoints = totalPoints;
        this.durationMinutes = durationMinutes;
        this.startDate = startDate;
        this.numberOfQuestions = numberOfQuestions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamName() {
        return name;
    }

    public void setExamName(String examName) {
        name = examName;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Long getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Long questionSetId) {
        this.questionSetId = questionSetId;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
