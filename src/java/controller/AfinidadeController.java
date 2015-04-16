package controller;

import facade.AfinidadeFacade;
import facade.DisciplinaFacade;
import facade.PessoaFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import model.Afinidade;
import model.Disciplina;
import model.Pessoa;
import util.AfinidadesLazyModel;

@Named(value = "afinidadesController")
@SessionScoped
public class AfinidadeController implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public AfinidadeController() {
        
 
    }
    
    //Guarda o afinidade atual
    private Afinidade afinidade;

    //Disciplina 
    private Disciplina disciplina;
    
    private Disciplina paraAdicionar;
    
    private Disciplina paraRemover;
  
    //Disciplinas disponiveis
    private List<Disciplina> disponiveis;

    //Disciplinas escolhidas
    private List<Disciplina> escolhidas;
    
    private List<Afinidade> afinidadesAtivas;
    
    private Pessoa pessoa;
    
    private Pessoa pessoaFiltro;

    private List<Afinidade> afinidadesFiltradas;
      
    //Se for "true" mostra no Data model as disciplinas que foram incluídas e depois removidas
    //Utilizada na busca pela disciplina
    private boolean incluirRemovidasD;

    
    //Se for "true" mostra no Data model as disciplinas que foram incluídas e depois removidas
    //utilizada na busca pelo docente
    private boolean incluirRemovidasP;

    
    @EJB
    private AfinidadeFacade afinidadeFacade;
    private AfinidadesLazyModel afinidadesLazyModel;
    
    @EJB
    private PessoaFacade pessoaFacade;
    
    @EJB
    private DisciplinaFacade disciplinaFacade;
    private AfinidadesLazyModel afinidadeDataModel;
    
    


    //------------------------------------Getters e Setters----------------------------------------------------------------

    public Afinidade getAfinidade() {
        return afinidade;
    }

    public void setAfinidade(Afinidade afinidade) {
        this.afinidade = afinidade;
    }
    
    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public boolean isIncluirRemovidasD() {
        return incluirRemovidasD;
    }

    public void setIncluirRemovidasD(boolean incluirRemovidasD) {
        this.incluirRemovidasD = incluirRemovidasD;
    }
    
    public boolean isIncluirRemovidasP() {
        return incluirRemovidasP;
    }

    public void setIncluirRemovidasP(boolean incluirRemovidasP) {
        this.incluirRemovidasP = incluirRemovidasP;
    }

    public List<Disciplina> getEscolhidas() {
        return escolhidas;
    }

    public void setEscolhidas(List<Disciplina> escolhidas) {
        this.escolhidas = escolhidas;
    }

    public void setDisponiveis(List<Disciplina> disponiveis) {
        this.disponiveis = disponiveis;
    }

    public Disciplina getParaAdicionar() {
        return paraAdicionar;
    }

    public void setParaAdicionar(Disciplina paraAdicionar) {
        this.paraAdicionar = paraAdicionar;
    }

    public Disciplina getParaRemover() {
        return paraRemover;
    }

    public void setParaRemover(Disciplina paraRemover) {
        this.paraRemover = paraRemover;
    }

    public Pessoa getPessoaFiltro() {
        return pessoaFiltro;
    }

    public void setPessoaFiltro(Pessoa pessoaFiltro) {
        this.pessoaFiltro = pessoaFiltro;
    }
    
    

//---------------------------------------------------CRUD-------------------------------------------------------
    private List<Afinidade> listarTodas() {
        return afinidadeFacade.findAll();

    }

    
    public void salvarNoBanco() {

        try {
            afinidadeFacade.save(afinidade);
//            JsfUtil.addSuccessMessage("Afinidade " + afinidade.getNome() + " criado com sucesso!");
            afinidade = null;
//            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");

        }

    }

    public Afinidade buscar(Long id) {

        return afinidadeFacade.find(id);
    }

    public void editar() {
        try {
            afinidadeFacade.edit(afinidade);
            JsfUtil.addSuccessMessage("Afinidades Editado com sucesso!");
            afinidade = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar o afinidade: " + e.getMessage());

        }
    }

    public void delete() {
//        afinidade = (Afinidade) afinidadeDataModel.getRowData();
        try {
            afinidadeFacade.remove(afinidade);
            afinidade = null;
            JsfUtil.addSuccessMessage("Afinidades Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }

//        recriarModelo();
    }
    
    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(afinidadeFacade.findAll(), true);
    }
    
    //----------------------------Preenchimento das listas Disponiveis e Escolhidas-----------------------------------------------------

    //Esse método vê quais disciplinas a pessoa já escolheu,
    //para exibir só as que ela ainda não selecionou 
    //(não estão salvas na afinidade, ou estão salvas como "Removida", porque ela pode escolher de novo nesse caso)
    public List<Disciplina> getDisponiveis() {

        if (disponiveis == null) {
            
            //Usuário que fez o logon
            pessoa = LoginBean.getUsuario();
//            pessoa = pessoaFacade.reinicializarUsuario(pessoa);

            //Todas as afinidades do usuario
            afinidadesAtivas = new ArrayList(pessoa.getAfinidades());
            
            escolhidas = new ArrayList<>();
            
            //Adiciona ao array escolhidas as disciplinas que estao como "Adicionada" em afinidades
            for(Afinidade a: afinidadesAtivas){
                
                if(a.getEstado().equals("Adicionada")){
                    escolhidas.add(a.getDisciplina());
                }        
            }

            //Seleciona todas disciplinas do banco
            disponiveis = disciplinaFacade.findAll();

            //Remove de todas as disciplinas as que já foram escolhidas
            for (Disciplina e : escolhidas) {
                disponiveis.remove(e);
            }

        }

        return disponiveis;
    }
    
    
    public void salvarAfinidade() {
        
        //Data da inclusao/remocao
        Calendar cal = Calendar.getInstance();
        
        //Regarrega o objeto disciplina, inicializando a Colecao de Afinidade(Lazy)
        paraAdicionar = disciplinaFacade.inicializarColecaoAfinidades(paraAdicionar);
        
        afinidade = new Afinidade("Adicionada", cal.getTime(), pessoa, paraAdicionar);
        
        afinidadeFacade.merge(afinidade);
        
        disponiveis = null;
        paraAdicionar = null;
    }
    
    public void removerAfinidade(){
        
        //Percorre a lista de afinidades da pessoa e muda o status de Adicionada para removida
        for(Afinidade a: afinidadesAtivas){
            
            if(a.getDisciplina() == paraRemover){

                Calendar cal = Calendar.getInstance();
                a.setDataAcao(cal.getTime());
                a.setEstado("Removida");
                afinidadeFacade.edit(a);
            }
            
        }
        
        disponiveis = null;
        paraRemover = null;
        paraAdicionar = null;
                 
    }
    

    //---------------------------------------Páginas web------------------------------------------------------------
    public String prepareCreate(int i) {
        afinidade = new Afinidade();
        if (i == 1) {
            return "/view/afinidade/Create";
        } else {
            return "Create";
        }
    }
    
    public String prepareAfinidades(){
        disponiveis = null;
        escolhidas = null;
        return "/Afinidades/DefinirAfinidade";
    }

    public String index() {
        afinidade = null;
//        afinidadeDataModel = null;
        return "/index";
    }
//
//    public String prepareEdit() {
//        afinidade = (Afinidade) afinidadeDataModel.getRowData();
//        return "Edit";
//    }

//    public String prepareView() {
//        afinidade = (Afinidade) afinidadeDataModel.getRowData();
//        //afinidade = afinidadeFacade.find(afinidade.getID());
//        //afinidadeFacade.edit(afinidadeFacade.find(afinidade.getID()));
//        //afinidadeFacade.edit(afinidade);
//        return "View";
//    }
    
    //------------------------------Filtros de Disciplina-------------------------------------------
    
    private List<String> filtrosEixos;
    
    private List<String> filtrosSelecEixos;
    
    private List<String> filtrosCursos;
    
    private List<String> filtrosSelecCursos;
           
    
    @PostConstruct
    public void init() {
//        afinidadesLazyModel = new AfinidadesLazyModel(this.listarTodas());
        filtrosEixos = new ArrayList<>();
        filtrosEixos.add("Ciencia, Tecnologia e Inovacao");
        filtrosEixos.add("Comunicacao e Informacao");
        filtrosEixos.add("Energia");
        filtrosEixos.add("Espaco, Cultura e Temporalidade");
        filtrosEixos.add("Estado, Sociedade e Mercado");
        filtrosEixos.add("Estrutura da Materia");
        filtrosEixos.add("Humanidades");
        filtrosEixos.add("Pensamento, Expressao e Significado");
        filtrosEixos.add("Processos de Transformacao");
        filtrosEixos.add("Mais de um eixo");
        filtrosEixos.add("Representacao e Simulacao");
        

        filtrosCursos = new ArrayList<>();
        filtrosCursos.add("Bacharelado em Ciencia da Computacao");
        filtrosCursos.add("Bacharelado em Economia");
        filtrosCursos.add("Bacharelado em Planejamento Territorial");
        filtrosCursos.add("Bacharelado em Politicas Publicas");
        filtrosCursos.add("Bacharelado em Relacoes Internacionais");
        filtrosCursos.add("Ciencias Biologicas");
        filtrosCursos.add("Engenharia Aeroespacial");
        filtrosCursos.add("Engenharia Ambiental e Urbana");
        filtrosCursos.add("Engenharia Biomedica");
        filtrosCursos.add("Engenharia de Automação e Robotica");
        filtrosCursos.add("Engenharia de Energia");
        filtrosCursos.add("Engenharia de Gestao");
        filtrosCursos.add("Engenharia de Informacao");
        filtrosCursos.add("Engenharia de Materiais");
        filtrosCursos.add("Filosofia");
        filtrosCursos.add("Fisica");
        filtrosCursos.add("Licenciaturas");
        filtrosCursos.add("Quimica");
        
        
    }

    public List<String> getFiltrosEixos() {
        return filtrosEixos;
    }

    public void setFiltrosEixos(List<String> filtrosEixos) {
        this.filtrosEixos = filtrosEixos;
    }

    public List<String> getFiltrosSelecEixos() {
        return filtrosSelecEixos;
    }

    public void setFiltrosSelecEixos(List<String> filtrosSelecEixos) {
        this.filtrosSelecEixos = filtrosSelecEixos;
    }

    public List<String> getFiltrosCursos() {
        return filtrosCursos;
    }

    public void setFiltrosCursos(List<String> filtrosCursos) {
        this.filtrosCursos = filtrosCursos;
    }

    public List<String> getFiltrosSelecCursos() {
        return filtrosSelecCursos;
    }

    public void setFiltrosSelecCursos(List<String> filtrosSelecCursos) {
        this.filtrosSelecCursos = filtrosSelecCursos;
    }
    
    public void filtrar() {

//        source = disciplinaFacade.findByEixo(filtros);
        
        disponiveis = disciplinaFacade.findByEixoCurso(filtrosSelecEixos, filtrosSelecCursos);
        
        for (Disciplina t : escolhidas) {
            disponiveis.remove(t);
        }

    }
    
    public void limparFiltro(){
        
        //filtros2 = null;
        filtrosSelecCursos = null;
        filtrosSelecEixos = null;
//        filtros = null;
        disponiveis = null;
        
    }
    
    
    //---------------------------LazyData Model--------------------------------------------------------------------
    
    public int getTotal() {
        
        if(afinidadesLazyModel == null){
            return 0;
        }
        else{
           return afinidadesLazyModel.getRowCount(); 
        }
                
    }
     
    

    public AfinidadesLazyModel getAfinidadesLazyModel() {
//        if(afinidadesLazyModel == null){
//            afinidadesLazyModel = new AfinidadesLazyModel(this.listarTodas());
//        }
        
        return afinidadesLazyModel;
    }

    public void setAfinidadesLazyModel(AfinidadesLazyModel afinidadesLazyModel) {
        this.afinidadesLazyModel = afinidadesLazyModel;
    }
    
    //Preenche o LazyModel com as afinidades de acordo com a disciplina escolhida
    public void povoarLazyModelD() {

        disciplina = disciplinaFacade.inicializarColecaoAfinidades(disciplina);
        afinidadesFiltradas = new ArrayList(disciplina.getAfinidades());

        if (!incluirRemovidasD) {

//                for (Afinidade a : afinidadesFiltradas) {
//                    if (a.getEstado().equals("Removida")) {
//                        afinidadesFiltradas.remove(a);
//                    }
//                }
            for (int i = 0; i < afinidadesFiltradas.size(); i++) {
                if (afinidadesFiltradas.get(i).getEstado().equals("Removida")) {
                    afinidadesFiltradas.remove(afinidadesFiltradas.get(i));
                    --i;
                }
            }

        }
        afinidadesLazyModel = new AfinidadesLazyModel(afinidadesFiltradas);
        disciplina = null;
        incluirRemovidasD = false;

    }
    
    public void limparLazyModel(){
        afinidadesLazyModel = null;
    }

    //Preenche o LazyModel com as afinidades de acordo com o docente escolhido
    public void povoarLazyModelP() {

        afinidadesFiltradas = new ArrayList(pessoaFiltro.getAfinidades());

        if (!incluirRemovidasP) {

//                for (Afinidade a : afinidadesFiltradas) {
//                    if (a.getEstado().equals("Removida")) {
//                        afinidadesFiltradas.remove(a);
//                    }
//                }
            for (int i = 0; i < afinidadesFiltradas.size(); i++) {
                if (afinidadesFiltradas.get(i).getEstado().equals("Removida")) {
                    afinidadesFiltradas.remove(afinidadesFiltradas.get(i));
                    --i;
                }
            }

        }

        afinidadesLazyModel = new AfinidadesLazyModel(afinidadesFiltradas);
        pessoa = null;
        incluirRemovidasP = false;

    }
   
    //---------------------------------------------------------------------------------------------------
    

    @FacesConverter(forClass = Afinidade.class)
    public static class AfinidadesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AfinidadeController controller = (AfinidadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "afinidadeController");
            return controller.buscar(getKey(value));
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
            if (object instanceof Afinidade) {
                Afinidade d = (Afinidade) object;               
                return getStringKey(new BigDecimal(d.getId().toString()).setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Afinidade.class.getName());
            }
        }
    }

}

