package controller;

import facade.AfinidadesFacade;
import facade.DisciplinaFacade;
import facade.PessoaFacade;
import java.io.Serializable;
import java.math.BigDecimal;
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

    public Afinidades getAfinidade() {
        return afinidade;
    }

    public void setAfinidade(Afinidades afinidade) {
        this.afinidade = afinidade;
    }
    

    //Disciplina 
    private Disciplina disciplina;

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
      
    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    private List<Afinidades> afinidadesFiltradas;
    
    
    //Se for "true" mostra no Data model as disciplinas que foram incluídas e depois removidas
    private boolean incluirRemovidasD;

    public boolean isIncluirRemovidasD() {
        return incluirRemovidasD;
    }

    public void setIncluirRemovidasD(boolean incluirRemovidasD) {
        this.incluirRemovidasD = incluirRemovidasD;
    }
    
    //Se for "true" mostra no Data model as disciplinas que foram incluídas e depois removidas
    private boolean incluirRemovidasP;

    public boolean isIncluirRemovidasP() {
        return incluirRemovidasP;
    }

    public void setIncluirRemovidasP(boolean incluirRemovidasP) {
        this.incluirRemovidasP = incluirRemovidasP;
    }

    
    
    

    @EJB
    private AfinidadesFacade afinidadeFacade;
    private AfinidadesLazyModel afinidadesLazyModel;
    
    @EJB
    private PessoaFacade pessoaFacade;
    
    @EJB
    private DisciplinaFacade disciplinaFacade;
    private AfinidadesLazyModel afinidadeDataModel;
    

    public void testeSalvarAfinidade(){
        
//        List<Pessoa> pessoas = pessoaFacade.findAll();
        
        pessoa = LoginBean.getUsuario();
        
        List<Disciplina> disciplinas = disciplinaFacade.findAll();
        
        disciplina = disciplinas.get(15);

        Calendar cal = Calendar.getInstance();
               
        afinidade = new Afinidades("Adicionada", cal.getTime(), pessoa, disciplina);
        
        afinidadeFacade.save(afinidade);
//        
//        disciplina.getAfinidades().add(afinidade);
//        disciplinaFacade.edit(disciplina);
//        
//        pessoa.getAfinidades().add(afinidade);
//        pessoaFacade.edit(pessoa);
         
        
//        afinidadeFacade.salvarAfinidade();
        
    }
    
    public void salvarAfinidade(Pessoa p, Disciplina d){
        
        Calendar cal = Calendar.getInstance();
                    
        afinidade = new Afinidades("Adicionada", cal.getTime(), p, d);
        
//        afinidadeFacade.save(afinidade);
        
        afinidadeFacade.save(afinidade);
        
    }
    
    public void removerAfinidade(Pessoa p, Disciplina d) {

        List<Afinidades> afinidades = p.getAfinidades();

        for (Afinidades a : afinidades) {
            if (a.getDisciplina() == d) {
                a.setEstado("Removida");
                Calendar cal = Calendar.getInstance();
                a.setDataAcao(cal.getTime());
                afinidadeFacade.edit(a);
            }
        }

    }
    
    public void testeRemoverAfinidade(){
        
        pessoa = LoginBean.getUsuario();
        
       List<Afinidades> afinidades = pessoa.getAfinidades();
        
       for(Afinidades a: afinidades){
           if(a.getDisciplina().getID() == 7){
               a.setEstado("Removida");
               Calendar cal = Calendar.getInstance();
               a.setDataAcao(cal.getTime());
               afinidadeFacade.edit(a);
           }
       }
        
        
    }


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
    
    //---------------------------LazyData Model--------------------------------------------------------------------
    private int total;

    public int getTotal() {
        
        if(afinidadesLazyModel == null){
            return 0;
        }
        
        
        
        else{
           return afinidadesLazyModel.getRowCount(); 
        }
        
        
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    
    
    
    @PostConstruct
    public void init() {
//        afinidadesLazyModel = new AfinidadesLazyModel(this.listarTodas());
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
        afinidadesFiltradas = disciplina.getAfinidades();

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

        afinidadesFiltradas = pessoa.getAfinidades();

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

    //--------------------------------------------------------------------------------------------------------------

//    public void recriarModelo() {
//    
//        afinidadeDataModel = null;
//
//    }

    private Object getAfinidade(Long key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  
    
    
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

