package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.entities.Answer;
import org.tu.varna.services.AnswerService;

import java.util.Collection;
import java.util.Objects;

@Path( "/answers")
public class AnswerResource {

    @Inject
    AnswerService answerService;

    @PUT
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    public void createAnswer(Answer requestBody) {
        answerService.createAnswer(requestBody);
    }

    @POST
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{answerId}")
    public String updateAnswer(@PathParam("answerId") Long answerId, Answer requestBody){
        if(!Objects.equals(answerId, requestBody.getId())){
            return "Answer id mismatch";
        }
        answerService.updateAnswer(requestBody);
        return "Answers updated";
    }

    @DELETE
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{answerId}")
    public String deleteAnswer(@PathParam("answerId") Long answerId){
        answerService.deleteAnswer(answerId);
        return "Answer deleted";
    }

    @GET
    //@RolesAllowed({"Teacher", "Admin", "Student"})
    @PermitAll
    @Path("/{answerId}")
    public Answer getAnswer(@PathParam("answerId") Long answerId){
        return answerService.getAnswer(answerId);
    }

    @GET
    @RolesAllowed({"Teacher", "Admin", "Student"})
    public Collection<Answer> getAnswer(@QueryParam("questionId") Long questionId,
                                @QueryParam("answerOrder") Integer answerOrder,
                                @QueryParam("answerText") String answerText,
                                @QueryParam("fraction") Double fraction){
        Answer searchTemplate = new Answer();
        searchTemplate.setQuestionId(questionId);
        searchTemplate.setAnswerOrder(answerOrder);
        searchTemplate.setAnswerText(answerText);
        searchTemplate.setFraction(fraction);
        return answerService.getAnswers(searchTemplate);
    }
}
