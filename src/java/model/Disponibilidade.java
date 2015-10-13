package model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Disponibilidade implements Serializable {

    @Embeddable
    public static class Id implements Serializable {

        private Long pessoaId;

        private Long turmaId;

        public Id() {
        }

        public Id(Long pessoaId, Long turmaId) {
            this.pessoaId = pessoaId;
            this.turmaId = turmaId;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.pessoaId.equals(that.pessoaId) && this.turmaId.equals(that.turmaId);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return pessoaId.hashCode() + turmaId.hashCode();
        }

    }

    @EmbeddedId
    private Id id = new Id();

    private String ordemPreferencia;
  
    //Se o docente quer dar teoria, pratica ou ambos
    private String tp;

    @ManyToOne
    private Pessoa pessoa;

    @ManyToOne(cascade = CascadeType.ALL)
    private OfertaDisciplina ofertaDisciplina;

    public Disponibilidade() {
    }

    public Disponibilidade(String ordem, Pessoa p, OfertaDisciplina oD) {

        this.pessoa = p;

        this.ofertaDisciplina = oD;

        this.ordemPreferencia = ordem;

        this.id.pessoaId = p.getID();
        this.id.turmaId = oD.getID();

        //Integridade Referencial
        pessoa.getDisponibilidades().add(Disponibilidade.this);
        ofertaDisciplina.getDisponibilidades().add(Disponibilidade.this);
    }
    
    public Disponibilidade(String ordem, String tp, Pessoa p, OfertaDisciplina oD) {

        this.pessoa = p;

        this.ofertaDisciplina = oD;
        
        this.tp = tp;

        this.ordemPreferencia = ordem;

        this.id.pessoaId = p.getID();
        this.id.turmaId = oD.getID();

        //Integridade Referencial
        pessoa.getDisponibilidades().add(Disponibilidade.this);
        ofertaDisciplina.getDisponibilidades().add(Disponibilidade.this);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public void setOfertaDisciplina(OfertaDisciplina ofertaDisciplina) {
        this.ofertaDisciplina = ofertaDisciplina;
    }

    

    public String getOrdemPreferencia() {
        return ordemPreferencia;
    }

    public void setOrdemPreferencia(String ordemPreferencia) {
        this.ordemPreferencia = ordemPreferencia;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

}
