package org.tu.varna.services;

import org.tu.varna.objects.Discipline;

import java.util.Collection;

public interface DisciplineService {
    void createDiscipline(Discipline discipline);

    void deleteDiscipline(Long disciplineId);

    void updateDiscipline(Discipline discipline);

    Discipline getDiscipline(Long disciplineId);

    Collection<Discipline> getDisciplines(Discipline searchTemplate);
}
