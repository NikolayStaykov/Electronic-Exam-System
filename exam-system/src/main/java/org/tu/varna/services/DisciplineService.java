package org.tu.varna.services;

import org.tu.varna.entities.Discipline;

import java.util.Collection;

public interface DisciplineService {
    void createDiscipline(Discipline discipline);

    void deleteDiscipline(Long disciplineId);

    void updateDiscipline(Discipline discipline);

    Discipline getDiscipline(Long disciplineId, boolean loadUsers);

    Collection<Discipline> getDisciplines(Discipline searchTemplate,boolean loadUsers);
}
