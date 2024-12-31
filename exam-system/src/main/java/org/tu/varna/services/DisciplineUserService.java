package org.tu.varna.services;

import org.tu.varna.entities.Discipline;
import org.tu.varna.entities.User;

import java.util.Collection;

public interface DisciplineUserService {
    void addUserToDiscipline(Long disciplineId, String userUniversityId);
    void removeUserFromDiscipline(Long disciplineId, String userUniversityId);
    Collection<User> getUsersForDiscipline(Long disciplineId);
    Collection<Discipline> getDisciplinesForUser(String userUniversityId);
}
