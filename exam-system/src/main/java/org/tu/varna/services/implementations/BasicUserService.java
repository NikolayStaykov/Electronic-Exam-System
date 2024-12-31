package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.User;
import org.tu.varna.repositories.UserRepository;
import org.tu.varna.services.DisciplineService;
import org.tu.varna.services.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class BasicUserService implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    DisciplineService disciplineService;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String universityID) {
        User toDelete = new User();
        toDelete.setUniversityId(universityID);
        userRepository.delete(toDelete);
    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    @Override
    public User getUser(String universityID,boolean loadDisciplines) {
        User searchTemplate = new User();
        searchTemplate.setUniversityId(universityID);
        Collection<User> users = userRepository.find(searchTemplate);
        User result = users.isEmpty() ? null : users.iterator().next();
        if(result != null && loadDisciplines){
            result.setDisciplines(getDisciplines(result));
        }
        return result;
    }

    @Override
    public Collection<User> getUsers(User searchTemplate, boolean loadDisciplines) {
        Collection<User> result = userRepository.find(searchTemplate);
        if(loadDisciplines){
            result.forEach(user -> user.setDisciplines(getDisciplines(user)));
        }
        return result;
    }

    private Set<Discipline> getDisciplines(User user){
        Discipline searchTemplate = new Discipline();
        searchTemplate.setUsers(Set.of(user));
        return new HashSet<>(disciplineService.getDisciplines(searchTemplate,false));
    }

}
