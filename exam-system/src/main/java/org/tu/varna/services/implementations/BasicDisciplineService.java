package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Discipline;
import org.tu.varna.repositories.DisciplineRepository;
import org.tu.varna.services.DisciplineService;

import java.util.Collection;

@ApplicationScoped
public class BasicDisciplineService implements DisciplineService {

    @Inject
    DisciplineRepository disciplineRepository;


    @Override
    public void createDiscipline(Discipline discipline) {
        disciplineRepository.save(discipline);
    }

    @Override
    public void deleteDiscipline(Long disciplineId) {
        Discipline toDelete = new Discipline();
        toDelete.setDisciplineId(disciplineId);
        disciplineRepository.delete(toDelete);
    }

    @Override
    public void updateDiscipline(Discipline discipline) {
        disciplineRepository.update(discipline);
    }

    @Override
    public Discipline getDiscipline(Long disciplineId) {
        Discipline searchTemplate = new Discipline();
        searchTemplate.setDisciplineId(disciplineId);
        Collection<Discipline> result = disciplineRepository.find(searchTemplate);
        return result.isEmpty() ? null : result.iterator().next();
    }

    @Override
    public Collection<Discipline> getDisciplines(Discipline searchTemplate) {
        return disciplineRepository.find(searchTemplate);
    }
}
