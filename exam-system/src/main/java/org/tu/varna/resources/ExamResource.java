package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.dto.ExamResultDto;
import org.tu.varna.entities.Exam;
import org.tu.varna.entities.ExamAttempt;
import org.tu.varna.entities.Question;
import org.tu.varna.entities.User;
import org.tu.varna.services.ExamAttemptService;
import org.tu.varna.services.ExamService;
import org.tu.varna.services.ExamUserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

@Path("exam")
public class ExamResource {
    @Inject
    ExamService examService;

    @Inject
    ExamUserService examUserService;

    @Inject
    ExamAttemptService examAttemptService;

    @GET
    @Path("/{examId}")
    public Exam getExam(@PathParam("examId") Long examId) {
        return examService.getExam(examId);
    }

    @GET
    public Collection<Exam> getExams(@QueryParam("Id") Long examId,
                                     @QueryParam("ExamName") String examName,
                                     @QueryParam("questionSetId") Long questionSetId,
                                     @QueryParam("disciplineId") Long disciplineId,
                                     @QueryParam("totalPoints") Integer totalPoints,
                                     @QueryParam("duration") Integer duration,
                                     @QueryParam("startDate")Timestamp startDate) {
        Exam searchTemplate = new Exam();
        searchTemplate.setId(examId);
        searchTemplate.setExamName(examName);
        searchTemplate.setQuestionSetId(questionSetId);
        searchTemplate.setDisciplineId(disciplineId);
        searchTemplate.setTotalPoints(totalPoints);
        searchTemplate.setDurationMinutes(duration);
        searchTemplate.setStartDate(startDate);
        return examService.getExams(searchTemplate);
    }

    @PUT
    public String createExam(Exam exam) {
        examService.addExam(exam);
        return "Exam created";
    }

    @DELETE
    @Path("/{examId}")
    public String deleteExam(@PathParam("examId") Long examId) {
        examService.deleteExam(examId);
        return "Exam deleted";
    }

    @POST
    @Path("/{examId}")
    public String updateExam(@PathParam("examId") Long examId, Exam exam) {
        if(examId.equals(exam.getId())){
            return "Exam id Mismatch";
        }
        examService.updateExam(exam);
        return "Exam updated";
    }

    @PUT
    @Path("/{examId}/users")
    public String addAccessToExam(@PathParam("examId") Long examID, String UniversityID){
        examUserService.addUserToExam(UniversityID,examID);
        return "Exam access added";
    }

    @DELETE
    @Path("/{examId}/users")
    public String removeAccessToExam(@PathParam("examId") Long examID, String UniversityID){
        examUserService.removeUserFromExam(UniversityID,examID);
        return "Exam access added";
    }

    @GET
    @Path("/{examId}/users")
    public Collection<User> getExams(@PathParam("examId") Long examID){
        return examUserService.geeExamUsers(examID);
    }

    @GET
    @Path("/{examId}/attempts")
    public Collection<ExamResultDto> getExamAttempts(@PathParam("examId") Long examID,
                                                     @QueryParam("userId") String userId){
        return examAttemptService.getExamResults(userId,examID);
    }

    @PUT
    @Path("/{examId}/attempts")
    public ExamAttempt initiateExamAttempt(@PathParam("examId") Long examID,
                                           @QueryParam("studentId") String studentID){
        Exam exam = examService.getExam(examID);
        long diff = Timestamp.from(Instant.now()).getTime() - exam.getStartDate().getTime();
        if(diff < 0 || diff > 600000){
            throw new RuntimeException("Unable to start exam attempt at this time.");
        }
        return examAttemptService.initateExamAttempt(exam, studentID);
    }

    @POST
    @Path("/{examId}/attempts/{attemptId}")
    public String answerQuestion(
            @PathParam("attemptId") Long attemptId,
            Question requestBody
    ){
        return examAttemptService.answerQuestion(requestBody,attemptId);
    }

}
