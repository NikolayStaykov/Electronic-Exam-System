package org.tu.varna.services;

import org.tu.varna.objects.User;

import java.util.Collection;

public interface UserService {

    void createUser(User user);

    void deleteUser(String universityID);

    void updateUser(User user);

    User getUser(String universityID, boolean loadDisciplines);

    Collection<User> getUsers(User searchTemplate, boolean loadDisciplines);

}
