package controller;

import facade.DisciplinaFacade;
import facade.DisponibilidadeFacade;
import facade.PessoaFacade;
import facade.OfertaDisciplinaFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import model.OfertaDisciplina;
import util.DisponibilidadeDataModel;
import util.OfertaDisciplinaDataModel;
import util.OfertaDisciplinaLazyModel;


@Named(value = "turmasPlanejamentoController")
@SessionScoped
public class OfertaDisciplinaController implements Serializable{
    
    public OfertaDisciplinaController() {
        
    }

    @EJB
    private OfertaDisciplinaFacade ofertaDisciplinaFacade;
    
    @EJB
    private DisciplinaFacade disciplinaFacade;
    
    @EJB
    private DisponibilidadeFacade disponibilidadeFacade;
    
    @EJB
    private PessoaFacade pessoaFacade;
    
    private OfertaDisciplina oferta;

    private List<OfertaDisciplina> escolhidas;
    
    private OfertaDisciplinaDataModel dataModel;
    
    
    //--------------------------------------Filtros----------------------------------------------------------
    
    private boolean filtrarAfinidades;
    
    private String campus;
    
    private String turno;
    
    
    //-------------------------------------Getters e Setters--------------------------------------------------------

    public List<OfertaDisciplina> getEscolhidas() {
        return escolhidas;
    }

    public void setEscolhidas(List<OfertaDisciplina> escolhidas) {
        this.escolhidas = escolhidas;
    }

    public OfertaDisciplina getOferta() {
        return oferta;
    }

    public void setOferta(OfertaDisciplina oferta) {
        this.oferta = oferta;
    }
    
    

    public OfertaDisciplinaDataModel getDataModel() {
        
        if(dataModel == null){
            List<OfertaDisciplina> ofertas = ofertaDisciplinaFacade.findAll();
            dataModel = new OfertaDisciplinaDataModel(ofertas);
        }
        
        return dataModel;
    }

    public void setDataModel(OfertaDisciplinaDataModel dataModel) {
        this.dataModel = dataModel;
    }

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
    
    
    

    //---------------------------------------Páginas web------------------------------------------------------------
//    public String prepareCreate(int i) {
//        pessoa= new Pessoa();
//        if (i == 1) {
//            return "/view/pessoa/Create";
//        } else {
//            return "Create";
//        }
//    }
//
//    public String index() {
//        pessoa= null;
//        pessoaDataModel = null;
//        return "/index";
//    }
//
//    public String prepareEdit() {
//        pessoa= (Pessoa) pessoaDataModel.getRowData();
//        return "Edit";
//    }
//
//    public String prepareView() {
//        pessoa= (Pessoa) pessoaDataModel.getRowData();
//        //pessoa= ofertaDisciplinaFacade.find(pessoa.getID());
//        //pessoaFacade.edit(ofertaDisciplinaFacade.find(pessoa.getID()));
//        //pessoaFacade.edit(pessoa);
//        return "View";
//    }
    
    public String prepareQuad1(){
        
        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(1));
        return "/Disponibilidade/FaseIQuad1";
        
    }
    
    public String prepareQuad2(){
        
        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(2));
        return "/Disponibilidade/FaseIQuad2";
        
    }
    
    public String prepareQuad3(){
        
        dataModel = new OfertaDisciplinaDataModel(this.listarTodasQuad(3));
        return "/Disponibilidade/FaseIQuad3";
    }
    
    //---------------------------LazyData Model--------------------------------------------------------------------
    
    private OfertaDisciplinaLazyModel ofertas1LazyModel;
    private OfertaDisciplinaLazyModel ofertas2LazyModel;
    private OfertaDisciplinaLazyModel ofertas3LazyModel;
    
    @PostConstruct
    public void init() {
        ofertas1LazyModel = new OfertaDisciplinaLazyModel(this.listarTodasQuad(1));
        ofertas2LazyModel = new OfertaDisciplinaLazyModel(this.listarTodasQuad(2));
        ofertas3LazyModel = new OfertaDisciplinaLazyModel(this.listarTodasQuad(3));
//        pessoaDataModel = new PessoaLazyModel(this.listarTodas());
    }

    public OfertaDisciplinaLazyModel getOfertas1LazyModel() {
        
        if(ofertas1LazyModel == null){
            ofertas1LazyModel = new OfertaDisciplinaLazyModel(this.listarTodasQuad(1));
        }
        
        return ofertas1LazyModel;
    }

    public void setOfertas1LazyModel(OfertaDisciplinaLazyModel ofertas1LazyModel) {
        this.ofertas1LazyModel = ofertas1LazyModel;
    }

    public OfertaDisciplinaLazyModel getOfertas2LazyModel() {
        if(ofertas2LazyModel == null){
            ofertas2LazyModel = new OfertaDisciplinaLazyModel(this.listarTodasQuad(2));
        }
        return ofertas2LazyModel;
    }

    public void setOfertas2LazyModel(OfertaDisciplinaLazyModel ofertas2LazyModel) {
        this.ofertas2LazyModel = ofertas2LazyModel;
    }

    public OfertaDisciplinaLazyModel getOfertas3LazyModel() {
        if(ofertas3LazyModel == null){
            ofertas3LazyModel = new OfertaDisciplinaLazyModel(this.listarTodasQuad(3));
        }
        return ofertas3LazyModel;
    }

    public void setOfertas3LazyModel(OfertaDisciplinaLazyModel ofertas3LazyModel) {
        this.ofertas3LazyModel = ofertas3LazyModel;
    }

    
    //---------------------------------------------------CRUD-------------------------------------------------------
    private List<OfertaDisciplina> listarTodas() {
        return ofertaDisciplinaFacade.findAll();

    }
    
    //Retorna as ofertas por quadrimestre
    private List<OfertaDisciplina> listarTodasQuad(int quad){
        
        return ofertaDisciplinaFacade.findAllQuad(quad);
    }

    
    public void salvarNoBanco() {

        try {
            ofertaDisciplinaFacade.save(oferta);
//            JsfUtil.addSuccessMessage("Pessoa " + pessoa.getNome() + " criado com sucesso!");
            oferta= null;
//            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");

        }

    }

    public OfertaDisciplina buscar(Long id) {

        return ofertaDisciplinaFacade.find(id);
    }

    public void editar() {
        try {
            ofertaDisciplinaFacade.edit(oferta);
//            JsfUtil.addSuccessMessage("Pessoa Editado com sucesso!");
            oferta= null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar a oferta de disciplina: " + e.getMessage());

        }
    }
    
    public void deletarPorDisciplina(Disciplina d){
        
        List<Disciplina> ds = new ArrayList<>();
        ds.add(d);
        List<OfertaDisciplina> ofertas = ofertaDisciplinaFacade.filtrarDTC(ds, "", "");
        
        for(OfertaDisciplina oferta:ofertas){
            ofertaDisciplinaFacade.remove(oferta);
        }
        
    }


        public void deleteAll(Long quad) {

        List<OfertaDisciplina> ofertas = listarTodasQuad((int)(long)quad);
        
        for (OfertaDisciplina o : ofertas) {
            o = ofertaDisciplinaFacade.inicializarColecaoDisponibilidades(o);
            Set<Disponibilidade> ds = o.getDisponibilidades();
            Pessoa atual;
            for (Disponibilidade d : ds) {
                o.getDisponibilidades().remove(d);
                atual = d.getPessoa();
                atual.getDisponibilidades().remove(d);
                pessoaFacade.edit(atual);
                if (atual.getNome().equals(LoginBean.getUsuario().getNome())) {
                    LoginBean.setUsuario(atual);
                }
                disponibilidadeFacade.remove(d);

            }
            ofertaDisciplinaFacade.remove(o);
        }
        
        ofertas1LazyModel = null;
        
    }
        
        public void deleteAllQuad(Long quad) {

        Integer q = (int)(long) quad;    
        List<OfertaDisciplina> all = listarTodasQuad(q);
        
        for (OfertaDisciplina t : all) {
            t = ofertaDisciplinaFacade.inicializarColecaoDisponibilidades(t);
            Set<Disponibilidade> ds = t.getDisponibilidades();
            Pessoa atual;

//            for (Disponibilidade d : ds) {
//                oferta.getDisponibilidades().remove(d);
//                atual = d.getPessoa();
//                atual.getDisponibilidades().remove(d);
//                pessoaFacade.edit(atual);
//                if (atual.getNome().equals(LoginBean.getUsuario().getNome())) {
//                    LoginBean.setUsuario(atual);
//                }
//                disponibilidadeFacade.remove(d);
//
//            }
            ofertaDisciplinaFacade.remove(t);

        }
        
        if(q == 1){
           ofertas1LazyModel = null; 
        }
        else{
            if(q == 2){
                ofertas2LazyModel = null;
            }
            else{
                ofertas3LazyModel = null;
            }
        }
        
        
    }
    
    
    
    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(ofertaDisciplinaFacade.findAll(), true);
    }

    //Resumo Fase I--------------------------------------------------------------------------------------------------------------

    public int qdtDocentesDisponibilidade(OfertaDisciplina of){
        
        of = ofertaDisciplinaFacade.inicializarColecaoDisponibilidades(of);
        
        Set<Disponibilidade> disponibilidades = of.getDisponibilidades();
        
        return disponibilidades.size();        
    }
    
    private DisponibilidadeDataModel docentesPorDisciplina;

    public DisponibilidadeDataModel getDocentesPorDisciplina() {
        
        if(docentesPorDisciplina == null){
            docentesPorDisciplina = new DisponibilidadeDataModel();
        }
        
        return docentesPorDisciplina;
    }

    public void setDocentesPorDisciplina(DisponibilidadeDataModel docentesPorDisciplina) {
        this.docentesPorDisciplina = docentesPorDisciplina;
    }
    
//    public AfinidadesLazyModel getAfinidadesDaDisciplina() {
//        
//        if(afinidadesDaDisciplina == null){
//            afinidadesDaDisciplina = new AfinidadesLazyModel(afinidadeFacade.findAll());
//        }
//        
//        return afinidadesDaDisciplina;
//    }
//
//    public void setAfinidadesDaDisciplina(AfinidadesLazyModel afinidadesDaDisciplina) {
//        this.afinidadesDaDisciplina = afinidadesDaDisciplina;
//    }
//    
//    public void preencherAfinidadesDisciplina(){
//        
//        afinidadesDaDisciplina = new AfinidadesLazyModel((List<Afinidade>) selecionada.getAfinidades());
//        
//    }

    
    //Cadastro-------------------------------------------------------------------------------------------

    
    //Cadastrar oferta primeiro quadrimestre
    public void turmaQuad1(){
        
        String[] palavras;
        
        //Primeiro quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad1.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    
                    String codigo = palavras[3];
                    
                    Disciplina d = disciplinaFacade.findByCodOrName(codigo, nome);

                    if (d != null) {
//                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }
  
                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(1);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas1LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        
    }
    
    //Cadastrar oferta 2 quadrimestre
    public void turmaQuad2(){
        
        String[] palavras;
        
        //Segundo quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad2.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    
                    String codigo = palavras[3];
                    
                    Disciplina d = disciplinaFacade.findByCodOrName(codigo, nome);

                    if (d != null) {
//                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }

                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(2);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas2LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        
        
    }
    
    //Cadastrar oferta 3 quadrimestre
    public void turmaQuad3(){
        String[] palavras;
        
        //Terceiro quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad3.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    
                    String codigo = palavras[3];
                    
                    Disciplina d = disciplinaFacade.findByCodOrName(codigo, nome);

                    if (d != null) {
//                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }

                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(3);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas3LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        
    }
    
    //Cadastrar todos os quadrimestres
    public void cadastrarOfertasDisciplinas(int quad) {

        String[] palavras;

        //Cadastrar todos quadrimestres
        if (quad == 0) {

            //Primeiro quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad1.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    List<Disciplina> ds = disciplinaFacade.findByName(nome);

                    if (!ds.isEmpty()) {
                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }

                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(1);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas1LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }

            //Segundo quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad2.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    List<Disciplina> ds = disciplinaFacade.findByName(nome);

                    if (!ds.isEmpty()) {
                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }

                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(2);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas2LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }

            //Terceiro quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad3.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    List<Disciplina> ds = disciplinaFacade.findByName(nome);

                    if (!ds.isEmpty()) {
                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }

                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(2);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas3LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }

        } else {

            if (quad == 1) {
                //Primeiro quadrimestre
                try {

                    FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad1.csv");

                    BufferedReader lerArq = new BufferedReader(arq);

                    String linha = lerArq.readLine(); //cabeçalho
                    // lê d primeira linha 
                    // d variável "linha" recebe o valor "null" quando o processo 
                    // de repetição atingir o final do arquivo texto 

                    linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                    while (linha != null) {

                        linha = linha.replaceAll("\"", "");

                        palavras = linha.split("_", -1);

                        oferta = new OfertaDisciplina();

                        oferta.setCurso(palavras[2]);

                        String nome = palavras[4];
                        List<Disciplina> ds = disciplinaFacade.findByName(nome);

                        if (!ds.isEmpty()) {
                            Disciplina d = disciplinaFacade.findByName(nome).get(0);
                            oferta.setDisciplina(d);
                        }

                        oferta.setT(Integer.parseInt(palavras[5]));
                        oferta.setP(Integer.parseInt(palavras[6]));
                        oferta.setTurno(palavras[11]);
                        oferta.setCampus(palavras[12]);
                        if (!palavras[13].equals("")) {
                            oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                        }

                        if (!palavras[19].equals("")) {
                            oferta.setPeriodicidade(palavras[19]);
                        }

                        oferta.setQuadrimestre(1);

                        salvarNoBanco();

                        linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                    }

                    arq.close();
                    ofertas1LazyModel = null;

                } catch (IOException e) {
                    System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
                }
            } else {

                if (quad == 2) {
                    //Segundo quadrimestre
                    try {

                        FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad2.csv");

                        BufferedReader lerArq = new BufferedReader(arq);

                        String linha = lerArq.readLine(); //cabeçalho
                        // lê d primeira linha 
                        // d variável "linha" recebe o valor "null" quando o processo 
                        // de repetição atingir o final do arquivo texto 

                        linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                        while (linha != null) {

                            linha = linha.replaceAll("\"", "");

                            palavras = linha.split("_", -1);

                            oferta = new OfertaDisciplina();

                            oferta.setCurso(palavras[2]);

                            String nome = palavras[4];
                            List<Disciplina> ds = disciplinaFacade.findByName(nome);

                            if (!ds.isEmpty()) {
                                Disciplina d = disciplinaFacade.findByName(nome).get(0);
                                oferta.setDisciplina(d);
                            }

                            oferta.setT(Integer.parseInt(palavras[5]));
                            oferta.setP(Integer.parseInt(palavras[6]));
                            oferta.setTurno(palavras[11]);
                            oferta.setCampus(palavras[12]);
                            if (!palavras[13].equals("")) {
                                oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                            }

                            if (!palavras[19].equals("")) {
                                oferta.setPeriodicidade(palavras[19]);
                            }

                            oferta.setQuadrimestre(2);

                            salvarNoBanco();

                            linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                        }

                        arq.close();
                        ofertas2LazyModel = null;

                    } catch (IOException e) {
                        System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
                    }
                }
                
                else{
                    if(quad == 3){
                        //Terceiro quadrimestre
            try {

                FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/quad3.csv");

                BufferedReader lerArq = new BufferedReader(arq);

                String linha = lerArq.readLine(); //cabeçalho
                // lê d primeira linha 
                // d variável "linha" recebe o valor "null" quando o processo 
                // de repetição atingir o final do arquivo texto 

                linha = lerArq.readLine();

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split("_", -1);

                    oferta = new OfertaDisciplina();

                    oferta.setCurso(palavras[2]);

                    String nome = palavras[4];
                    List<Disciplina> ds = disciplinaFacade.findByName(nome);

                    if (!ds.isEmpty()) {
                        Disciplina d = disciplinaFacade.findByName(nome).get(0);
                        oferta.setDisciplina(d);
                    }

                    oferta.setT(Integer.parseInt(palavras[5]));
                    oferta.setP(Integer.parseInt(palavras[6]));
                    oferta.setTurno(palavras[11]);
                    oferta.setCampus(palavras[12]);
                    if (!palavras[13].equals("")) {
                        oferta.setNumTurmas(Integer.parseInt(palavras[13]));
                    }

                    if (!palavras[19].equals("")) {
                        oferta.setPeriodicidade(palavras[19]);
                    }

                    oferta.setQuadrimestre(2);

                    salvarNoBanco();

                    linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
                }

                arq.close();
                ofertas3LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
                    }
                }
            }

        }

    }

    private OfertaDisciplina getTurma(Long key) {

        return buscar(key);
        
    }
    
    

    
    //AutoComplete----------------------------------------------------------------------------------------
//    public List<Pessoa> completePessoa(String query) {
//        
//       query = query.toLowerCase();
//        
//        List<Pessoa> allPessoas = this.listarTodas();
//        List<Pessoa> filteredPessoas = new ArrayList<>();
//
//        for (Pessoa p : allPessoas) {
//            if (p.getNome().toLowerCase().startsWith(query)) {
//                filteredPessoas.add(p);
//            }
//        }
//        return filteredPessoas;
//    }

//    public List<Disciplina> completeDisciplina(String query) {
//        List<Disciplina> allDisciplinas = this.listarTodas();
//        List<Disciplina> filteredDisciplinas = new ArrayList<>();
//
//        for (int i = 0; i < allDisciplinas.size(); i++) {
//            Disciplina d = allDisciplinas.get(i);
//            if (d.getNome().toLowerCase().contains(query)) {
//                filteredDisciplinas.add(d);
//            }
//        }
//
//        return filteredDisciplinas;
//    }

    //----------------------------------------------------------------------------------------------------

    
    
    //---------------------------------------------------------------------------------------------------
    

    @FacesConverter(forClass = OfertaDisciplina.class)
    public static class OfertaDisciplinaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OfertaDisciplinaController controller = (OfertaDisciplinaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "turmasPlanejamentoController");
            return controller.getTurma(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof OfertaDisciplina) {
                OfertaDisciplina d = (OfertaDisciplina) object;    
                return getStringKey(d.getID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + OfertaDisciplina.class.getName());
            }
        }
    }
}