///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package model;
//
//import java.io.Serializable;
//import javax.persistence.Embeddable;
//import javax.persistence.ManyToOne;
//
///**
// *
// * @author charles
// */
//@Embeddable
//public class AfinidadesID implements Serializable {
//    
//    @ManyToOne
//    private Pessoa pessoa;
//    
//    @ManyToOne
//    private Disciplina disciplina;
//
//    public Pessoa getPessoa() {
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public Disciplina getDisciplina() {
//        return disciplina;
//    }
//
//    public void setDisciplina(Disciplina disciplina) {
//        this.disciplina = disciplina;
//    }
//    
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
// 
//        AfinidadesID that = (AfinidadesID) o;
// 
//        if (pessoa != null ? !pessoa.equals(that.pessoa) : that.pessoa != null) return false;
//        if (disciplina != null ? !disciplina.equals(that.disciplina) : that.disciplina != null)
//            return false;
// 
//        return true;
//    }
// 
//    public int hashCode() {
//        int result;
//        result = (pessoa != null ? pessoa.hashCode() : 0);
//        result = 31 * result + (disciplina != null ? disciplina.hashCode() : 0);
//        return result;
//    }
//    
//    
//    
//}
