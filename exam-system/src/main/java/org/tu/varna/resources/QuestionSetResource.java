package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.common.AddQuestionRequestBody;
import org.tu.varna.entities.QuestionSet;
import org.tu.varna.services.QuestionSetQuestionService;
import org.tu.varna.services.QuestionSetService;

import java.util.Collection;

@Path("questionSets")
public class QuestionSetResource {

    @Inject
    QuestionSetService questionSetService;

    @Inject
    QuestionSetQuestionService questionSetQuestionService;

    @Path("/{questionSetId}")
    @PermitAll
    @GET
    public QuestionSet getQuestionSet(@PathParam("questionSetId") Long questionSetId,
                                      @QueryParam("loadChildQuestionSets") boolean loadChildQuestionSets,
                                      @QueryParam("loadQuestions") boolean loadQuestions) {
        return questionSetService.getQuestionSet(questionSetId,loadChildQuestionSets, loadQuestions);
    }

    @GET
    @PermitAll
    public Collection<QuestionSet> getQuestionSets(@QueryParam("loadChildQuestionSets") boolean loadChildQuestionSets,
                                                   @QueryParam("loadQuestions") boolean loadQuestions,
                                                   @QueryParam("id") Long questionSetId,
                                                   @QueryParam("name") String name,
                                                   @QueryParam("info") String info,
                                                   @QueryParam("disciplineId") Long disciplineId,
                                                   @QueryParam("parentQUestionSetId") Long parentQuestionSetId) {
        QuestionSet searchTemplate = new QuestionSet();
        searchTemplate.setId(questionSetId);
        searchTemplate.setName(name);
        searchTemplate.setInfo(info);
        searchTemplate.setDisciplineId(disciplineId);
        searchTemplate.setParentQuestionSetId(parentQuestionSetId);
        return questionSetService.getQuestionSets(searchTemplate,loadChildQuestionSets,loadQuestions);
    }

    @PUT
    @PermitAll
    public String createRQuestionSet(QuestionSet requestBody) {
        questionSetService.save(requestBody);
        return "QuestionSet created";
    }

    @POST
    @PermitAll
    @Path("{questionSetId}")
    public String updateRQuestionSet(@PathParam("questionSetId") Long questionSetId
            ,QuestionSet requestBody) {
        if(!questionSetId.equals(requestBody.getId())) {
            return "QuestionSet id mismatch";
        }
        questionSetService.update(requestBody);
        return "QuestionSet updated";
    }

    @PUT
    @PermitAll
    @Path("/{QuestionsetId}/questions")
    public String addQuestionToQuestionSet(@PathParam("QuestionsetId") Long questionSetId,
                                           AddQuestionRequestBody body) {
        questionSetQuestionService.addQuestion(questionSetId, body.getQuestionId());
        return "Question added";
    }

    @DELETE
    @PermitAll
    @Path("/{QuestionsetId}/questions")
    public String removeQuestionFromQuestionSet(@PathParam("QuestionsetId") Long questionSetId,
                                                AddQuestionRequestBody body) {
        questionSetQuestionService.removeQuestion(questionSetId, body.getQuestionId());
        return "Question removed";
    }

}
