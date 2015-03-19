package controller;

import facade.DisponibilidadeFacade;
import facade.TurmasPlanejamentoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Afinidades;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import model.TurmasPlanejamento;
import org.primefaces.event.CellEditEvent;
import util.DisponibilidadeDataModel;
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

    private DisponibilidadeDataModel dispdataModel;

    public DisponibilidadeDataModel getDispdataModel() {
        
        if(dispdataModel == null){
            
            List<Disponibilidade> d = disponibilidadeFacade.findByPessoa(pessoa);
            
            dispdataModel = new DisponibilidadeDataModel(d);
            
        }
        
        return dispdataModel;
    }

    public void setDispdataModel(DisponibilidadeDataModel dispdataModel) {
        this.dispdataModel = dispdataModel;
    }
    
    
    
    
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

    private List<String> ordem;

    public List<String> getOrdem() {
        
        int tamanho = pessoa.getDisponibilidades().size();
        
        ordem = new ArrayList<>();
        
        ordem.add("Selecione");
        
        for(int i = 1; i <= tamanho; i++){
            ordem.add(String.valueOf(i));
        }
        
        return ordem;
    }

    public void setOrdem(List<String> ordem) {
        this.ordem = ordem;
    }
  
    public void onCellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        
//        Object newValue = event.getNewValue();
//        
//        int indice = event.getRowIndex();
        
        Disponibilidade d = (Disponibilidade) dispdataModel.getRowData();
        
        disponibilidadeFacade.merge(d);
         
//        if(newValue != null && !newValue.equals(oldValue)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
    }
    
    
    
    
    
    
    
    
    public void salvarDisponibilidade(){
        
        for(TurmasPlanejamento t: turmasEtapa1){
            
            //Regarrega o objeto turma, inicializando a Colecao de Disponibilidades(Lazy)
            t = turmasFacade.inicializarColecaoDisponibilidades(t);
            disponibilidade = new Disponibilidade("", "", pessoa, t);
            disponibilidadeFacade.save(disponibilidade);
            
        }
        
        dispdataModel = null;
        
//        org.primefaces.context.RequestContext.getCurrentInstance().execute("dlg.show();"); 
        
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
    
    private Set<Afinidades> afinidades;
    
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
        
        dispdataModel = null;
        
    }
    
    
    
    
    
}
