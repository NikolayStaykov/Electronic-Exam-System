package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.User;
import org.tu.varna.services.UserService;

import java.util.Collection;

@Path("/teacher")
public class TeacherResource {

    @Inject
    UserService userService;


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
}
