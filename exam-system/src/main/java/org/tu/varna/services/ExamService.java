package org.tu.varna.services;

import org.tu.varna.entities.Exam;

import java.util.Collection;

public interface ExamService {
    void addExam(Exam exam);
    void deleteExam(Long examId);
    void updateExam(Exam exam);
    Collection<Exam> getExams(Exam searchTemplate);
    Exam getExam(Long examId);
}
