package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.User;
import org.tu.varna.services.DisciplineUserService;
import org.tu.varna.services.UserService;

import java.util.Collection;

@Path("/teacher")
public class TeacherResource {

    @Inject
    UserService userService;

    @Inject
    DisciplineUserService disciplineUserService;


    @Path("/{universityId}")
    @GET
    public User getTeacherById(@PathParam("universityId") String universityID,
                               @QueryParam("loadDisciplines") boolean loadDisciplines){
        return userService.getUser(universityID, loadDisciplines);
    }

    @GET
    public Collection<User> getTeachers(@QueryParam("email") String email, @QueryParam("loadDisciplines") boolean loadDisciplines){
        User searchTemplate = new User();
        searchTemplate.setRole("Teacher");
        searchTemplate.setEmail(email);
        return userService.getUsers(searchTemplate, loadDisciplines);
    }

    @PUT
    public String createTeacher(User requestBody){
        if(requestBody.getRole() != null && !requestBody.getRole().equals("Teacher")){
            return "Role mismatch";
        }
        userService.createUser(requestBody);
        return "User created";
    }

    @POST
    @Path("/{universityId}")
    public String updateTeacher(@PathParam("universityId") String universityID, User requestBody){
        if(!universityID.equals(requestBody.getUniversityId())){
            return "University id mismatch";
        }
        userService.updateUser(requestBody);
        return "User updated";
    }

    @DELETE
    @Path("/{universityId}")
    public String deleteTeacher(@PathParam("universityId") String universityID){
        userService.deleteUser(universityID);
        return "User deleted";
    }

    @PUT
    @Path("/{universityId}/disciplines")
    public String addAccessToDiscipline(@PathParam("universityId") String universityId, Long disciplineID){
        disciplineUserService.addUserToDiscipline(disciplineID, universityId);
        return "Discipline access added";
    }

    @DELETE
    @Path("/{universityId}/disciplines")
    public String removeAccessFromDiscipline(@PathParam("universityId") String universityId,
                                             Long disciplineId){
        disciplineUserService.removeUserFromDiscipline(disciplineId, universityId);
        return "Discipline access removed";
    }

    @GET
    @Path("/{universityId}/disciplines")
    public Collection<Discipline> getDisciplines(@PathParam("universityId") String universityId){
        return disciplineUserService.getDisciplinesForUser(universityId);
    }
}
