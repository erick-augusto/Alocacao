package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//Vai ter as informações gerais das turmas planejadas para aquele ano
//Corresponde à primeira etapa da alocação
@Entity
public class Turma implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    private String curso;
    
    @ManyToOne
    private Disciplina disciplina;
    
    //Horas de teoria
    private int t;
    
    //Horas pratica
    private int p;
    
    //Turno: D/N
    private String turno;
    
    //Campus: SA/SB
    private String campus;
    
    //Esse número é usado em um primeiro momento, quando as turmas ainda não estão definidas
    private int numTurmas;
    
    //Semanal ou quinzenal
    private String periodicidade;
    
    private int quadrimestre;

    
    //private Horario horarios;
    
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int getNumTurmas() {
        return numTurmas;
    }

    public void setNumTurmas(int numTurmas) {
        this.numTurmas = numTurmas;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }

    public int getQuadrimestre() {
        return quadrimestre;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }
    
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
}
