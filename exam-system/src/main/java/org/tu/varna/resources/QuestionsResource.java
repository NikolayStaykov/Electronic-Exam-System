package org.tu.varna.resources;

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
    public String createQuestion(Question requestBody) {
        questionService.createQuestion(requestBody);
        return "Question created";
    }

    @POST
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
    @Path("/{questionId}")
    public Question getQuestion(@PathParam("questionId") Long questionId) {
        return questionService.getQuestion(questionId);
    }

    @GET
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
    @Path("/{questionId}")
    public String deleteQuestion(@PathParam("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
        return "Question deleted";
    }
}
