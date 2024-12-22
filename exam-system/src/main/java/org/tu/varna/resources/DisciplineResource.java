package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.User;
import org.tu.varna.services.DisciplineService;
import org.tu.varna.services.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Path("/disciplines")
public class DisciplineResource {

    @Inject
    DisciplineService disciplineService;

    @Inject
    UserService userService;

    @GET
    @RolesAllowed({"Teacher", "Admin", "Student"})
    @Path("/{disciplineId}")
    public Discipline getDiscipline(@PathParam("disciplineId") Long disciplineId,
                                    @QueryParam("loadUsers") boolean loadUsers){
        Discipline result = disciplineService.getDiscipline(disciplineId);
        if(loadUsers){
            result.setUsers(getUsers(result));
        }
        return result;
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
        Collection<Discipline> result = disciplineService.getDisciplines(searchTemplate);
        if(loadUsers){
            result.forEach(discipline -> discipline.setUsers(getUsers(discipline)));
        }
        return result;
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

    private Set<User> getUsers(Discipline discipline){
        User searchTemplate = new User();
        searchTemplate.setDisciplines(Set.of(discipline));
        return new HashSet<>(userService.getUsers(searchTemplate));
    }
}
