package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.QuestionSet;
import org.tu.varna.services.QuestionSetService;

import java.util.Collection;

@Path("questionSet")
public class QuestionSetResource {

    @Inject
    QuestionSetService questionSetService;

    @Path("/{questionSetId}")
    @GET
    public QuestionSet getQuestionSet(@PathParam("questionSetId") Long questionSetId,
                                      @QueryParam("loadChildQuestionSets") boolean loadChildQuestionSets,
                                      @QueryParam("loadQuestions") boolean loadQuestions) {
        return questionSetService.getQuestionSet(questionSetId,loadChildQuestionSets, loadQuestions);
    }

    @GET
    public Collection<QuestionSet> getQuestionSets(QuestionSet requestBody,
                                                   @QueryParam("loadChildQuestionSets") boolean loadChildQuestionSets,
                                                   @QueryParam("loadQuestions") boolean loadQuestions) {
        return questionSetService.getQuestionSets(requestBody,loadChildQuestionSets,loadQuestions);
    }

    @PUT
    public String createRQuestionSet(QuestionSet requestBody) {
        questionSetService.save(requestBody);
        return "QuestionSet created";
    }

    @POST
    @Path("{questionSetId}")
    public String updateRQuestionSet(@PathParam("questionSetId") Long questionSetId
            ,QuestionSet requestBody) {
        if(!questionSetId.equals(requestBody.getId())) {
            return "QuestionSet id mismatch";
        }
        questionSetService.update(requestBody);
        return "QuestionSet updated";
    }
}
