package org.tu.varna.services;

import org.tu.varna.entities.Exam;
import org.tu.varna.entities.User;

import java.util.Collection;

public interface ExamUserService {
    void addUserToExam(String userId, Long examId);
    void removeUserFromExam(String userId, Long examId);
    Collection<Exam> getUserExams(String userId);
    Collection<User> getExamUsers(Long examId);
}
