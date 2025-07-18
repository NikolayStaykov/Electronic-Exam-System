package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.inject.Decorated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.common.AddUserRequestBody;
import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.QuestionSet;
import org.tu.varna.entities.User;
import org.tu.varna.services.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/disciplines")
public class DisciplineResource {

    @Inject
    DisciplineService disciplineService;

    @Inject
    QuestionSetService questionSetService;

    @Inject
    QuestionSetQuestionService questionSetQuestionService;

    @Inject
    DisciplineUserService disciplineUserService;

    @GET
    //@RolesAllowed({"Teacher", "Admin", "Student"})
    @PermitAll
    @Path("/{disciplineId}")
    public Discipline getDiscipline(@PathParam("disciplineId") Long disciplineId,
                                    @QueryParam("loadUsers") boolean loadUsers){
        return disciplineService.getDiscipline(disciplineId, loadUsers);
    }

    @GET
    //@RolesAllowed({"Teacher", "Admin", "Student"})
    @PermitAll
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
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    public String createDiscipline(Discipline requestBody){
        disciplineService.createDiscipline(requestBody);
        return "Discipline created";
    }

    @POST
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{disciplineId}")
    public String updateDiscipline(@PathParam("disciplineId") Long disciplineId, Discipline requestBody){
        if(!disciplineId.equals(requestBody.getDisciplineId())){
            return "Discipline id mismatch";
        }
        disciplineService.updateDiscipline(requestBody);
        return "Disciplines updated";
    }

    @DELETE
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{disciplineId}")
    public String deleteDiscipline(@PathParam("disciplineId") Long disciplineId){
        disciplineService.deleteDiscipline(disciplineId);
        return "Discipline deleted";
    }

    @PUT
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{disciplineId}/users")
    public String addUserToDiscipline(@PathParam("disciplineId") Long disciplineId,
                                      AddUserRequestBody body){
        disciplineUserService.addUserToDiscipline(disciplineId, body.getUserId());
        return "User added to Discipline " + disciplineId;
    }

    @DELETE
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{disciplineId}/users")
    public String removeUserFromDiscipline(@PathParam("disciplineId") Long disciplineId,
                                           AddUserRequestBody body){
        disciplineUserService.removeUserFromDiscipline(disciplineId, body.getUserId());
        return "User removed from Discipline " + disciplineId;
    }

    @GET
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{disciplineId}/teachers")
    public Collection<User> getTeachersForDiscipline(@PathParam("disciplineId") Long disciplineId){
        Collection<User> res = disciplineUserService.getUsersForDiscipline(disciplineId);
        return res.stream().filter((u) -> {
            return u.getRole().equals("Teacher");
        }).collect(Collectors.toSet());
    }

    @GET
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{disciplineId}/students")
    public Collection<User> getStudentsForDiscipline(@PathParam("disciplineId") Long disciplineId){
        Collection<User> res = disciplineUserService.getUsersForDiscipline(disciplineId);
        return res.stream().filter((u) -> {
            return u.getRole().equals("Student");
        }).collect(Collectors.toSet());
    }

}
