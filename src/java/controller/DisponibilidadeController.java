package controller;

import facade.DisponibilidadeFacade;
import facade.TurmasPlanejamentoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
    
    //Construtor (pega o usuario Logado)
    public DisponibilidadeController(){
        
        usuario = LoginBean.getUsuario();
        
    }
    
    //Atributos de Disponibilidade controller
    private Disponibilidade disponibilidade;
    
    private Pessoa usuario;
    
    private List<String> ordem;
      
    @EJB
    private TurmasPlanejamentoFacade turmasFacade;
    
    @EJB
    private DisponibilidadeFacade disponibilidadeFacade;
    
    private List<TurmasPlanejamento> turmasEtapa1;

    private List<TurmasPlanejamento> turmasEtapa2;
    
        public List<TurmasPlanejamento> getTurmasEtapa2() {
        
        if(turmasEtapa2 == null){
            turmasEtapa2 = new ArrayList<TurmasPlanejamento>();
        }
        
        return turmasEtapa2;
    }

    public void setTurmasEtapa2(List<TurmasPlanejamento> turmasEtapa2) {
        this.turmasEtapa2 = turmasEtapa2;
    }
    
    
    public List<TurmasPlanejamento> getTurmasEtapa1() {
        
        if(turmasEtapa1 == null){
            turmasEtapa1 = new ArrayList<TurmasPlanejamento>();
        }
        
        return turmasEtapa1;
    }

    public void setTurmasEtapa1(List<TurmasPlanejamento> turmasEtapa1) {
        this.turmasEtapa1 = turmasEtapa1;
    }

    
    
    public Pessoa getUsuario() {
        return usuario;
    }

    public void setUsuario(Pessoa usuario) {
        this.usuario = usuario;
    }

    public List<String> getOrdem() {
        
        int tamanho = usuario.getDisponibilidades().size();
        
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
            disponibilidade = new Disponibilidade("", "", usuario, t, t.getQuadrimestre());
            disponibilidadeFacade.save(disponibilidade);
            
        }
        
        dispdataModel = null;
        
//        org.primefaces.context.RequestContext.getCurrentInstance().execute("dlg.show();"); 
        
    }
    
    //------------------------------------Data Model---------------------------------------------------------
    
    //Data Model das Turmas da Etapa I------------------------------------------------------------------------
    
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
    
    
    //Data Model das Disponibilidades---------------------------------------------------------------------------
    
    private DisponibilidadeDataModel dispdataModel;

    public DisponibilidadeDataModel getDispdataModel() {
        
        if(dispdataModel == null){
            
            List<Disponibilidade> d = disponibilidadeFacade.findByPessoa(usuario);
            
            dispdataModel = new DisponibilidadeDataModel(d);
            
        }
        
        return dispdataModel;
    }
    
    public DisponibilidadeDataModel getDispdataModel(int quad) {
        
        if(dispdataModel == null){
            
            List<Disponibilidade> d = disponibilidadeFacade.findByPessoaQuad(usuario, quad);
            
            dispdataModel = new DisponibilidadeDataModel(d);
            
        }
        
        return dispdataModel;
    }

    public void setDispdataModel(DisponibilidadeDataModel dispdataModel) {
        this.dispdataModel = dispdataModel;
    }
    
   
    //----------------------------------------Filtros----------------------------------------------------------
    
    //Filtros para as turmas da Etapa I------------------------------------------------------------------------
    
    private boolean filtrarAfinidades;
    
    private boolean filtrarDisponibilidades;
    
    private String campus;
    
    private String turno;
    
    private Set<Afinidades> afinidades;
    
    private Set<Disponibilidade> setDisponibilidades;
    
    private List<Disciplina> discAfinidades;

    private List<Disciplina> discEtapa1;
    
    public boolean isFiltrarAfinidades() {
        return filtrarAfinidades;
    }
    
    public boolean isFiltrarDisponibilidades() {
        return filtrarDisponibilidades;
    }

    public void setFiltrarAfinidades(boolean filtrarAfinidades) {
        this.filtrarAfinidades = filtrarAfinidades;
    }
    
    public void setFiltrarDisponibilidades(boolean filtrarDisponibilidades) {
        this.filtrarDisponibilidades = filtrarDisponibilidades;
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
    
    public void filtrarTurmas(){
        dataModel = null;
        discAfinidades = new ArrayList<>();
        
        //Caso o usuário queira filtrarTurmas por afinidades
        if(filtrarAfinidades){
            afinidades = usuario.getAfinidades();
            
            
            //Quais disciplinas ele tem afinidade
            for(Afinidades a: afinidades){
                if(a.getEstado().equals("Adicionada")){
                    discAfinidades.add(a.getDisciplina());
                }
     
            }
        }
        
        dataModel = new TurmasPlanejamentoDataModel(turmasFacade.filtrarDTC(discAfinidades, turno, campus));        
    }
    
    public void filtrarTurmas2(){
        dataModel = null;
        discEtapa1 = new ArrayList<>();
        
        if(filtrarDisponibilidades){
            setDisponibilidades = usuario.getDisponibilidades();
            
            for (Disponibilidade d: setDisponibilidades){
                discEtapa1.add(d.getTurma().getDisciplina());             
            }
        }
        
        dataModel = new TurmasPlanejamentoDataModel(turmasFacade.filtrarDTC(discEtapa1, turno, campus)); 
    }
    public void limparFiltroTurmas(){
        dataModel = null;
        dispdataModel = null;
        
    }
    
    //Filtros para o Log das Disponibilidades da Etapa I-------------------------------------------------------
    
    private Disciplina disciplina;
    
    private Pessoa pessoa;

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
    
    private List<Disponibilidade> disponibilidades;

    public List<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
  
    //Filtrar as disponibilidades por disciplina
    public void filtrarDisp(){
        
        disponibilidades = new ArrayList<>();
        
        disponibilidades = disponibilidadeFacade.findByDisciplinaTC(disciplina, campus, turno);
        
        dispdataModel = new DisponibilidadeDataModel(disponibilidades);
        
    }
    
    public void limparFiltroDisp(){
        
        dispdataModel = null;
    }
    
    //Filtrar as disponibilidades por docente
    public void filtrarDispDoc(){
        
        disponibilidades = disponibilidadeFacade.findByPessoa(pessoa);
        
        dispdataModel = new DisponibilidadeDataModel(disponibilidades);
        
    }
    
    
    
    
    
    
    
}
