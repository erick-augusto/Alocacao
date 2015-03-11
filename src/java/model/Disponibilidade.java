/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 *
 * @author charles
 */
@Entity
public class Disponibilidade {
   
    @EmbeddedId
    private Disponibilidade.Id id = new Disponibilidade.Id();
    
    private Horario horario;
    
    
    
    
    
    
    @Embeddable
    public static class Id implements Serializable{
        
        private Long pessoaId;
        
        private Long turmaId;
        
        public Id() {}
        
        public Id(Long pessoaId, Long turmaId){
            this.pessoaId = pessoaId;
            this.turmaId = turmaId;
        }
        
        @Override
        public boolean equals(Object o){
            if(o != null && o instanceof Id){
                Id that = (Id)o;
                return this.pessoaId.equals(that.pessoaId) && this.turmaId.equals(that.turmaId);
            }
            
            else{
                return false;
            }
        }
        
        @Override
        public int hashCode(){
            return pessoaId.hashCode() + turmaId.hashCode();
        }
        

    }   
    
    
}
