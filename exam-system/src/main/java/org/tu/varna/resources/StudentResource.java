package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.dto.ExamResultDto;
import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.Exam;
import org.tu.varna.entities.User;
import org.tu.varna.services.DisciplineUserService;
import org.tu.varna.services.ExamAttemptService;
import org.tu.varna.services.ExamUserService;
import org.tu.varna.services.UserService;

import java.util.Collection;
@Path( "/student")
public class StudentResource {

    @Inject
    UserService userService;

    @Inject
    DisciplineUserService disciplineUserService;

    @Inject
    ExamUserService examUserService;

    @Inject
    ExamAttemptService examAttemptService;

    @GET
    @Path("/{UniversityID}")
    public User getStudent(@PathParam("UniversityID") String universityID,
                           @QueryParam("loadDisciplines") boolean loadDisciplines){
        return userService.getUser(universityID, loadDisciplines);
    }

    @GET
    public Collection<User> getStudents(@QueryParam("email") String email, @QueryParam("loadDisciplines") boolean loadDisciplines){
        User searchTemplate = new User();
        searchTemplate.setRole("Student");
        searchTemplate.setEmail(email);
        return userService.getUsers(searchTemplate, loadDisciplines);
    }

    @POST
    @Path("/{UniversityID}")
    public String updateStudents(@PathParam("UniversityID") String universityID, User requestBody){
        if(!universityID.equals(requestBody.getUniversityId())){
            return "University id mismatch";
        }
        userService.updateUser(requestBody);
        return "User updated";
    }

    @PUT
    public String createStudent(User requestBody){
        if(requestBody.getRole() != null && !requestBody.getRole().equals("Student")){
            return "Role mismatch";
        }
        userService.createUser(requestBody);
        return "User created";
    }

    @DELETE
    @Path("/{UniversityID}")
    public String deleteStudent(@PathParam("UniversityID") String universityID){
        userService.deleteUser(universityID);
        return "User deleted";
    }

    @PUT
    @Path("/{UniversityID}/disciplines")
    public String addAccessToDiscipline(@PathParam("UniversityID") String UniversityID, Long disciplineID){
        disciplineUserService.addUserToDiscipline(disciplineID, UniversityID);
        return "Discipline access added";
    }

    @DELETE
    @Path("/{UniversityID}/disciplines")
    public String removeAccessFromDiscipline(@PathParam("UniversityID") String UniversityID,
                                             Long disciplineId){
        disciplineUserService.removeUserFromDiscipline(disciplineId, UniversityID);
        return "Discipline access removed";
    }

    @GET
    @Path("/{UniversityID}/disciplines")
    public Collection<Discipline> getDisciplines(@PathParam("UniversityID") String UniversityID){
        return disciplineUserService.getDisciplinesForUser(UniversityID);
    }

    @PUT
    @Path("/{UniversityID}/exams")
    public String addAccessToExam(@PathParam("UniversityID") String UniversityID, Long examID){
        examUserService.addUserToExam(UniversityID,examID);
        return "Exam access added";
    }

    @DELETE
    @Path("/{UniversityID}/exams")
    public String removeAccessToExam(@PathParam("UniversityID") String UniversityID, Long examID){
        examUserService.removeUserFromExam(UniversityID,examID);
        return "Exam access added";
    }

    @GET
    @Path("/{UniversityID}/exams")
    public Collection<Exam> getExams(@PathParam("UniversityID") String UniversityID){
        return examUserService.getUserExams(UniversityID);
    }

    @GET
    @Path("/{UniversityID}/examAttempts")
    public Collection<ExamResultDto> getExamAttempts(@PathParam("UniversityID") String UniversityID,
                                                     @QueryParam("ExamId") Long examId){
        return examAttemptService.getExamResults(UniversityID, examId);
    }
}
