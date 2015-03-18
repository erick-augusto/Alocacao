package controller;

import facade.AfinidadesFacade;
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
import model.Afinidades;
import model.Disciplina;
import model.Pessoa;
import util.AfinidadesLazyModel;

@Named(value = "afinidadesController")
@SessionScoped
public class AfinidadesController implements Serializable{
    
    
    public AfinidadesController() {
        
 
    }
    
    //Guarda o afinidade atual
    private Afinidades afinidade;

    //Disciplina 
    private Disciplina disciplina;
    
    private Disciplina paraAdicionar;
    
    private Disciplina paraRemover;
  
    //Disciplinas disponiveis
    private List<Disciplina> disponiveis;

    //Disciplinas escolhidas
    private List<Disciplina> escolhidas;
    
    private List<Afinidades> afinidadesAtivas;
    
    private Pessoa pessoa;

    private List<Afinidades> afinidadesFiltradas;
      
    //Se for "true" mostra no Data model as disciplinas que foram incluídas e depois removidas
    //Utilizada na busca pela disciplina
    private boolean incluirRemovidasD;

    
    //Se for "true" mostra no Data model as disciplinas que foram incluídas e depois removidas
    //utilizada na busca pelo docente
    private boolean incluirRemovidasP;

    
    @EJB
    private AfinidadesFacade afinidadeFacade;
    private AfinidadesLazyModel afinidadesLazyModel;
    
    @EJB
    private PessoaFacade pessoaFacade;
    
    @EJB
    private DisciplinaFacade disciplinaFacade;
    private AfinidadesLazyModel afinidadeDataModel;
    
    


    //------------------------------------Getters e Setters----------------------------------------------------------------

    public Afinidades getAfinidade() {
        return afinidade;
    }

    public void setAfinidade(Afinidades afinidade) {
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
    

//---------------------------------------------------CRUD-------------------------------------------------------
    private List<Afinidades> listarTodas() {
        return afinidadeFacade.findAll();

    }

    
    public void salvarNoBanco() {

        try {
            afinidadeFacade.save(afinidade);
//            JsfUtil.addSuccessMessage("Afinidades " + afinidade.getNome() + " criado com sucesso!");
            afinidade = null;
//            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");

        }

    }

    public Afinidades buscar(Long id) {

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
//        afinidade = (Afinidades) afinidadeDataModel.getRowData();
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

            //Todas as afinidades do usuario
            afinidadesAtivas = new ArrayList(pessoa.getAfinidades());
            
            escolhidas = new ArrayList<>();
            
            //Adiciona ao array escolhidas as disciplinas que estao como "Adicionada" em afinidades
            for(Afinidades a: afinidadesAtivas){
                
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
        
        //Regarrega o objeto disciplina, inicializando a Colecao de Afinidades(Lazy)
        paraAdicionar = disciplinaFacade.inicializarColecaoAfinidades(paraAdicionar);
        
        afinidade = new Afinidades("Adicionada", cal.getTime(), pessoa, paraAdicionar);
        
        afinidadeFacade.merge(afinidade);
        
        disponiveis = null;
        paraAdicionar = null;
    }
    
    public void removerAfinidade(){
        
        //Percorre a lista de afinidades da pessoa e muda o status de Adicionada para removida
        for(Afinidades a: afinidadesAtivas){
            
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
    

//    public void testeSalvarAfinidade(){
//        
////        List<Pessoa> pessoas = pessoaFacade.findAll();
//        
//        pessoa = LoginBean.getUsuario();
//        
//        List<Disciplina> disciplinas = disciplinaFacade.findAll();
//        
//        disciplina = disciplinas.get(15);
//
//        Calendar cal = Calendar.getInstance();
//               
//        afinidade = new Afinidades("Adicionada", cal.getTime(), pessoa, disciplina);
//        
//        afinidadeFacade.save(afinidade);
////        
////        disciplina.getAfinidades().add(afinidade);
////        disciplinaFacade.edit(disciplina);
////        
////        pessoa.getAfinidades().add(afinidade);
////        pessoaFacade.edit(pessoa);
//         
//        
////        afinidadeFacade.salvarAfinidade();
//        
//    }
    
//    public void salvarAfinidade(Pessoa p, Disciplina d){
//        
//        Calendar cal = Calendar.getInstance();
//                    
//        afinidade = new Afinidades("Adicionada", cal.getTime(), p, d);
//        
////        afinidadeFacade.save(afinidade);
//        
//        afinidadeFacade.save(afinidade);
//        
//    }
//    
//    public void removerAfinidade(Pessoa p, Disciplina d) {
//
//        List<Afinidades> afinidades = p.getAfinidades();
//
//        for (Afinidades a : afinidades) {
//            if (a.getDisciplina() == d) {
//                a.setEstado("Removida");
//                Calendar cal = Calendar.getInstance();
//                a.setDataAcao(cal.getTime());
//                afinidadeFacade.edit(a);
//            }
//        }
//
//    }
//    
//    public void testeRemoverAfinidade(){
//        
//        pessoa = LoginBean.getUsuario();
//        
//       List<Afinidades> afinidades = pessoa.getAfinidades();
//        
//       for(Afinidades a: afinidades){
//           if(a.getDisciplina().getID() == 7){
//               a.setEstado("Removida");
//               Calendar cal = Calendar.getInstance();
//               a.setDataAcao(cal.getTime());
//               afinidadeFacade.edit(a);
//           }
//       }
//        
//        
//    }

    
    
    
    
    //---------------------------------------Páginas web------------------------------------------------------------
    public String prepareCreate(int i) {
        afinidade = new Afinidades();
        if (i == 1) {
            return "/view/afinidade/Create";
        } else {
            return "Create";
        }
    }

    public String index() {
        afinidade = null;
//        afinidadeDataModel = null;
        return "/index";
    }
//
//    public String prepareEdit() {
//        afinidade = (Afinidades) afinidadeDataModel.getRowData();
//        return "Edit";
//    }

//    public String prepareView() {
//        afinidade = (Afinidades) afinidadeDataModel.getRowData();
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
        filtrosEixos.add("Humanidades");
        filtrosEixos.add("Estrutura da Materia");
        filtrosEixos.add("Energia");
        filtrosEixos.add("Processos de Transformacao");
        filtrosEixos.add("Comunicacao e Informacao");
        filtrosEixos.add("Representacao e Simulacao");
        filtrosEixos.add("Estado, Sociedade e Mercado");
        filtrosEixos.add("Pensamento, Expressao e Significado");
        filtrosEixos.add("Espaco, Cultura e Temporalidade");
        filtrosEixos.add("Ciencia, Tecnologia e Inovacao");
        filtrosEixos.add("Mais de um eixo");
        
        filtrosCursos = new ArrayList<>();
        filtrosCursos.add("Engenharia de Energia");
        filtrosCursos.add("Bacharelado em Politicas Publicas");
        filtrosCursos.add("Engenharia Aeroespacial");
        filtrosCursos.add("Engenharia Biomedica");
        filtrosCursos.add("Engenharia de Automação e Robotica");
        filtrosCursos.add("Bacharelado em Relacoes Internacionais");
        filtrosCursos.add("Bacharelado em Planejamento Territorial");
        filtrosCursos.add("Engenharia de Gestao");
        filtrosCursos.add("Bacharelado em Economia");
        filtrosCursos.add("Engenharia Ambiental e Urbana");
        filtrosCursos.add("Quimica");
        filtrosCursos.add("Filosofia");
        filtrosCursos.add("Engenharia de Informacao");
        filtrosCursos.add("Ciencias Biologicas");
        filtrosCursos.add("Engenharia de Materiais");
        filtrosCursos.add("Fisica");
        filtrosCursos.add("Licenciaturas");
        
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

//                for (Afinidades a : afinidadesFiltradas) {
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

    //Preenche o LazyModel com as afinidades de acordo com o docente escolhido
    public void povoarLazyModelP() {

        afinidadesFiltradas = new ArrayList(pessoa.getAfinidades());

        if (!incluirRemovidasP) {

//                for (Afinidades a : afinidadesFiltradas) {
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

    
//    
//    public AfinidadesLazyModel getAfinidadesLazyModel() {
//        
//        if(afinidadeDataModel == null){
//            afinidadeDataModel = new AfinidadesLazyModel(this.listarTodas());
//        }
//        
//        
//        return this.afinidadeDataModel;
//    }
//    
    
//    public void preencherDataModel(){
//        
//        cadastro.cadastrarAfinidadess();
//        afinidadeDataModel = null;
//        
//    }
    
    
    //---------------------------------------------------------------------------------------------------
    

    @FacesConverter(forClass = Afinidades.class)
    public static class AfinidadesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AfinidadesController controller = (AfinidadesController) facesContext.getApplication().getELResolver().
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
            if (object instanceof Afinidades) {
                Afinidades d = (Afinidades) object;               
                return getStringKey(new BigDecimal(d.getId().toString()).setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Afinidades.class.getName());
            }
        }
    }

}

