package org.tu.varna.objects;

public class Discipline {
    private Long disciplineId;
    private String disciplineName;

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
}
