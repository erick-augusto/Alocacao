package controller;

import facade.DisciplinaFacade;
import facade.TurmasPlanejamentoFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import model.Disciplina;
import model.TurmasPlanejamento;
import util.TurmasPlanejamentoDataModel;


@Named(value = "turmasPlanejamentoController")
@SessionScoped
public class TurmasPlanejamentoController implements Serializable{
    
    public TurmasPlanejamentoController() {
        
    }

    @EJB
    private TurmasPlanejamentoFacade turmasPlanejamentoFacade;
    
    @EJB
    private DisciplinaFacade disciplinaFacade;
    
    private TurmasPlanejamento turma;

    private List<TurmasPlanejamento> escolhidas;
    
    private TurmasPlanejamentoDataModel dataModel;
    
    
    //--------------------------------------Filtros----------------------------------------------------------
    
    private boolean filtrarAfinidades;
    
    private String campus;
    
    private String turno;
    
    public void filtrar(){
        
        
        if(filtrarAfinidades){
            
            
            
        }
        
        
    }
    
    //-------------------------------------Getters e Setters--------------------------------------------------------

    public List<TurmasPlanejamento> getEscolhidas() {
        return escolhidas;
    }

    public void setEscolhidas(List<TurmasPlanejamento> escolhidas) {
        this.escolhidas = escolhidas;
    }
    
    public TurmasPlanejamento getTurma() {
        return turma;
    }

    public void setTurma(TurmasPlanejamento turma) {
        this.turma = turma;
    }

    public TurmasPlanejamentoDataModel getDataModel() {
        
        if(dataModel == null){
            List<TurmasPlanejamento> turmas = turmasPlanejamentoFacade.findAll();
            dataModel = new TurmasPlanejamentoDataModel(turmas);
        }
        
        return dataModel;
    }

    public void setDataModel(TurmasPlanejamentoDataModel dataModel) {
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
//        //pessoa= turmasPlanejamentoFacade.find(pessoa.getID());
//        //pessoaFacade.edit(turmasPlanejamentoFacade.find(pessoa.getID()));
//        //pessoaFacade.edit(pessoa);
//        return "View";
//    }
    
    //---------------------------LazyData Model--------------------------------------------------------------------
    
//    private TurmasPlanejamentoLazyModel_NU turmasLazyModel;
//    
//    @PostConstruct
//    public void init() {
//        turmasLazyModel = new TurmasPlanejamentoLazyModel_NU(this.listarTodas());
//    }
//
//    public TurmasPlanejamentoLazyModel_NU getTurmasLazyModel() {
//        if(turmasLazyModel == null){
//            turmasLazyModel = new TurmasPlanejamentoLazyModel_NU(this.listarTodas());
//        }
// 
//        return this.turmasLazyModel;
//    }
//
//    public void setTurmasLazyModel(TurmasPlanejamentoLazyModel_NU turmasLazyModel) {
//        this.turmasLazyModel = turmasLazyModel;
//    }

    
//    @PostConstruct
//    public void init() {
//        pessoaDataModel = new PessoaLazyModel(this.listarTodas());
//    }
//    
//    public PessoaLazyModel getPessoaLazyModel() {
//        
//        if(pessoaDataModel == null){
//            pessoaDataModel = new PessoaLazyModel(this.listarTodas());
//        }
//        
//        
//        return this.pessoaDataModel;
//    }
//    
//    
////    public void preencherDataModel(){
////        
////        cadastro.cadastrarPessoas();
////        pessoaDataModel = null;
////        
////    }
    
    //---------------------------------------------------CRUD-------------------------------------------------------
    private List<TurmasPlanejamento> listarTodas() {
        return turmasPlanejamentoFacade.findAll();

    }

    
    public void salvarNoBanco() {

        try {
            turmasPlanejamentoFacade.save(turma);
//            JsfUtil.addSuccessMessage("Pessoa " + pessoa.getNome() + " criado com sucesso!");
            turma= null;
//            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");

        }

    }

    public TurmasPlanejamento buscar(Long id) {

        return turmasPlanejamentoFacade.find(id);
    }

    public void editar() {
        try {
            turmasPlanejamentoFacade.edit(turma);
//            JsfUtil.addSuccessMessage("Pessoa Editado com sucesso!");
            turma= null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar a turma: " + e.getMessage());

        }
    }

//    public void delete() {
//        
//        turma= (TurmasPlanejamento) pessoaDataModel.getRowData();
//        try {
//            turmasPlanejamentoFacade.remove(pessoa);
//            pessoa= null;
//            JsfUtil.addSuccessMessage("Pessoa Deletado");
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
//        }
//
//        recriarModelo();
//    }
    
    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(turmasPlanejamentoFacade.findAll(), true);
    }

    //--------------------------------------------------------------------------------------------------------------

//    public void recriarModelo() {
//    
//        pessoaDataModel = null;
//
//    }
    
    
    //Cadastro-------------------------------------------------------------------------------------------
    
    
    public void cadastrarTurmasPlanejamento() {

        String[] palavras;

        try {

            FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/PLANEJAMENTO 2014 quad 1.csv");

            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); //cabeçalho
            // lê a primeira linha 
            // a variável "linha" recebe o valor "null" quando o processo 
            // de repetição atingir o final do arquivo texto 

            linha = lerArq.readLine();
            
//            linha = linha.replaceAll("\"", "");

            while (linha != null) {
                
                linha = linha.replaceAll("\"", "");

                palavras = linha.split("_", -1);
                
                turma = new TurmasPlanejamento();
                
                turma.setCurso(palavras[2]);
                
                String nome = palavras[4];
                List<Disciplina> ds = disciplinaFacade.findByName(nome);
                
                if(!ds.isEmpty()){
                    Disciplina d = disciplinaFacade.findByName(nome).get(0);
                    turma.setDisciplina(d);
                }
                
                turma.setT(Integer.parseInt(palavras[5]));
                turma.setP(Integer.parseInt(palavras[6]));
                turma.setTurno(palavras[11]);
                turma.setCampus(palavras[12]);
                turma.setNumTurmas(Integer.parseInt(palavras[13]));
                
                
                if(!palavras[19].equals("")){
                   turma.setPeriodicidade(palavras[19]); 
                }

                turma.setQuadrimestre(1);

                salvarNoBanco();

                linha = lerArq.readLine();
//                linha = linha.replaceAll("\"", "");
            }

            arq.close();

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

     

    }

    private TurmasPlanejamento getTurma(Long key) {

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
    

    @FacesConverter(forClass = TurmasPlanejamento.class)
    public static class TurmasPlanejamentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TurmasPlanejamentoController controller = (TurmasPlanejamentoController) facesContext.getApplication().getELResolver().
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
            if (object instanceof TurmasPlanejamento) {
                TurmasPlanejamento d = (TurmasPlanejamento) object;    
                return getStringKey(d.getID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TurmasPlanejamento.class.getName());
            }
        }
    }
}