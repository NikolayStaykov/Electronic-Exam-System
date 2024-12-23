package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.User;
import org.tu.varna.services.UserService;

import java.util.Collection;
@Path( "/student")
public class StudentResource {

    @Inject
    UserService userService;

    @GET
    @Path("/{UniversityID}")
    public User getStudent(@PathParam("UniversityID") String universityID,
                           @QueryParam("loadDisciplines") boolean loadDisciplines){
        return userService.getUser(universityID, loadDisciplines);
    }

    @GET
    public Collection<User> getStudents(@QueryParam("email") String email, @QueryParam("loadDisciplines") boolean loadDisciplines){
        User searchTemplate = new User();
        searchTemplate.setRole("Student");
        searchTemplate.setEmail(email);
        return userService.getUsers(searchTemplate, loadDisciplines);
    }

    @POST
    @Path("/{UniversityID}")
    public String updateStudents(@PathParam("UniversityID") String universityID, User requestBody){
        if(!universityID.equals(requestBody.getUniversityId())){
            return "University id mismatch";
        }
        userService.updateUser(requestBody);
        return "User updated";
    }

    @PUT
    public String createStudent(User requestBody){
        if(requestBody.getRole() != null && !requestBody.getRole().equals("Student")){
            return "Role mismatch";
        }
        userService.createUser(requestBody);
        return "User created";
    }

    @DELETE
    @Path("/{UniversityID}")
    public String deleteStudent(@PathParam("UniversityID") String universityID){
        userService.deleteUser(universityID);
        return "User deleted";
    }
}
