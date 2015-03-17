package controller;

import facade.DisponibilidadeFacade;
import facade.TurmasPlanejamentoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Afinidades;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import model.TurmasPlanejamento;
import util.TurmasPlanejamentoDataModel;


@Named(value = "disponibilidadeController")
@SessionScoped
public class DisponibilidadeController implements Serializable{
    
    public DisponibilidadeController(){
        
        pessoa = LoginBean.getUsuario();
        
    }
    
    private Disponibilidade disponibilidade;
    
    private Pessoa pessoa;
    
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    
    
    
    
    public void salvarDisponibilidade(){
        
        for(TurmasPlanejamento t: turmasEtapa1){
            
            //Regarrega o objeto turma, inicializando a Colecao de Disponibilidades(Lazy)
            t = turmasFacade.inicializarColecaoDisponibilidades(t);
            disponibilidade = new Disponibilidade("", pessoa, t);
            disponibilidadeFacade.save(disponibilidade);
            
        }
        
    }
    
    //------------------------------------Data Model---------------------------------------------------------
    
    private TurmasPlanejamentoDataModel dataModel;
    
    public TurmasPlanejamentoDataModel getDataModel() {
        
        if(dataModel == null){
            List<TurmasPlanejamento> turmas = turmasFacade.findAll();
            dataModel = new TurmasPlanejamentoDataModel(turmas);
        }
        
        return dataModel;
    }

    public void setDataModel(TurmasPlanejamentoDataModel dataModel) {
        this.dataModel = dataModel;
    }
    
    
    
    //--------------------------------------Filtros----------------------------------------------------------
    
    private boolean filtrarAfinidades;
    
    private String campus;
    
    private String turno;
    
    private List<Afinidades> afinidades;
    
    private List<Disciplina> discAfinidades;

    public boolean isFiltrarAfinidades() {
        return filtrarAfinidades;
    }

    public void setFiltrarAfinidades(boolean filtrarAfinidades) {
        this.filtrarAfinidades = filtrarAfinidades;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
    
    
    
    
    public void filtrar(){
        
        discAfinidades = new ArrayList<>();
        
        //Caso o usuário queira filtrar por afinidades
        if(filtrarAfinidades){
            afinidades = pessoa.getAfinidades();
            
            
            //Quais disciplinas ele tem afinidade
            for(Afinidades a: afinidades){
                if(a.getEstado().equals("Adicionada")){
                    discAfinidades.add(a.getDisciplina());
                }
     
            }
        }
        
        dataModel = new TurmasPlanejamentoDataModel(turmasFacade.filtrarDTC(discAfinidades, turno, campus));        
    }
    
    public void limparFiltro(){
        
        dataModel = null;
        
    }
    
    
    
    
    
}
