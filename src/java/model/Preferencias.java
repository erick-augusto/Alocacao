
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author erick
 */

@Entity
public class Preferencias implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public Preferencias(){
        
    }
    
    @Id
    @GeneratedValue//(strategy = GenerationType.AUTO)
    private Long ID;
    private int preferenciaHorarios;
    private String observacoes;
    private int quadrimestre;
    private Long pessoa_id;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
    public int getPreferenciaHorarios() {
        return preferenciaHorarios;
    }

    public void setPreferenciaHorarios(int preferenciaHorarios) {
        this.preferenciaHorarios = preferenciaHorarios;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getQuadrimestre() {
        return quadrimestre;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }

    public Long getPessoa_id() {
        return pessoa_id;
    }

    public void setPessoa_id(Long pessoa_id) {
        this.pessoa_id = pessoa_id;
    }
    
    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preferencias)) {
            return false;
        }
        Preferencias other = (Preferencias) object;
        if ((this.ID == null && other.ID != null) || (this.ID != null && !this.ID.equals(other.ID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Preferencias[ ID=" + ID + " ]";
    }*/
    @Override
    public String toString() {
        return "model.Preferencias["+preferenciaHorarios+","+observacoes+","+quadrimestre+","+pessoa_id+"]";
    }
}
