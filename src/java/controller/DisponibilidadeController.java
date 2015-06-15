package controller;

import facade.CreditoFacade;
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
import model.Credito;
import model.Disciplina;
import model.Disponibilidade;
import model.Docente;
import model.Pessoa;
import model.OfertaDisciplina;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import util.DisponibilidadeDataModel;
import util.OfertaDisciplinaDataModel;

@Named(value = "disponibilidadeController")
@SessionScoped
public class DisponibilidadeController implements Serializable {

    //Construtor (pega o usuario Logado)
    public DisponibilidadeController() {

        docente = (Docente) LoginBean.getUsuario();

    }
 
    private Disponibilidade disponibilidade;
    
    @EJB
    private OfertaDisciplinaFacade turmasFacade;
    
    @EJB
    private DisponibilidadeFacade disponibilidadeFacade;
    
    @EJB
    private CreditoFacade creditoFacade;
      
//-----------------------------------Fase I Alocacao-----------------------------------------------------    

    //Quantidade de creditos por quadrimestre para o planejamento anual
    private double creditosPlanejados;
    
    public double getCreditosPlanejados() {
        return docente.getCreditoQuad(quadrimestre);
    }
    
    private double creditosPlanejados2;

    public double getCreditosPlanejados2() {
       return docente.getCreditoQuad(quadrimestre);
    }

    public void setCreditosPlanejados2(double creditosPlanejados2) {
        this.creditosPlanejados2 = creditosPlanejados2;
    }
    
    

    public void setCreditosPlanejados(double creditosPlanejados) {
        this.creditosPlanejados = creditosPlanejados;
    }
    
    //Vai mudando de acordo com a oferta de disciplina escolhida ou retirada pelo docente
    private double creditosEscolhidos;

    public double getCreditosEscolhidos() {
        return creditosEscolhidos;
    }

    public void setCreditosEscolhidos(double creditosEscolhidos) {
        this.creditosEscolhidos = creditosEscolhidos;
    }
    
    public boolean changeColor(){
        
        return creditosEscolhidos > docente.getCreditoQuad(1L);
        
    }
    
    
    /**
     * Associa a quantidade de créditos ao quadrimestre atual e ao docente que
     * está fazendo o planejamento
     *
     * @param q Long
     */
    public void salvarCreditos(Long q) {

        Integer quad = (int) (long) q;

        //Verifica se já existe um planejamento de credito para aquele quadrimestre
        List<Credito> listCreditos = docente.getCreditos();
        
        Credito credito = creditoFacade.creditoQuadrimestre(docente, quad);
        if (credito != null) { //Já existe um planejamento para o quadrimestre
            try {
                int posicao = listCreditos.indexOf(credito);
                credito.setQuantidade(creditosPlanejados);
                listCreditos.add(posicao, credito);
                docente.setCreditos(listCreditos);
                creditoFacade.edit(credito);
                JsfUtil.addSuccessMessage("Créditos editados com sucesso!");
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar os créditos " + e.getMessage());
            }

        }
        else{
            credito = new Credito();
            credito.setQuadrimestre(quad);
            credito.setQuantidade(creditosPlanejados);
            credito.setDocente(docente);
            listCreditos.add(credito);
            docente.setCreditos(listCreditos);

            try {
                creditoFacade.save(credito);

                JsfUtil.addSuccessMessage("Créditos salvos com sucesso!");

            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível salvar os créditos " + e.getMessage());

            }
        }

        creditosPlanejados = 0.0;

        //Atualiza o usuário logado
        LoginBean.setUsuario(docente);
        
    }
    
    /**
     * Associa a quantidade de créditos ao quadrimestre atual e ao docente que
     * está fazendo o planejamento
     */
    public void salvarCreditos() {

       
        //Verifica se já existe um planejamento de credito para aquele quadrimestre
        List<Credito> listCreditos = docente.getCreditos();
        
        Credito credito = creditoFacade.creditoQuadrimestre(docente, quadrimestre);
        if (credito != null) { //Já existe um planejamento para o quadrimestre
            try {
                int posicao = listCreditos.indexOf(credito);
                credito.setQuantidade(creditosPlanejados);
                listCreditos.add(posicao, credito);
                docente.setCreditos(listCreditos);
                creditoFacade.edit(credito);
                JsfUtil.addSuccessMessage("Créditos editados com sucesso!");
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar os créditos " + e.getMessage());
            }

        }
        else{
            credito = new Credito();
            credito.setQuadrimestre(quadrimestre);
            credito.setQuantidade(creditosPlanejados);
            credito.setDocente(docente);
            listCreditos.add(credito);
            docente.setCreditos(listCreditos);

            try {
                creditoFacade.save(credito);

                JsfUtil.addSuccessMessage("Créditos salvos com sucesso!");

            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível salvar os créditos " + e.getMessage());

            }
        }

        creditosPlanejados = 0.0;

        //Atualiza o usuário logado
        LoginBean.setUsuario(docente);
        
    }
    
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
    
    private Docente docente;

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
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
    
    //Guarda se o docente escolheu teoria, pratica ou ambas para determinada
    //oferta de disciplina
    private List<String> selectedOptions;

    public List<String> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }
    
    /**
     * Preenche o atributo temporario "funcao" de uma oferta de disciplina
     * que indica se o docente vai dar Teoria, Pratica ou Ambas
     * @param oferta 
     */
    public void setFuncaoOferta(OfertaDisciplina oferta) {

        if (selectedOptions == null) {
            oferta.setFuncao("T e P");
        } else {
            if (selectedOptions.size() > 1) {
                oferta.setFuncao("T e P");
            } else {
                if (selectedOptions.get(0).equals("T")) {
                    oferta.setFuncao("Teoria");
                } else {
                    if (selectedOptions.get(0).equals("P")) {
                        oferta.setFuncao("Pratica");
                    }
                }
            }
        }

    }
    
    /**
     * Quando o docente escolhe uma oferta de disciplina, adiciona os creditos da
     * teoria e/ou pratica a variavel creditosEscolhidos
     * @param event 
     */
     public void adicionaCredito(SelectEvent event) {
        OfertaDisciplina oferta = (OfertaDisciplina) event.getObject();

        switch(oferta.getFuncao()){
            case "Teoria":
                creditosEscolhidos += oferta.getT();
                break;
            case "Pratica":
                creditosEscolhidos += oferta.getP();
                break;
            default:
                creditosEscolhidos += oferta.getT() + oferta.getP();
        }

    }
     
     /**
     * Quando o docente retira uma oferta de disciplina, remove os creditos da
     * teoria e/ou pratica da variavel creditosEscolhidos
     * @param event 
     */
     public void removeCredito(UnselectEvent event){
         OfertaDisciplina oferta = (OfertaDisciplina) event.getObject();

        switch(oferta.getFuncao()){
            case "Teoria":
                creditosEscolhidos -= oferta.getT();
                break;
            case "Pratica":
                creditosEscolhidos -= oferta.getP();
                break;
            default:
                creditosEscolhidos -= oferta.getT() + oferta.getP();
        }
     }
    
    //guarda as disponibilidades escolhidas pelo docente em cada quadrimestre
    private DisponibilidadeDataModel dispdataModel;

    public DisponibilidadeDataModel getDispdataModel() {

        if (dispdataModel == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByDocente(docente);

            dispdataModel = new DisponibilidadeDataModel(d);

        }

        return dispdataModel;
    }

    public DisponibilidadeDataModel getDispdataModel(int quad) {

        if (dispdataModel == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByDocenteQuad(docente, quad);

            dispdataModel = new DisponibilidadeDataModel(d);

        }

        return dispdataModel;
    }

    public void setDispdataModel(DisponibilidadeDataModel dispdataModel) {
        this.dispdataModel = dispdataModel;
    }
    
    private DisponibilidadeDataModel dispdataModel2;

    public DisponibilidadeDataModel getDispdataModel2() {
        if (dispdataModel2 == null) {

            List<Disponibilidade> d = disponibilidadeFacade.findByDocenteQuad(docente, quadrimestre);

            dispdataModel2 = new DisponibilidadeDataModel(d);

        }

        return dispdataModel2;
    }

    public void setDispdataModel2(DisponibilidadeDataModel dispdataModel2) {
        this.dispdataModel2 = dispdataModel2;
    }
    
    
    
    
    public void salvarDisponibilidade() {

        for (OfertaDisciplina oferta : ofertasEtapa1) {

            //Regarrega o objeto turma, inicializando a Colecao de Disponibilidades(Lazy)
            oferta = turmasFacade.inicializarColecaoDisponibilidades(oferta);
//            disponibilidade = new Disponibilidade("", docente, t);
            disponibilidade = new Disponibilidade("",oferta.getFuncao() ,docente, oferta);
            disponibilidadeFacade.save(disponibilidade);

        }

        dispdataModel = null;
        dispdataModel2 = null;
        
    }

    
    //Método para editar as escolhas de disponibilidade, definindo a ordem de preferencia e 
    //se prefere dar teoria ou prática ou ambos
    public void onCellEdit(CellEditEvent event) {

        Disponibilidade d = (Disponibilidade) dispdataModel2.getRowData();

        disponibilidadeFacade.merge(d);
        
    }

    //Usado para ordem de preferência do docente na escolha da oferta de disciplina
    private List<String> ordem;
    
    public List<String> getOrdem() {

        int tamanho = 0;

        for (Disponibilidade d : docente.getDisponibilidades()) {

            if (d.getOfertaDisciplina().getQuadrimestre() == quadrimestre) {
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
    
    public List<String> getOrdem(Long quad) {

        int tamanho = 0;

        for (Disponibilidade d : docente.getDisponibilidades()) {

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
    public List<String> getTipoOferta(OfertaDisciplina o){
            
            List<String> tp;
            tp = new ArrayList<>();
            tp.add("Selecione");
            if(o.getT() > 0){
                tp.add("Teoria");
            }
            if(o.getP() > 0){
                tp.add("Prática");
            }
            if(o.getP() > 0  && o.getT() > 0 ){
                tp.add("Teoria & Prática");
            }
            return tp;
        
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
        quadrimestre = 1;
        return "/Disponibilidade/FaseIQuad";

    }

    public String prepareQuad2() {

        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(2));
        quadrimestre = 2;
        return "/Disponibilidade/FaseIQuad";

    }

    public String prepareQuad3() {

        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(3));
        quadrimestre = 3;
        return "/Disponibilidade/FaseIQuad";
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
            afinidades = docente.getAfinidades();

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
    
    public void filtrarTurmasQuad() {
        dataModel = null;
        discAfinidades = new ArrayList<>();

        //Caso o usuário queira filtrarTurmas por afinidades
        if (filtrarAfinidades) {
            afinidades = docente.getAfinidades();

            //Quais disciplinas ele tem afinidade
            for (Afinidade a : afinidades) {
                if (a.getEstado().equals("Adicionada")) {
                    discAfinidades.add(a.getDisciplina());
                }

            }
        }

        

        dataModel = new OfertaDisciplinaDataModel(turmasFacade.filtrarAfinidTurnCampQuad(discAfinidades, turno, campus, quadrimestre));

        filtrarAfinidades = false;
        turno = "";
        campus = "";
    }

    public void limparFiltroQuad(Long quad) {

        dispdataModel = null;
        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad((int) (long) quad));
    }
    
    public void limparFiltroQuad() {

        dispdataModel = null;
        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(quadrimestre));
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

   

    public void limparFiltroDisp() {

        dispdataModel = null;
    }

   
    
    
    

}
