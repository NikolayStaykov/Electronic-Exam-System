package org.tu.varna.services;

import org.tu.varna.dto.ExamResultDto;
import org.tu.varna.entities.Exam;
import org.tu.varna.entities.ExamAttempt;
import org.tu.varna.entities.Question;

import java.util.Collection;

public interface ExamAttemptService {
    ExamAttempt initateExamAttempt(Exam exam, String studentId);
    String answerQuestion(Question question, Long examAttemptId);
    Collection<ExamResultDto> getExamResults(String userId, Long examId);
}
