package controller;

import facade.AfinidadesFacade;
import facade.DisciplinaFacade;
import facade.PessoaFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import org.primefaces.model.DualListModel;
import util.DisciplinaLazyModel;

@Named(value = "disciplinaController")
@SessionScoped
public class DisciplinaController implements Serializable{
    
    public DisciplinaController() {
//        disciplina = new Disciplina();
        cadastro = new CadastroBean();
    }

    //Guarda o disciplina atual
    private Disciplina disciplina;
    
    private CadastroBean cadastro;

    @EJB
    private DisciplinaFacade disciplinaFacade;
    private DisciplinaLazyModel disciplinaDataModel;
    

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    private Disciplina getDisciplina(Long key) {
        return this.buscar(key);

    }

    public Disciplina getDisciplina() {
        if (disciplina == null) {
            disciplina = new Disciplina();
        }
        return disciplina;
    }


    //---------------------------------------Páginas web------------------------------------------------------------
    public String prepareCreate(int i) {
        disciplina = new Disciplina();
        if (i == 1) {
            return "/view/disciplina/Create";
        } else {
            return "Create";
        }
    }

    public String index() {
        disciplina = null;
        disciplinaDataModel = null;
        return "/index";
    }

    public String prepareEdit() {
        disciplina = (Disciplina) disciplinaDataModel.getRowData();
        return "Edit";
    }

    public String prepareView() {
        disciplina = (Disciplina) disciplinaDataModel.getRowData();
        //disciplina = disciplinaFacade.find(disciplina.getID());
        //disciplinaFacade.edit(disciplinaFacade.find(disciplina.getID()));
        //disciplinaFacade.edit(disciplina);
        return "View";
    }
    
    //---------------------------LazyData Model--------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        disciplinaDataModel = new DisciplinaLazyModel(this.listarTodas());
    }
    
    public DisciplinaLazyModel getDisciplinaLazyModel() {
        
        if(disciplinaDataModel == null){
            disciplinaDataModel = new DisciplinaLazyModel(this.listarTodas());
        }
        
        
        return this.disciplinaDataModel;
    }
    
    
//    public void preencherDataModel(){
//        
//        cadastro.cadastrarDisciplinas();
//        disciplinaDataModel = null;
//        
//    }
    
    //---------------------------------------------------CRUD-------------------------------------------------------
    private List<Disciplina> listarTodas() {
        return disciplinaFacade.findAll();

    }

    
    public void salvarNoBanco() {

        try {
            disciplinaFacade.save(disciplina);
            JsfUtil.addSuccessMessage("Disciplina " + disciplina.getNome() + " criado com sucesso!");
            disciplina = null;
            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");

        }

    }

    public Disciplina buscar(Long id) {

        return disciplinaFacade.find(id);
    }

    public void editar() {
        try {
            disciplinaFacade.edit(disciplina);
            JsfUtil.addSuccessMessage("Disciplina Editado com sucesso!");
            disciplina = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar o disciplina: " + e.getMessage());

        }
    }

    public void delete() {
        disciplina = (Disciplina) disciplinaDataModel.getRowData();
        try {
            disciplinaFacade.remove(disciplina);
            disciplina = null;
            JsfUtil.addSuccessMessage("Disciplina Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }

        recriarModelo();
    }
    
    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(disciplinaFacade.findAll(), true);
    }

    //--------------------------------------------------------------------------------------------------------------

    public void recriarModelo() {
    
        disciplinaDataModel = null;

    }
    
    
    //Cadastro-------------------------------------------------------------------------------------------
    
    private String converteEixo(String sigla) {

        char letra = sigla.charAt(2);

        switch (letra) {

            case 'J':
                return "Energia";
            case 'K':
                return "Estrutura da Matéria";
            case 'L':
                return "Processos de Transformação";
            case 'M':
                return "Comunicação e Informação";
            case 'N':
                return "Representação e Simulação";
            case 'O':
                return "Estado, Sociedade e Mercado";
            case 'P':
                return "Pensamento, Expressão e Significado";
            case 'Q':
                return "Espaço, Cultura e Temporalidade";
            case 'R':
                return "Ciência, Tecnologia e Inovação";
            default:
                return "Mais de um eixo";

        }
    }

    private String converteCurso(String sigla) {

        switch (sigla.substring(0, 2)) {

            //Cursos do CMCC
            case "MC":

                switch (sigla.charAt(2)) {

                    case 'A':
                        return "Bacharelado em Ciência da Computação";
                    case 'B':
                        return "Bacharelado em Matemática";
                    case 'C':
                        return "Bacharelado em Neurociência";
                    case 'D':
                        return "Licenciatura em Matemática";
                    case 'X':
                        return "Disciplina não ofertada";
                }

            //Cursos do CECS
            case "ES":

                switch (sigla.charAt(3)) {

                    case 'P':
                        return "Bacharelado em Políticas Públicas";
                    case 'S':
                        return "Engenharia Aeroespacial";
                    case 'U':
                        return "Engenharia Ambiental e Urbana";
                    case 'A':
                        return "Engenharia de Automação e Robótica";
                    case 'B':
                        return "Engenharia Biomédica";
                    case 'E':
                        return "Engenharia de Energia";
                    case 'G':
                        return "Engenharia de Gestão";
                    case 'I':
                        return "Engenharia de Informação";
                    case 'M':
                        return "Engenharia de Materiais";
                    case 'C':
                        return "Bacharelado em Economia";
                    case 'R':
                        return "Bacharelado em Relações Internacionais";
                    case 'T':
                        return "Bacharelado em Planejamento Territorial";
                    case 'X':
                        return "Disciplina não ofertada";
                }

            //Cursos do CCNH
            case "NH":

                switch (sigla.charAt(3)) {

                    case '1':
                        return "Ciências Biológicas";
                    case '2':
                        return "Filosofia";
                    case '3':
                        return "Física";
                    case '4':
                        return "Química";
                    case '5':
                        return "Licenciaturas";

                }
        }

        return null;

    }
    
    public void cadastrarDisciplinas() {

        String[] palavras;
        String eixo;
        String curso;

        try {

            FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/catalogoDisciplinas.csv");

            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); //cabeçalho
            // lê a primeira linha 
            // a variável "linha" recebe o valor "null" quando o processo 
            // de repetição atingir o final do arquivo texto 

            linha = lerArq.readLine();

            while (linha != null) {
//                linha = linha.replace("\"", "");

                palavras = linha.split(";");

                List<Disciplina> disciplinaExist = disciplinaFacade.findByName(palavras[2]);

                if (disciplinaExist.isEmpty()) {
                    Disciplina d = new Disciplina();
                    d.setNome(palavras[2]);

                    if (palavras[0].substring(0, 2).equals("BC") || palavras[0].substring(0, 2).equals("BH")
                            || palavras[0].substring(0, 2).equals("BI")) {

                        eixo = converteEixo(palavras[0]);
                        d.setEixo(eixo);

                    } else {

                        curso = converteCurso(palavras[0]);
                        d.setCurso(curso);

                    }

                    disciplinaFacade.save(d);
                }

                linha = lerArq.readLine();
            }

            arq.close();

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        Disciplina d = new Disciplina();
        recriarModelo();
        
      

    }

    //---------------------------------------------------------------------------------------------------
    
    //Para o Picklist
    
    private AfinidadesController afinidadesController;
    
   

    
    private List<Disciplina> disciplinas;
    
    private List<Afinidades> afinidades;
    
    private List<Disciplina> anteriores;
    

    public List<Disciplina> getDisciplinas() {
        return disciplinaFacade.findAll();
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    
    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    private List<Disciplina> source;
    private List<Disciplina> target;
    private DualListModel<Disciplina> all;

//    private String[] filtros;
//
//    public String[] getFiltros() {
//        return filtros;
//    }
//
//    public void setFiltros(String[] filtros) {
//        this.filtros = filtros;
//    }
//    
//    private String[] filtrosSelecionados;
//    private List<String> filtros2;
//    
//    @PostConstruct
//    public void init() {
//        filtros2 = new ArrayList<String>();
//        filtros2.add("Humanidades");
//        filtros2.add("Estrutura da Materia");
//        filtros2.add("Energia");
//        filtros2.add("Processos de Transformacao");
//        filtros2.add("Comunicacao e Informacao");
//        filtros2.add("Representacao e Simulacao");
//        filtros2.add("Estado, Sociedade e Mercado");
//        filtros2.add("Pensamento, Expressao e Significado");
//        filtros2.add("Espaco, Cultura e Temporalidade");
//        filtros2.add("Ciencia, Tecnologia e Inovacao");
//        filtros2.add("Mais de um eixo");
//                  
//    }
//    
//
//    public String[] getFiltrosSelecionados() {
//        return filtrosSelecionados;
//    }
// 
//    public void setFiltrosSelecionados(String[] filtrosSelecionados) {
//        this.filtrosSelecionados = filtrosSelecionados;
//    }
// 
//    public List<String> getFiltros2() {
//        return filtros2;
//    }
    

    @EJB
    private PessoaFacade pessoaFacade;
    
    @EJB AfinidadesFacade afinidadeFacade;


//    @PostConstruct
//    public void init() {
//
//        pessoa = pessoaFacade.find(1L);
//
//        target = pessoa.getDisciplinas();
//
//        source = disciplinaFacade.findAll();
//
//        for (Disciplina t : target) {
//            source.remove(t);
//        }
//
//        all = new DualListModel<Disciplina>(
//                source,
//                target
//        );
//    }

    public DualListModel<Disciplina> getAll() {

        //Sem Filtro
        if (all == null) {

            pessoa = LoginBean.getUsuario();
            
            afinidades = pessoa.getAfinidades();
            
            target = new ArrayList<>();
            anteriores = new ArrayList<>();
            
            for(Afinidades a: afinidades){
                target.add(a.getDisciplina());
                anteriores.add(a.getDisciplina());
            }

            
            source = disciplinaFacade.findAll();

            for (Disciplina t : target) {
                source.remove(t);
            }

            all = new DualListModel<>(
                    source,
                    target
            );
        } 
        
//        else {
////            setSource(disciplinaFacade.findByEixo(filtros));
//            for (Disciplina t : target) {
//                source.remove(t);
//            }
//
//            all = new DualListModel<Disciplina>(
//                    source,
//                    target
//            );
//        }

        return all;
    }

    public List<Disciplina> getSource() {
        return source;
    }

//    public DualListModel<Disciplina> getAll() {
//        return all;
//    }

    public void setAll(DualListModel<Disciplina> all) {
        this.all = all;
    }

    public List<Disciplina> getSelected() {
        return all.getTarget();
    }

    public void setSource(List<Disciplina> source) {
        this.source = source;
    }

    

//    public void filtrar() {
//
////        source = disciplinaFacade.findByEixo(filtros);
//        
//        source = disciplinaFacade.findByEixo(filtrosSelecionados);
//        
//        for (Disciplina t : target) {
//            source.remove(t);
//        }
//
//        all = new DualListModel<Disciplina>(
//                source,
//                target
//        );
//       
//
//    }
    
//    public void limparFiltro(){
//        
//        //filtros2 = null;
//        filtrosSelecionados = null;
//        filtros = null;
//        all = null;
//        
//    }

    public void gerenciarDisciplinas() {


        List<Disciplina> atuais = getSelected();
        
        for(Disciplina d: atuais){
            
            if(!anteriores.contains(d)){
                
                Calendar cal = Calendar.getInstance();
                d = disciplinaFacade.inicializarColecaoAfinidades(d);
                Afinidades afinidade = new Afinidades("Adicionada", cal.getTime(), pessoa, d);
                afinidadeFacade.save(afinidade);
            }
            
        }
        
        for(Disciplina d: anteriores){
            if(!atuais.contains(d)){
                Calendar cal = Calendar.getInstance();
//                Afini
//                        
//                List<Afinidades> afinidades = p.getAfinidades();
//
//        for (Afinidades a : afinidades) {
//            if (a.getDisciplina() == d) {
//                a.setEstado("Removida");
//                Calendar cal = Calendar.getInstance();
//                a.setDataAcao(cal.getTime());
//                afinidadeFacade.edit(a);
//            }
//        }        
                        
            }
        }
        
//        
//        pessoa.setDisciplinas(this.getSelected());
//
//        pessoaFacade.edit(pessoa);

    }
    
    //---------------------------------------------------------------------------------------------------
    

    @FacesConverter(forClass = Disciplina.class)
    public static class DisciplinaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DisciplinaController controller = (DisciplinaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "disciplinaController");
            return controller.getDisciplina(getKey(value));
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
            if (object instanceof Disciplina) {
                Disciplina d = (Disciplina) object;               
                return getStringKey(new BigDecimal(d.getID().toString()).setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Disciplina.class.getName());
            }
        }
    }

}

