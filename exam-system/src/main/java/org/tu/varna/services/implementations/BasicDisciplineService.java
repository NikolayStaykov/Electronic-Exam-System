package org.tu.varna.services.implementations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tu.varna.objects.Discipline;
import org.tu.varna.objects.User;
import org.tu.varna.repositories.DisciplineRepository;
import org.tu.varna.services.DisciplineService;
import org.tu.varna.services.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class BasicDisciplineService implements DisciplineService {

    @Inject
    DisciplineRepository disciplineRepository;

    @Inject
    UserService userService;

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
    public Discipline getDiscipline(Long disciplineId, boolean loadUsers) {
        Discipline searchTemplate = new Discipline();
        searchTemplate.setDisciplineId(disciplineId);
        Collection<Discipline> disciplines = disciplineRepository.find(searchTemplate);
        Discipline result = disciplines.isEmpty() ? null : disciplines.iterator().next();
        if(result != null && loadUsers){
            result.setUsers(getUsers(result));
        }
        return result;
    }

    @Override
    public Collection<Discipline> getDisciplines(Discipline searchTemplate, boolean loadUsers) {
        Collection<Discipline> result = disciplineRepository.find(searchTemplate);
        if(loadUsers){
            result.forEach(discipline -> discipline.setUsers(getUsers(discipline)));
        }
        return result;
    }

    private Set<User> getUsers(Discipline discipline){
        User searchTemplate = new User();
        searchTemplate.setDisciplines(Set.of(discipline));
        return new HashSet<>(userService.getUsers(searchTemplate,false));
    }
}
