package org.tu.varna.services;

public interface DisciplineUserService {
    void addUserToDiscipline(Long disciplineId, String userUniversityId);
    void removeUserFromDiscipline(Long disciplineId, String userUniversityId);
}
