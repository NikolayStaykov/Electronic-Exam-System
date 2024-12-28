package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.QuestionSet;
import org.tu.varna.services.QuestionSetQuestionService;
import org.tu.varna.services.QuestionSetService;

import java.util.Collection;

@Path("questionSet")
public class QuestionSetResource {

    @Inject
    QuestionSetService questionSetService;

    @Inject
    QuestionSetQuestionService questionSetQuestionService;

    @Path("/{questionSetId}")
    @GET
    public QuestionSet getQuestionSet(@PathParam("questionSetId") Long questionSetId,
                                      @QueryParam("loadChildQuestionSets") boolean loadChildQuestionSets,
                                      @QueryParam("loadQuestions") boolean loadQuestions) {
        return questionSetService.getQuestionSet(questionSetId,loadChildQuestionSets, loadQuestions);
    }

    @GET
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

    @PUT
    @Path("/{QuestionsetId}/questions")
    public String addQuestionToQuestionSet(@PathParam("QuestionsetId") Long questionSetId,
                                           Long questionId) {
        questionSetQuestionService.addQuestion(questionSetId,questionId);
        return "Question added";
    }

    @DELETE
    @Path("/{QuestionsetId}/questions")
    public String removeQuestionFromQuestionSet(@PathParam("QuestionsetId") Long questionSetId,
                                                Long questionId) {
        questionSetQuestionService.removeQuestion(questionSetId,questionId);
        return "Question removed";
    }

}
