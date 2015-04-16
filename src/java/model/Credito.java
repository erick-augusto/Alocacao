package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Credito implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    
    private int quantidade;
    
    private int quadrimestre;
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuadrimestre() {
        return quadrimestre;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;

    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Credito)) {
            return false;
        }

        Credito other = (Credito) object;
        if ((this.ID == null && other.ID != null) || (this.ID != null && !(this.ID.equals(other.ID)))) {
            return false;
        }

        return true;

    }

    @Override
    public String toString() {
        return "Créditos: " + this.quantidade;
    }
    
}
