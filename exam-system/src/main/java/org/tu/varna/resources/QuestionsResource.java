package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.Question;
import org.tu.varna.objects.QuestionType;
import org.tu.varna.services.QuestionService;

import java.util.Collection;

@Path("/questions")
public class QuestionsResource {

    @Inject
    QuestionService questionService;

    @PUT
    @RolesAllowed({"Teacher", "Admin"})
    public String createQuestion(Question requestBody) {
        questionService.createQuestion(requestBody);
        return "Question created";
    }

    @POST
    @RolesAllowed({"Teacher", "Admin"})
    @Path("/{questionId}")
    public String updateQuestion(@PathParam("questionId") Long questionId, Question requestBody) {
        if(questionId != requestBody.getId())
        {
            return "Question id mismatch";
        }
        questionService.updateQuestion(requestBody);
        return "Questions updated";
    }

    @GET
    @RolesAllowed({"Teacher", "Admin", "Student"})
    @Path("/{questionId}")
    public Question getQuestion(@PathParam("questionId") Long questionId) {
        return questionService.getQuestion(questionId);
    }

    @GET
    @RolesAllowed({"Teacher", "Admin", "Student"})
    public Collection<Question> getQuestions(
            @QueryParam("questionText") String questionText,
            @QueryParam("questionType") String questionType,
            @QueryParam("diciplineId") Long disciplineId) {
        Question searchTemplate = new Question();
        searchTemplate.setQuestionText(questionText);
        searchTemplate.setQuestionType(questionType != null ? QuestionType.valueOf(questionType) : null);
        Discipline dicipline = new Discipline();
        dicipline.setDisciplineId(disciplineId);
        searchTemplate.setDiscipline(disciplineId != null ? dicipline : null);
        return questionService.getQuestions(searchTemplate);
    }

    @DELETE
    @RolesAllowed({"Teacher", "Admin"})
    @Path("/{questionId}")
    public String deleteQuestion(@PathParam("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
        return "Question deleted";
    }
}
