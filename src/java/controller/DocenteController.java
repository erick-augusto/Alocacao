package controller;

import facade.CreditoFacade;
import facade.DocenteFacade;
import facade.PessoaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Afinidade;
import model.Credito;
import model.Disponibilidade;
import model.Docente;
import model.Pessoa;
import util.AfinidadeDataModel;
import util.DisponibilidadeDataModel;
import util.DocenteDataModel;
import util.PessoaLazyModel;

@Named(value = "docenteController")
@SessionScoped
public class DocenteController extends Filtros implements Serializable{
    
    public DocenteController(){
        quad = 1; //O primeiro quadrimestre é exibido por default
    }
    
    @EJB
    private DocenteFacade docenteFacade;
    
    @EJB
    private PessoaFacade pessoaFacade;
 
    @EJB
    private CreditoFacade creditoFacade;
    
    
    //Docente atual----------------------------------------------------
    private Docente docente;
    
    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    

//--------------------------------------------Cadastro dos docentes------------------------------------------------------
    
//Docente para salvar
    private Docente docenteSalvar;
    
    public Docente getDocenteSalvar() {
        
        if(docenteSalvar == null){
            docenteSalvar = new Docente();
        }
        
        return docenteSalvar;
    }

    public void setDocenteSalvar(Docente docenteSalvar) {
        this.docenteSalvar = docenteSalvar;
    } 
    
    private PessoaLazyModel docenteLazyModel;

    public PessoaLazyModel getDocenteLazyModel() {
        
        if(docenteLazyModel == null){
            docenteLazyModel = new PessoaLazyModel(pessoaFacade.listDocentes());
        }
        
        return docenteLazyModel;
    }
    
    @PostConstruct
    public void init() {
        docenteLazyModel = new PessoaLazyModel(pessoaFacade.listDocentes());
        
    }
    
    public void cadastrarDocentes() {

        String[] palavras;

        try {

            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/NetBeansProjects/Arquivos CSV/docentes.csv"), "UTF-8"))) {

                String linha = lerArq.readLine(); //cabeçalho

                linha = lerArq.readLine();

                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split(",");

                    List<Docente> docentes = docenteFacade.findByName(trataNome(palavras[1]));

                    if (docentes.isEmpty()) {

                        Docente d = new Docente();

                        d.setNome(trataNome(palavras[1]));
                        d.setSiape(palavras[2]);
                        d.setEmail(palavras[3]);
                        d.setCentro(palavras[4]);
                        d.setAdm(false);

                        docenteFacade.save(d);

                    }

                    linha = lerArq.readLine();
                }
            }

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        docenteLazyModel = null;
        JsfUtil.addSuccessMessage("Cadastro de docentes realizado com sucesso", "");

    }

    public void cadastrarDocentesCMCC() {

        String[] palavras;

        try {

            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Juliana\\Documents\\NetBeansProjects\\alocacao\\Arquivos Alocação\\Arquivos CSV\\Docentes CMCC.csv"), "UTF-8"))) {

                String linha = lerArq.readLine(); //cabeçalho

                linha = lerArq.readLine();

                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_");

                    List<Docente> docentes = docenteFacade.findByName(palavras[0]);

                    if (docentes.isEmpty()) {

                        Docente d = new Docente();

                        d.setNome(palavras[0]);
                        d.setSiape(palavras[1]);
                        d.setEmail(palavras[4]);
                        d.setCentro(palavras[2]);
                        d.setAreaAtuacao(palavras[3]);
                        d.setAdm(false);

                        docenteFacade.save(d);

                    }

                    linha = lerArq.readLine();
                }
            }

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        docenteLazyModel = null;
        JsfUtil.addSuccessMessage("Cadastro de docentes realizado com sucesso", "");

    }

    private String trataNome(String nome) {

        String retorno = "";
        String[] palavras = nome.split(" ");

        for (String p : palavras) {

            if (p.equals("DAS") || p.equals("DOS") || p.length() <= 2) {
                p = p.toLowerCase();
                retorno += p + " ";
            } else {
                p = p.charAt(0) + p.substring(1, p.length()).toLowerCase();
                retorno += p + " ";
            }

        }

        return retorno;

    }

//    public void cadastrarArea() {
//
//        String[] palavras;
//
//        try {
//
//            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/NetBeansProjects/Arquivos CSV/professores.csv"), "UTF-8"))) {
//
//                String linha = lerArq.readLine(); //cabeçalho
//
//                linha = lerArq.readLine();
//
//                while (linha != null) {
//
//                    linha = linha.replaceAll("\"", "");
//
//                    palavras = linha.split("_");
//
//                    List<Docente> docentes = docenteFacade.findByName(trataNome(palavras[0]));
//
//                    if (!docentes.isEmpty()) {
//
//                        Docente d = docentes.get(0);
//
//                        d.setAreaAtuacao(palavras[1]);
//
//                        docenteFacade.edit(d);
//
//                    }
//
//                    linha = lerArq.readLine();
//                }
//            }
//
//        } catch (IOException e) {
//            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
//        }
//
//        docenteLazyModel = null;
//        JsfUtil.addSuccessMessage("Cadastro de docentes realizado com sucesso", "");
//
//    }
    
//-------------------------------------------Resumo Afinidades-------------------------------------------------------------------------------------------
    
    //Afinidades de acordo com o docente
    private AfinidadeDataModel afinidadesDoDocente;
  
    private boolean mostrarAdicionadas;
  
    /**
     * Método para a exibição da quantidade de creditos de cada docente na tabela
     * de resumos de afinidades
     * @param d
     * @return 
     */
    public int qtdAfinidades(Docente d){
        
        Set<Afinidade> afinidades = d.getAfinidades();
        int qtd = 0;
        
        for(Afinidade a: afinidades){
            if(a.getEstado().equals("Adicionada")){
                qtd++;
            }
        }

        return qtd;
        
    }
    
    /**
     * Preenche as afinidades do docente selecionado 
     * para a visualização dos detalhes no Resumo das Afinidades
     */
    public void preencherAfinidadesDoDocente(){

        mostrarAdicionadas = false;
        
        List<Afinidade> afinidades;
        if (docente != null) {
            afinidades = new ArrayList<>(docente.getAfinidades());

        } else {
            afinidades = new ArrayList<>();
        }

        afinidadesDoDocente = new AfinidadeDataModel(afinidades);
     
    }
    
    /**
     * Filtro caso o usuario administrador não queira ver as afinidades que foram removidas
     */
    public void verSoAdicionadas() {

        List<Afinidade> afinidades = new ArrayList<>(docente.getAfinidades());
        List<Afinidade> adicionadas = new ArrayList<>();

        if (mostrarAdicionadas) {
            for (Afinidade a : afinidades) {
                if (a.getEstado().equals("Adicionada")) {
                    adicionadas.add(a);
                }
            }

            afinidadesDoDocente = new AfinidadeDataModel(adicionadas);
        } else {
            afinidadesDoDocente = new AfinidadeDataModel(afinidades);
        }

    }
    
    public AfinidadeDataModel getAfinidadesDoDocente() {
        
        return afinidadesDoDocente;
    }

    public void setAfinidadesDoDocente(AfinidadeDataModel afinidadesDoDocente) {
        this.afinidadesDoDocente = afinidadesDoDocente;
    }
    
    public boolean isMostrarAdicionadas() {
        return mostrarAdicionadas;
    }

    public void setMostrarAdicionadas(boolean mostrarAdicionadas) {
        this.mostrarAdicionadas = mostrarAdicionadas;
    }
 
//------------------------------------Fase I da alocação didática-----------------------------------------
   
    //Creditos por quadrimestre para o planejamento anual
    private double creditos;
    
    public double getCreditos() {
        return creditos;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }
    
    
    /**
     * Associa a quantidade de créditos ao quadrimestre atual e ao docente que
     * está fazendo o planejamento
     *
     * @param quad Long
     */
    public void salvarCreditos(Long quad) {

        docente = (Docente) LoginBean.getUsuario();
        Integer quadrimestre = (int) (long) quad;
        boolean salvar = true;

        //Verifica se já existe um planejamento de credito para aquele quadrimestre
        List<Credito> listCreditos = docente.getCreditos();
        if (listCreditos.size() > 0) {

            for (Credito c : listCreditos) {

                if (c.getQuadrimestre() == quadrimestre) { //substitui o planejamento anterior
                    c.setQuantidade(creditos);
                    try {
                        listCreditos.add(c);
                        docente.setCreditos(listCreditos);
                        creditoFacade.edit(c);
                        salvar = false;
                        JsfUtil.addSuccessMessage("Créditos editados com sucesso!");
                    } catch (Exception e) {
                        JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar os créditos " + e.getMessage());
                    }

                }
            }

        }

        if (salvar) {
            Credito credito = new Credito();
            credito.setQuadrimestre(quadrimestre);
            credito.setQuantidade(creditos);
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

        creditos = 0.0;

        LoginBean.setUsuario(docente);
        //salvar = true;
    }
    
//    public double creditosQuad(Long quad){
//        
//        docente = (Docente) LoginBean.getUsuario();
//        Integer quadrimestre = (int) (long) quad;
//        double credito = 0;
//        List<Credito> all = docente.getCreditos();
//        for(Credito c : all){
//            if(c.getQuadrimestre() == quadrimestre){
//                credito = c.getQuantidade();
//            }
//        }
//        
//        return credito;
//        
//    }
    
//-----------------------------------------Resumo Fase I-------------------------------------------------------------------------------------------
    
    private DisponibilidadeDataModel disponibilidadesDocente;
    
    //Quadrimestre para visualização dos docentes no resumo
    private int quad;
   
    public int qtdDisponibilidades(Docente d){
        
        Set<Disponibilidade> all = d.getDisponibilidades();
        List<Disponibilidade> byQuad = new ArrayList<>();
        
        for(Disponibilidade disp : all){
            if(disp.getOfertaDisciplina().getQuadrimestre() == quad){
                byQuad.add(disp);
            }
        }
        
        return byQuad.size();
        
    }
    
    public double creditosQuad(Docente d){
        
        double creditosQuad = 0.0;
        List<Credito> listCreditos = d.getCreditos();
        if(listCreditos.size() > 0){
            
            for(Credito c: listCreditos){
                if(c.getQuadrimestre() == quad){
                    creditosQuad = c.getQuantidade();
                }
            }
            
        }     
        return creditosQuad;
    }
    
    public void preencherDisponibilidadesDoDocente() {

        List<Disponibilidade> all;

        if (docente != null) {
            all = new ArrayList<>(docente.getDisponibilidades());
            List<Disponibilidade> byQuad = new ArrayList<>();
            for (Disponibilidade d : all) {
                if (d.getOfertaDisciplina().getQuadrimestre() == quad) {
                    byQuad.add(d);
                }
            }
            disponibilidadesDocente = new DisponibilidadeDataModel(byQuad);

        } else { //caso o usuario não tenha clicado em nada, para não dar nullpointer
            all = new ArrayList<>();
            disponibilidadesDocente = new DisponibilidadeDataModel(all);

        }

    }

    public DisponibilidadeDataModel getDisponibilidadesDocente() {
        return disponibilidadesDocente;
    }

    public void setDisponibilidadesDocente(DisponibilidadeDataModel disponibilidadesDocente) {
        this.disponibilidadesDocente = disponibilidadesDocente;
    }
    
    public int getQuad() {
        return quad;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }
//-----------------------------------------DataModel--------------------------------------------------
//Utilizado para exibição nos Resumos de Afinidades e da Fase I da Alocação
    private DocenteDataModel docenteDataModel;

    public DocenteDataModel getDocenteDataModel() {
        
        if(docenteDataModel == null){
            docenteDataModel = new DocenteDataModel(this.listarTodas());
        }
        
        return docenteDataModel;
    }
    
    
    
   
    
    //------------------------------Filtros de Docente-------------------------------------------
    
    public void filtrar() {
        
        if (!getFiltrosSelecAreaAtuacao().isEmpty()) {
            List<Docente> docentesFiltrados = docenteFacade.findByArea(getFiltrosSelecAreaAtuacao());

            setFiltrosSelecAreaAtuacao(null);
            docenteDataModel = new DocenteDataModel(docentesFiltrados);
            
        }
        else{
            docenteDataModel = new DocenteDataModel(docenteFacade.findAll());
        }

        
    }
    
    public void limparFiltro(){
     
        limparFiltroDocente();
        
        docenteDataModel = null;
        quad = 1;
        
    }


    
    
    //------------------------------------------CRUD---------------------------------------------------------------------------------------------
    
    public Docente buscar(Long id) {

        return docenteFacade.find(id);
    }
    
    public void salvar(){
        try {
            docenteFacade.save(docenteSalvar);
            JsfUtil.addSuccessMessage("Docente " + docenteSalvar.getNome() + " cadastrado com sucesso!");
            docenteSalvar = null;
            docenteLazyModel = null;
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Não foi possível cadastrar o docente");
        }
    }

    public void editar() {
        try {
            docenteFacade.edit(docente);
            JsfUtil.addSuccessMessage("Docente editado com sucesso!");
            docente = null;
            docenteLazyModel = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar o docente: " + e.getMessage());

        }
    }

    public void delete() {
        docente = (Docente) docenteLazyModel.getRowData();
        try {
            docenteFacade.remove(docente);
            docente = null;
            JsfUtil.addSuccessMessage("Docente Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }

        docenteLazyModel = null;
    }
    
    public List<Docente> listarTodas(){
        return docenteFacade.findAll();
    }
    
    //----------------------------------------Páginas web------------------------------------------------------
    
    public String prepareEdit() {
        docente = (Docente) (Pessoa) docenteLazyModel.getRowData();
        return "/Cadastro/editDocente";
    }
    
   
}
