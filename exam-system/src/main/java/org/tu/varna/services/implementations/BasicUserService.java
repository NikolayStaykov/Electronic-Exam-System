package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.User;
import org.tu.varna.repositories.UserRepository;
import org.tu.varna.services.UserService;

import java.util.Collection;

@ApplicationScoped
public class BasicUserService implements UserService {

    @Inject
    UserRepository userRepository;

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
    public User getUser(String universityID) {
        User searchTemplate = new User();
        searchTemplate.setUniversityId(universityID);
        Collection<User> result = userRepository.find(searchTemplate);
        return result.isEmpty() ? null : result.iterator().next();
    }

    @Override
    public Collection<User> getUsers(User searchTemplate) {
        return userRepository.find(searchTemplate);
    }
}
