package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Docente")
public class Docente extends Pessoa{
    
    private static final long serialVersionUID = 1L;
    
    public Docente(){
        
    }
    
    private String areaAtuacao;
   
    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getID() != null ? getID().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.getID() == null && other.getID() != null) || (this.getID()!= null && !this.getID().equals(other.getID()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNome();
    }
 
}
