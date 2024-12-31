package org.tu.varna.entities;

import java.util.Set;

public class Discipline {
    private Long disciplineId;
    private String disciplineName;
    private Set<User> users;

    public Discipline(Long disciplineId, String disciplineName) {
        this.disciplineId = disciplineId;
        this.disciplineName = disciplineName;
    }

    public Discipline() {}

    public Long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {this.users = users;}
}
