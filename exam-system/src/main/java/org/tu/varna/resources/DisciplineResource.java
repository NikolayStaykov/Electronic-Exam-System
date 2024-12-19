package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.Discipline;
import org.tu.varna.services.DisciplineService;

import java.util.Collection;

@Path("/disciplines")
public class DisciplineResource {

    @Inject
    DisciplineService disciplineService;

    @GET
    @Path("/{disciplineId}")
    public Discipline getDiscipline(@PathParam("disciplineId") Long disciplineId){
        return disciplineService.getDiscipline(disciplineId);
    }

    @GET
    public Collection<Discipline> getDisciplines(
            @QueryParam("name") String name
    ){
        Discipline searchTemplate = new Discipline();
        searchTemplate.setDisciplineName(name);
        return disciplineService.getDisciplines(searchTemplate);
    }

    @PUT
    public String createDiscipline(Discipline requestBody){
        disciplineService.createDiscipline(requestBody);
        return "Discipline created";
    }

    @POST
    @Path("/{disciplineId}")
    public String updateDiscipline(@PathParam("disciplineId") Long disciplineId, Discipline requestBody){
        if(disciplineId != requestBody.getDisciplineId()){
            return "Discipline id mismatch";
        }
        disciplineService.updateDiscipline(requestBody);
        return "Disciplines updated";
    }

    @DELETE
    @Path("/{disciplineId}")
    public String deleteDiscipline(@PathParam("disciplineId") Long disciplineId){
        disciplineService.deleteDiscipline(disciplineId);
        return "Discipline deleted";
    }
}
