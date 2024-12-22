package org.tu.varna.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.User;
import org.tu.varna.services.DisciplineService;
import org.tu.varna.services.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Path( "/student")
public class StudentResource {

    @Inject
    UserService userService;

    @Inject
    DisciplineService disciplineService;

    @GET
    @Path("/{UniversityID}")
    public User getStudent(@PathParam("UniversityID") String universityID,
                           @QueryParam("loadDisciplines") boolean loadDisciplines){
        User result = userService.getUser(universityID);
        if(loadDisciplines){
            result.setDisciplines(getDisciplines(result));
        }
        return result;
    }

    @GET
    public Collection<User> getStudents(@QueryParam("email") String email, @QueryParam("loadDisciplines") boolean loadDisciplines){
        User searchTemplate = new User();
        searchTemplate.setRole("Student");
        searchTemplate.setEmail(email);
        Collection<User> result = userService.getUsers(searchTemplate);
        if(loadDisciplines){
            result.forEach(user -> user.setDisciplines(getDisciplines(user)));
        }
        return result;
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

    private Set<Discipline> getDisciplines(User user){
        Discipline searchTemplate = new Discipline();
        searchTemplate.setUsers(Set.of(user));
        return new HashSet<>(disciplineService.getDisciplines(searchTemplate));
    }

}
