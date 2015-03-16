/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.DisponibilidadeFacade;
import facade.TurmasPlanejamentoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Disponibilidade;
import model.TurmasPlanejamento;

/**
 *
 * @author charles
 */
@Named(value = "disponibilidadeController")
@SessionScoped
public class DisponibilidadeController implements Serializable{
    
    public DisponibilidadeController(){
        
    }
    
    private Disponibilidade disponibilidade;
    
    @EJB
    private TurmasPlanejamentoFacade turmasFacade;
    
    @EJB
    private DisponibilidadeFacade disponibilidadeFacade;
    
    private List<TurmasPlanejamento> turmasEtapa1;

    public List<TurmasPlanejamento> getTurmasEtapa1() {
        
        if(turmasEtapa1 == null){
            turmasEtapa1 = new ArrayList<>();
        }
        
        return turmasEtapa1;
    }

    public void setTurmasEtapa1(List<TurmasPlanejamento> turmasEtapa1) {
        this.turmasEtapa1 = turmasEtapa1;
    }
    
    public void salvarDisponibilidade(){
        
        for(TurmasPlanejamento t: turmasEtapa1){
            
            //Regarrega o objeto turma, inicializando a Colecao de Disponibilidades(Lazy)
            t = turmasFacade.inicializarColecaoDisponibilidades(t);
            disponibilidade = new Disponibilidade("", LoginBean.getUsuario(), t);
            disponibilidadeFacade.save(disponibilidade);
            
        }
        
    }
    
    
    
}
