package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author erick
 */
@Entity
public class Fase implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue//(strategy = GenerationType.AUTO)

    private Long id;

    private boolean afinidades;
    
    private boolean fase1_quad1;
    
    private boolean fase1_quad2;
    
    private boolean fase1_quad3;
    
    private boolean fase2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAfinidades() {
        return afinidades;
    }

    public void setAfinidades(boolean afinidades) {
        this.afinidades = afinidades;
    }

    public boolean isFase1_quad1() {
        return fase1_quad1;
    }

    public void setFase1_quad1(boolean fase1_quad1) {
        this.fase1_quad1 = fase1_quad1;
    }

    public boolean isFase1_quad2() {
        return fase1_quad2;
    }

    public void setFase1_quad2(boolean fase1_quad2) {
        this.fase1_quad2 = fase1_quad2;
    }

    public boolean isFase1_quad3() {
        return fase1_quad3;
    }

    public void setFase1_quad3(boolean fase1_quad3) {
        this.fase1_quad3 = fase1_quad3;
    }

    public boolean isFase2() {
        return fase2;
    }

    public void setFase2(boolean fase2) {
        this.fase2 = fase2;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fase)) {
            return false;
        }
        Fase other = (Fase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Fase[ id=" + id + " ]";
    }
}
