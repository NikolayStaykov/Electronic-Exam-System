package org.tu.varna.dto;

import org.tu.varna.entities.Answer;
import org.tu.varna.entities.Exam;
import org.tu.varna.entities.ExamAttempt;
import org.tu.varna.entities.Question;

import java.util.Map;
import java.util.Set;

public class ExamResultDto {
    private ExamAttempt examAttempt;
    private Exam exam;
    private Map<Question, Set<Answer>> questions;

    public ExamAttempt getExamAttempt() {
        return examAttempt;
    }

    public void setExamAttempt(ExamAttempt examAttempt) {
        this.examAttempt = examAttempt;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Map<Question, Set<Answer>> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<Question, Set<Answer>> questions) {
        this.questions = questions;
    }
}
