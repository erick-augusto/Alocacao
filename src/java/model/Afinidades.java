
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author charles
 */


//Tentativa 1 -------------------------------------------------------------------------------------------

//@Entity
//@AssociationOverrides({
//		@AssociationOverride(name = "pk.pessoa", 
//			joinColumns = @JoinColumn(name = "pessoa_id")),
//		@AssociationOverride(name = "pk.disciplina", 
//			joinColumns = @JoinColumn(name = "disciplina_id")) })
//public class Afinidades implements Serializable{
//    
//    @EmbeddedId
//    private AfinidadesID pk = new AfinidadesID();
//
//    public AfinidadesID getPk() {
//        return pk;
//    }
//
//    public void setPk(AfinidadesID pk) {
//        this.pk = pk;
//    }
//    
//    //Adicionada ou removida
//    private String estado;
//    
//    //Quando foi adicionada ou removida
//    @Temporal(javax.persistence.TemporalType.DATE)
//    private Date dataAcao;
//
//    public String getEstado() {
//        return estado;
//    }
//
//    public void setEstado(String estado) {
//        this.estado = estado;
//    }
//
//    public Date getDataAcao() {
//        return dataAcao;
//    }
//
//    public void setDataAcao(Date dataAcao) {
//        this.dataAcao = dataAcao;
//    }
//    
//    @Transient
//    private Disciplina disciplina;
//    
//    @Transient
//    private Pessoa pessoa;
//
//    public Disciplina getDisciplina() {
//        return getPk().getDisciplina();
//    }
//
//    public void setDisciplina(Disciplina disciplina) {
//        getPk().setDisciplina(disciplina);
//    }
//    
//
//
//    public Pessoa getPessoa() {
//        return getPk().getPessoa();
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        getPk().setPessoa(pessoa);
//    }
//    
//    
//    @Override
//    public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
// 
//		Afinidades that = (Afinidades) o;
// 
//		if (getPk() != null ? !getPk().equals(that.getPk())
//				: that.getPk() != null)
//			return false;
// 
//		return true;
//	}
// 
//    @Override
//	public int hashCode() {
//		return (getPk() != null ? getPk().hashCode() : 0);
//	}
//    
//    
//    
//    
//}

//Fim da Tentativa 1


//Tentativa 2---------------------------------------------------------------------------------------------
//@Entity
//public class Afinidades {
//
//    public Afinidades() {}
//
//    public Afinidades(AfinidadesId AfinidadesId) {
//        this.AfinidadesId = AfinidadesId;
//    }
//
//    @ManyToOne
//    @JoinColumn(name="pessoa_id", insertable=false, updatable=false)
//    private Pessoa pessoa;
//
//    @ManyToOne
//    @JoinColumn(name="disciplina", insertable=false, updatable=false)
//    private Disciplina disciplina;
//    
//    
//    
//    
//
//    @EmbeddedId
//    // Implemented as static class - see bellow
//    private AfinidadesId AfinidadesId;
//
//    // required because JoinedUserRole contains composite id
//    @Embeddable
//    public static class AfinidadesId implements Serializable {
//
//        @ManyToOne
//        @JoinColumn(name="pessoa_id")
//        private Pessoa pessoa;
//
//        @ManyToOne
//        @JoinColumn(name="disciplina_id")
//        private Disciplina disciplina;
//
//        // required no arg constructor
//        public AfinidadesId() {}
//
//        public AfinidadesId(Pessoa pessoa, Disciplina disciplina) {
//            this.pessoa = pessoa;
//            this.disciplina = disciplina;
//        }
//
//        public AfinidadesId(Long pessoaId, Long disciplinaId) {
//            this(new Pessoa(pessoaId), new Disciplina(disciplinaId));
//        }
//
//        @Override
//        public boolean equals(Object instance) {
//            if (instance == null)
//                return false;
//
//            if (!(instance instanceof AfinidadesId))
//                return false;
//
//            final AfinidadesId other = (AfinidadesId) instance;
//            if (!(pessoa.getID().equals(other
//                    
//                    getUser().getId())))
//                return false;
//
//            if (!(role.getId().equals(other.getRole().getId())))
//                return false;
//
//            return true;
//        }
//
//        @Override
//        public int hashCode() {
//            int hash = 7;
//            hash = 47 * hash + (this.pessoa != null ? this.pessoa.hashCode() : 0);
//            hash = 47 * hash + (this.disciplina != null ? this.disciplina.hashCode() : 0);
//            return hash;
//        }
//
//    }
//
//}

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
    
    @ManyToOne 
//    @JoinColumn(name = "pessoaId", referencedColumnName = "pessoa", insertable = false, updatable = false)
    private Pessoa pessoa;
    
    @ManyToOne
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