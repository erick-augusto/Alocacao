package controller;

import facade.DisponibilidadeFacade;
import facade.OfertaDisciplinaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Afinidade;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import model.OfertaDisciplina;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import util.DisponibilidadeDataModel;
import util.OfertaDisciplinaDataModel;

@Named(value = "disponibilidadeController")
@SessionScoped
public class DisponibilidadeController implements Serializable {

    //Construtor (pega o usuario Logado)
    public DisponibilidadeController() {

        usuario = LoginBean.getUsuario();

    }
 
    private Disponibilidade disponibilidade;
    
    @EJB
    private OfertaDisciplinaFacade turmasFacade;
    
    @EJB
    private DisponibilidadeFacade disponibilidadeFacade;
    
      
//-----------------------------------Fase I Alocacao-----------------------------------------------------    

    private List<OfertaDisciplina> ofertasEtapa1;
    
    public List<OfertaDisciplina> getOfertasEtapa1() {

        if (ofertasEtapa1 == null) {
            ofertasEtapa1 = new ArrayList<>();
        }

        return ofertasEtapa1;
    }

    public void setOfertasEtapa1(List<OfertaDisciplina> ofertasEtapa1) {
        this.ofertasEtapa1 = ofertasEtapa1;
    }
    
    private Pessoa usuario;
    
    public Pessoa getUsuario() {
        return usuario;
    }

    public void setUsuario(Pessoa usuario) {
        this.usuario = usuario;
    }
    
    //Data Model das OfertaDisciplina da Etapa I
    private OfertaDisciplinaDataModel dataModel;

    public OfertaDisciplinaDataModel getDataModel() {

        if (dataModel == null) {
            List<OfertaDisciplina> turmas = turmasFacade.findAll();
            dataModel = new OfertaDisciplinaDataModel(turmas);
        }

        return dataModel;
    }

    public void setDataModel(OfertaDisciplinaDataModel dataModel) {
        this.dataModel = dataModel;
    }
    
    private double quantidadeCreditos;

    public double getQuantidadeCreditos() {
        return quantidadeCreditos;
    }

    public void setQuantidadeCreditos(double quantidadeCreditos) {
        this.quantidadeCreditos = quantidadeCreditos;
    }
    
    
    
    public void onRowSelect(SelectEvent event) {
        
        if(ofertasEtapa1 != null){
            for(OfertaDisciplina o:ofertasEtapa1){
                quantidadeCreditos += o.getT();
            }
        }
        
    }
    
    //guarda as disponibilidades escolhidas pelo docente em cada quadrimestre
    private DisponibilidadeDataModel dispdataModel;

    public DisponibilidadeDataModel getDispdataModel() {

        if (dispdataModel == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByDocente(usuario);

            dispdataModel = new DisponibilidadeDataModel(d);

        }

        return dispdataModel;
    }

    public DisponibilidadeDataModel getDispdataModel(int quad) {

        if (dispdataModel == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByDocenteQuad(usuario, quad);

            dispdataModel = new DisponibilidadeDataModel(d);

        }

        return dispdataModel;
    }

    public void setDispdataModel(DisponibilidadeDataModel dispdataModel) {
        this.dispdataModel = dispdataModel;
    }
    
    public void salvarDisponibilidade() {

        for (OfertaDisciplina t : ofertasEtapa1) {

            //Regarrega o objeto turma, inicializando a Colecao de Disponibilidades(Lazy)
            t = turmasFacade.inicializarColecaoDisponibilidades(t);
            disponibilidade = new Disponibilidade("", usuario, t);
            disponibilidadeFacade.save(disponibilidade);

        }

        dispdataModel = null;
        
    }

    
    //Método para editar as escolhas de disponibilidade, definindo a ordem de preferencia e 
    //se prefere dar teoria ou prática ou ambos
    public void onCellEdit(CellEditEvent event) {

        Disponibilidade d = (Disponibilidade) dispdataModel.getRowData();

        disponibilidadeFacade.merge(d);
        
    }

    //Usado para ordem de preferência do docente na escolha da oferta de disciplina
    private List<String> ordem;
    
    public List<String> getOrdem() {

        int tamanho = usuario.getDisponibilidades().size();

        ordem = new ArrayList<>();

        ordem.add("Selecione");

        for (int i = 1; i <= tamanho; i++) {
            ordem.add(String.valueOf(i));
        }

        return ordem;
    }
    
    public List<String> getOrdem(Long quad) {

        int tamanho = 0;

        for (Disponibilidade d : usuario.getDisponibilidades()) {

            if (d.getOfertaDisciplina().getQuadrimestre() == (int) (long) quad) {
                tamanho++;
            }
        }

        ordem = new ArrayList<>();

        ordem.add("Selecione");

        for (int i = 1; i <= tamanho; i++) {
            ordem.add(String.valueOf(i));
        }

        return ordem;
        
    }
    
    public void setOrdem(List<String> ordem) {
        this.ordem = ordem;
    }
    
    //Usado para o docente definir se ele quer dar teoria, prática ou ambos 
    public List<String> getTipoDisp(Disponibilidade d){
            
            List<String> tp;
            tp = new ArrayList<>();
            tp.add("Selecione");
            if(d.getOfertaDisciplina().getT() > 0){
                tp.add("Teoria");
            }
            if(d.getOfertaDisciplina().getP() > 0){
                tp.add("Prática");
            }
            if(d.getOfertaDisciplina().getP() > 0  && d.getOfertaDisciplina().getT() > 0 ){
                tp.add("Teoria & Prática");
            }
            return tp;
        
    }
 
    public void sucessoFase1(){
        
        ofertasEtapa1 = null;
        JsfUtil.addSuccessMessage("Disponibilidades em turmas salvas com sucesso!");
      
    }


//-----------------------------------------Paginas web-----------------------------------------------
    
    private List<OfertaDisciplina> listarTodasQuad(int quad) {

        return turmasFacade.findAllQuad(quad);
    }

    public String prepareQuad1() {

        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(1));
        return "/Disponibilidade/FaseIQuad1";

    }

    public String prepareQuad2() {

        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(2));
        return "/Disponibilidade/FaseIQuad2";

    }

    public String prepareQuad3() {

        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(3));
        return "/Disponibilidade/FaseIQuad3";
    }

    
//-------------------------------------------Filtros----------------------------------------------------------
    //Filtros para as turmas da Etapa I------------------------------------------------------------------------
    private boolean filtrarAfinidades;

    private boolean filtrarDisponibilidades;

    private String campus;

    private String turno;

    private Set<Afinidade> afinidades;

    private Set<Disponibilidade> setDisponibilidades;

    private List<Disciplina> discAfinidades;

    private List<Disciplina> discEtapa1;

    private int quadrimestre;

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

    public int getQuadrimestre() {
        return quadrimestre;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }

    

    public void filtrarTurmasQuad(Long quad) {
        dataModel = null;
        discAfinidades = new ArrayList<>();

        //Caso o usuário queira filtrarTurmas por afinidades
        if (filtrarAfinidades) {
            afinidades = usuario.getAfinidades();

            //Quais disciplinas ele tem afinidade
            for (Afinidade a : afinidades) {
                if (a.getEstado().equals("Adicionada")) {
                    discAfinidades.add(a.getDisciplina());
                }

            }
        }

        Integer q = (int) (long) quad;

        dataModel = new OfertaDisciplinaDataModel(turmasFacade.filtrarAfinidTurnCampQuad(discAfinidades, turno, campus, q));

        filtrarAfinidades = false;
        turno = "";
        campus = "";
    }

    public void limparFiltroQuad(Long quad) {

        dispdataModel = null;
        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad((int) (long) quad));
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
    public void filtrarDisp() {

        disponibilidades = new ArrayList<>();

        disponibilidades = disponibilidadeFacade.findByDiscTurCamQuad(disciplina, campus, turno, quadrimestre);

        dispdataModel = new DisponibilidadeDataModel(disponibilidades);

    }

    public void limparFiltroDisp() {

        dispdataModel = null;
    }

    //Filtrar as disponibilidades por docente
    public void filtrarDispDoc() {

        disponibilidades = disponibilidadeFacade.findByDocenteQuad(pessoa, quadrimestre);

        dispdataModel = new DisponibilidadeDataModel(disponibilidades);

    }
    
    
    

}
