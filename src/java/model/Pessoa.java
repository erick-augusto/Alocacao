package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Pessoa implements Serializable {

    private static final long SerialVersionUID = 1L;

    public Pessoa() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pessoa_id")
    private Long ID;

    Pessoa(Long pessoaId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    private String siape;
    
    private String email;
    
    private String centro;

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }
    
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
//            ,cascade = CascadeType.ALL)
//    @Cascade(value = { org.hibernate.annotations.CascadeType.ALL })  
//    @LazyCollection(value = LazyCollectionOption.EXTRA)  
//    @Fetch(value = FetchMode.SUBSELECT) 
    private List<Afinidades> afinidades;
    

    public List<Afinidades> getAfinidades() {
        return afinidades;
    }

    public void setAfinidades(List<Afinidades> afinidades) {
        this.afinidades = afinidades;
    }
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Disponibilidade> disponibilidades;

    public List<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;

    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Pessoa)) {
            return false;
        }

        Pessoa other = (Pessoa) object;
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