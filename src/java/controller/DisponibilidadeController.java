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
import util.DisponibilidadeDataModel;
import util.OfertaDisciplinaDataModel;

@Named(value = "disponibilidadeController")
@SessionScoped
public class DisponibilidadeController implements Serializable {

    //Construtor (pega o usuario Logado)
    public DisponibilidadeController() {

        usuario = LoginBean.getUsuario();

    }

    //Atributos de Disponibilidade controller
    private Disponibilidade disponibilidade;

    private Pessoa usuario;
    
    private List<String> ordem;

    @EJB
    private OfertaDisciplinaFacade turmasFacade;
    
    @EJB
    private DisponibilidadeFacade disponibilidadeFacade;

    private List<OfertaDisciplina> turmasEtapa1;

    private List<OfertaDisciplina> turmasEtapa2;

    public List<OfertaDisciplina> getTurmasEtapa2() {

        if (turmasEtapa2 == null) {
            turmasEtapa2 = new ArrayList<>();
        }

        return turmasEtapa2;
    }

    public void setTurmasEtapa2(List<OfertaDisciplina> turmasEtapa2) {
        this.turmasEtapa2 = turmasEtapa2;
    }

    public List<OfertaDisciplina> getTurmasEtapa1() {

        if (turmasEtapa1 == null) {
            turmasEtapa1 = new ArrayList<>();
        }

        return turmasEtapa1;
    }

    public void setTurmasEtapa1(List<OfertaDisciplina> turmasEtapa1) {
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

        for (int i = 1; i <= tamanho; i++) {
            ordem.add(String.valueOf(i));
        }

        return ordem;
    }

    public List<String> getTipoDisp(Disponibilidade d){
            
            List<String> tp;
            tp = new ArrayList<String>();
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

    public void salvarDisponibilidade(int quad) {

        for (OfertaDisciplina t : turmasEtapa1) {

            //Regarrega o objeto turma, inicializando a Colecao de Disponibilidades(Lazy)
            t = turmasFacade.inicializarColecaoDisponibilidades(t);
            disponibilidade = new Disponibilidade("", usuario, t, quad);
            disponibilidadeFacade.save(disponibilidade);

        }

        dispdataModel = null;

//        org.primefaces.context.RequestContext.getCurrentInstance().execute("dlg.show();"); 
    }

    //------------------------------------Data Model---------------------------------------------------------
    //Data Model das OfertaDisciplina da Etapa I------------------------------------------------------------------------
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

    //Data Model das Disponibilidades---------------------------------------------------------------------------
    private DisponibilidadeDataModel dispdataModel;

    public DisponibilidadeDataModel getDispdataModel() {

        if (dispdataModel == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByPessoa(usuario);

            dispdataModel = new DisponibilidadeDataModel(d);

        }

        return dispdataModel;
    }

    public DisponibilidadeDataModel getDispdataModel(int quad) {

        if (dispdataModel == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByPessoaQuad(usuario, quad);

            dispdataModel = new DisponibilidadeDataModel(d);

        }

        return dispdataModel;
    }

    public void setDispdataModel(DisponibilidadeDataModel dispdataModel) {
        this.dispdataModel = dispdataModel;
    }
    
    
    public void sucessoFase1(){
        
        turmasEtapa1 = null;
        JsfUtil.addSuccessMessage("Disponibilidades em turmas salvas com sucesso!");
      
    }
    

    //------------------------------------Prepare das paginas web-----------------------------------------------
    //Paginas da Fase I de disponibilidade-----------------------------------------------------------------------
    //Retorna as turmas por quadrimestre
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

    public String prepareLogs1() {

        dispdataModel = new DisponibilidadeDataModel(disponibilidadeFacade.findAll());
        return "/Disponibilidade/LogsDisponibilidades1";

    }

    //----------------------------------------Filtros----------------------------------------------------------
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

    public void filtrarTurmas() {
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

        dataModel = new OfertaDisciplinaDataModel(turmasFacade.filtrarDTC(discAfinidades, turno, campus));
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

        dataModel = new OfertaDisciplinaDataModel(turmasFacade.filtrarDTCQ(discAfinidades, turno, campus, q));

        filtrarAfinidades = false;
        turno = "";
        campus = "";
    }

    public void filtrarTurmas2() {
        dataModel = null;
        discEtapa1 = new ArrayList<>();

        if (filtrarDisponibilidades) {
            setDisponibilidades = usuario.getDisponibilidades();

            for (Disponibilidade d : setDisponibilidades) {
                discEtapa1.add(d.getOfertaDisciplina().getDisciplina());
            }
        }

        dataModel = new OfertaDisciplinaDataModel(turmasFacade.filtrarDTC(discEtapa1, turno, campus));
    }

    public void limparFiltroTurmas() {
        dataModel = null;
        dispdataModel = null;

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

        disponibilidades = disponibilidadeFacade.findByDisciplinaTCQ(disciplina, campus, turno, quadrimestre);

        dispdataModel = new DisponibilidadeDataModel(disponibilidades);

    }

    public void limparFiltroDisp() {

        dispdataModel = null;
    }

    //Filtrar as disponibilidades por docente
    public void filtrarDispDoc() {

        disponibilidades = disponibilidadeFacade.findByPessoaQuad(pessoa, quadrimestre);

        dispdataModel = new DisponibilidadeDataModel(disponibilidades);

    }

}
