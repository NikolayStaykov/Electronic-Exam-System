package org.tu.varna.services;

import org.tu.varna.objects.Exam;
import org.tu.varna.objects.User;

import java.util.Collection;

public interface ExamUserService {
    void addUserToExam(String userId, Long examId);
    void removeUserFromExam(String userId, Long examId);
    Collection<Exam> getUserExams(String userId);
    Collection<User> geeExamUsers(Long examId);
}
