package org.tu.varna.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.entities.User;
import org.tu.varna.services.DisciplineUserService;
import org.tu.varna.services.UserService;

import java.util.Collection;
import java.util.List;

@Path("/teachers")
public class TeacherResource {

    @Inject
    UserService userService;

    @Inject
    DisciplineUserService disciplineUserService;


    @Path("/{universityId}")
    //@RolesAllowed({"Teacher", "Admin"})
    @PermitAll
    @GET
    public User getTeacherById(@PathParam("universityId") String universityID,
                               @QueryParam("loadDisciplines") boolean loadDisciplines){
        return userService.getUser(universityID, loadDisciplines);
    }

    @GET
    //@RolesAllowed({"Admin"})
    @PermitAll
    public Collection<User> getTeachers(@QueryParam("email") String email, @QueryParam("loadDisciplines") boolean loadDisciplines){
        User searchTemplate = new User();
        searchTemplate.setRole("Teacher");
        searchTemplate.setEmail(email);
        return userService.getUsers(searchTemplate, loadDisciplines);
    }

    @PUT
    //@RolesAllowed({"Admin"})
    @PermitAll
    public String createTeacher(User requestBody){
        if(requestBody.getRole() != null && !requestBody.getRole().equals("Teacher")){
            return "Role mismatch";
        }
        userService.createUser(requestBody);
        return "User created";
    }

    @POST
   // @RolesAllowed({"Admin"})
    @PermitAll
    @Path("/{universityId}")
    public String updateTeacher(@PathParam("universityId") String universityID, User requestBody){
        if(!universityID.equals(requestBody.getUniversityId())){
            return "University id mismatch";
        }
        userService.updateUser(requestBody);
        return "User updated";
    }

    @DELETE
    //@RolesAllowed({"Admin"})
    @PermitAll
    @Path("/{universityId}")
    public String deleteTeacher(@PathParam("universityId") String universityID){
        userService.deleteUser(universityID);
        return "User deleted";
    }

    @POST
    //@RolesAllowed({"Admin"})
    @PermitAll
    @Path("/import")
    public String importTeacher(List<User> requestBody){
        for(User user : requestBody){
            user.setRole("Teacher");
            userService.createUser(user);
        }
        return "Teachers imported";
    }
}
