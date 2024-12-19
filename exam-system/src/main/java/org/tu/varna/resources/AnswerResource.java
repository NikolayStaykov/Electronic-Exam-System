package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.Answer;
import org.tu.varna.services.AnswerService;

import java.util.Collection;

@Path( "/answers")
public class AnswerResource {
    @Inject
    AnswerService answerService;

    @PUT
    public void createAnswer(Answer requestBody) {
        answerService.createAnswer(requestBody);
    }

    @POST
    @Path("/{answerId}")
    public String updateAnswer(@PathParam("answerId") Long answerId, Answer requestBody){
        if(answerId != requestBody.getId()){
            return "Answer id mismatch";
        }
        answerService.updateAnswer(requestBody);
        return "Answers updated";
    }

    @DELETE
    @Path("/{answerId}")
    public String deleteAnswer(@PathParam("answerId") Long answerId){
        answerService.deleteAnswer(answerId);
        return "Answer deleted";
    }

    @GET
    @Path("/{answerId}")
    public Answer getAnswer(@PathParam("answerId") Long answerId){
        return answerService.getAnswer(answerId);
    }

    @GET
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
