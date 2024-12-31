package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.User;
import org.tu.varna.services.DisciplineService;
import org.tu.varna.services.DisciplineUserService;

import java.util.Collection;
import java.util.HashSet;

@Path("/disciplines")
public class DisciplineResource {

    @Inject
    DisciplineService disciplineService;

    @Inject
    DisciplineUserService disciplineUserService;

    @GET
    @RolesAllowed({"Teacher", "Admin", "Student"})
    @Path("/{disciplineId}")
    public Discipline getDiscipline(@PathParam("disciplineId") Long disciplineId,
                                    @QueryParam("loadUsers") boolean loadUsers){
        return disciplineService.getDiscipline(disciplineId, loadUsers);
    }

    @GET
    @PermitAll
    //@RolesAllowed({"Teacher", "Admin", "Student"})
    public Collection<Discipline> getDisciplines(
            @QueryParam("name") String name,
            @QueryParam("userId") String userId,
            @QueryParam("loadUsers") boolean loadUsers
    ){
        Discipline searchTemplate = new Discipline();
        searchTemplate.setDisciplineName(name);
        if(userId != null){
            searchTemplate.setUsers(new HashSet<>());
            User user = new User();
            user.setUniversityId(userId);
            searchTemplate.getUsers().add(user);
        }
        return disciplineService.getDisciplines(searchTemplate, loadUsers);
    }

    @PUT
    @RolesAllowed({"Teacher", "Admin"})
    public String createDiscipline(Discipline requestBody){
        disciplineService.createDiscipline(requestBody);
        return "Discipline created";
    }

    @POST
    @RolesAllowed({"Teacher", "Admin"})
    @Path("/{disciplineId}")
    public String updateDiscipline(@PathParam("disciplineId") Long disciplineId, Discipline requestBody){
        if(!disciplineId.equals(requestBody.getDisciplineId())){
            return "Discipline id mismatch";
        }
        disciplineService.updateDiscipline(requestBody);
        return "Disciplines updated";
    }

    @DELETE
    @RolesAllowed({"Teacher", "Admin"})
    @Path("/{disciplineId}")
    public String deleteDiscipline(@PathParam("disciplineId") Long disciplineId){
        disciplineService.deleteDiscipline(disciplineId);
        return "Discipline deleted";
    }

    @PUT
    @Path("/{disciplineId}/users")
    public String addUserToDiscipline(@PathParam("disciplineId") Long disciplineId,
                               String userId){
        disciplineUserService.addUserToDiscipline(disciplineId, userId);
        return "User added to Discipline " + disciplineId;
    }

    @DELETE
    @Path("/{disciplineId}/users")
    public String removeUserFromDiscipline(@PathParam("disciplineId") Long disciplineId,
                                           String userId){
        disciplineUserService.removeUserFromDiscipline(disciplineId, userId);
        return "User removed from Discipline " + disciplineId;
    }

    @GET
    @Path("/{disciplineId}/users")
    public Collection<User> getUsersForDiscipline(@PathParam("disciplineId") Long disciplineId){
        return disciplineUserService.getUsersForDiscipline(disciplineId);
    }

}
