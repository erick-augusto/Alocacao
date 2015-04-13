package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Disciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    public Disciplina() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="disciplina_id")
    private Long ID;
    
    
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private Set<Afinidades> afinidades;
    
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private Set<TurmasPlanejamento> turmas;

    public Set<TurmasPlanejamento> getTurmas() {
        return turmas;
    }

    public void setTurmas(Set<TurmasPlanejamento> turmas) {
        this.turmas = turmas;
    }
    
    

    Disciplina(Long disciplinaId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    public Set<Afinidades> getAfinidades() {
        return afinidades;
    }

    public void setAfinidades(Set<Afinidades> afinidades) {
        this.afinidades = afinidades;
    }
    
    
    

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    private String eixo;

    public String getEixo() {
        return eixo;
    }

    public void setEixo(String eixo) {
        
        if(eixo.equals("")){
            this.eixo = null;
        }
        else{
            this.eixo = eixo;
        }
        
    
    }
    
    private String curso;

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        
        if(curso.equals("")){
            this.curso = null;
        }
        else{
            this.curso = curso;
        }
        
        
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;

    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Disciplina)) {
            return false;
        }

        Disciplina other = (Disciplina) object;
        if ((this.ID == null && other.ID != null) || (this.ID != null && !(this.ID.equals(other.ID)))) {
            return false;
        }

        return true;

    }

    @Override
    public String toString() {
        return this.nome;
    }

}

