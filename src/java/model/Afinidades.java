
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

//Tentativa 3------------------------------------------------------------------

@Entity
public class Afinidades{
    
    @Embeddable
    public static class Id implements Serializable{
        
        private Long pessoaId;
        
        private Long disciplinaId;
        
        public Id() {}
        
        public Id(Long pessoaId, Long disciplinaId){
            this.pessoaId = pessoaId;
            this.disciplinaId = disciplinaId;
        }
        
        @Override
        public boolean equals(Object o){
            if(o != null && o instanceof Id){
                Id that = (Id)o;
                return this.pessoaId.equals(that.pessoaId) && this.disciplinaId.equals(that.disciplinaId);
            }
            
            else{
                return false;
            }
        }
        
        @Override
        public int hashCode(){
            return pessoaId.hashCode() + disciplinaId.hashCode();
        }

//        public Long getPessoaId() {
//            return pessoaId;
//        }
//
//        public void setPessoaId(Long pessoaId) {
//            this.pessoaId = pessoaId;
//        }
//
//        public Long getDisciplinaId() {
//            return disciplinaId;
//        }
//
//        public void setDisciplinaId(Long disciplinaId) {
//            this.disciplinaId = disciplinaId;
//        }
        
        
        
    }
    
    @EmbeddedId
    private Id id = new Id();
    
    private String estado;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataAcao;
    
    @ManyToOne(cascade = CascadeType.ALL)
//    @Column(insertable=false, updatable=false)
//    @JoinColumn(name = "pessoaId", referencedColumnName = "pessoa", insertable = false, updatable = false)
    private Pessoa pessoa;
    
    @ManyToOne(cascade = CascadeType.ALL)
//    @Column(insertable=false, updatable=false)
//  @JoinColumn(insertable = false, updatable = false)    
    private Disciplina disciplina;
    
    public Afinidades() {}
    
    public Afinidades(String estado, Date data, Pessoa p, Disciplina d){
        
        this.estado = estado;
        this.dataAcao = data;
        
        this.pessoa = p;
        this.disciplina = d;
        
        this.id.pessoaId = p.getID();
        this.id.disciplinaId = d.getID();
        
        //Integridade Referencial
        pessoa.getAfinidades().add(this);
        disciplina.getAfinidades().add(this);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getDataAcao() {
        return dataAcao;
    }

    public void setDataAcao(Date dataAcao) {
        this.dataAcao = dataAcao;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    
    
    
    
    
    
}