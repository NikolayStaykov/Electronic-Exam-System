package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Exam;
import org.tu.varna.repositories.ExamRepository;
import org.tu.varna.services.ExamService;

import java.util.Collection;

@ApplicationScoped
public class BasicExamService implements ExamService {

    @Inject
    ExamRepository examRepository;
    @Override
    public void addExam(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    public void deleteExam(Long examId) {
        Exam toDelete = new Exam();
        toDelete.setId(examId);
        examRepository.delete(toDelete);
    }

    @Override
    public void updateExam(Exam exam) {
        examRepository.update(exam);
    }

    @Override
    public Collection<Exam> getExams(Exam searchTemplate) {
        return examRepository.find(searchTemplate);
    }

    @Override
    public Exam getExam(Long examId) {
        Exam searchTemplate = new Exam();
        searchTemplate.setId(examId);
        Collection<Exam> exams = examRepository.find(searchTemplate);
        return exams.isEmpty() ? null : exams.iterator().next();
    }
}
