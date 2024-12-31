package org.tu.varna.entities;

import java.sql.Timestamp;
import java.util.Set;

public class ExamAttempt {
    private Long id;
    private String studentId;
    private Long examId;
    private Timestamp startTime;
    private Set<Long> questionIds;

    public ExamAttempt() {}

    public ExamAttempt(Long id, String studentId, Long examId, Timestamp startTime) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Set<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(Set<Long> questionIds) {
        this.questionIds = questionIds;
    }
}
