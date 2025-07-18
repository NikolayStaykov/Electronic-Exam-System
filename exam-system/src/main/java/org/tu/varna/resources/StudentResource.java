package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.entities.User;
import org.tu.varna.repositories.UserRepository;
import org.tu.varna.services.UserService;

import java.util.Collection;
import java.util.List;

@Path( "/students")
public class StudentResource {

    @Inject
    UserService userService;

    @GET
    //@RolesAllowed({"Teacher", "Admin", "Student"})
    @PermitAll
    @Path("/{UniversityID}")
    public User getStudent(@PathParam("UniversityID") String universityID,
                           @QueryParam("loadDisciplines") boolean loadDisciplines){
        return userService.getUser(universityID, loadDisciplines);
    }

    @GET
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    public Collection<User> getStudents(@QueryParam("email") String email, @QueryParam("loadDisciplines") boolean loadDisciplines){
        User searchTemplate = new User();
        searchTemplate.setRole("Student");
        searchTemplate.setEmail(email);
        return userService.getUsers(searchTemplate, loadDisciplines);
    }

    @POST
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{UniversityID}")
    public String updateStudents(@PathParam("UniversityID") String universityID, User requestBody){
        if(!universityID.equals(requestBody.getUniversityId())){
            return "University id mismatch";
        }
        userService.updateUser(requestBody);
        return "User updated";
    }

    @PUT
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    public String createStudent(User requestBody){
        if(requestBody.getRole() != null && !requestBody.getRole().equals("Student")){
            return "Role mismatch";
        }
        userService.createUser(requestBody);
        return "User created";
    }

    @DELETE
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @Path("/{UniversityID}")
    public String deleteStudent(@PathParam("UniversityID") String universityID){
        userService.deleteUser(universityID);
        return "User deleted";
    }

    @POST
    //@RolesAllowed({"Admin"})
    @PermitAll
    @Path("/import")
    public String importStudents(List<User> requestBody){
        for(User user : requestBody){
            user.setRole("Student");
            userService.createUser(user);
        }
        return "Students imported";
    }
}
