package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Disciplina;
import util.DisciplinaDataModel;

@Named(value = "filtrosBean")
@SessionScoped
public class FiltrosBean implements Serializable {

    public FiltrosBean() {
         
    }
    
    
    
    //------------------------------Filtros de Disciplina-------------------------------------------
    
    private List<String> filtrosEixos;
    
    private List<String> filtrosSelecEixos;
    
    private List<String> filtrosCursos;
    
    private List<String> filtrosSelecCursos;

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

        List<Disciplina> disciplinasFiltradas = disciplinaFacade.findByEixoCurso(filtrosSelecEixos, filtrosSelecCursos);
        
        disciplinaDataModel = new DisciplinaDataModel(disciplinasFiltradas);

    }
    
    public void limparFiltro(){
        
        filtrosSelecCursos = null;
        filtrosSelecEixos = null;
        disciplinaDataModel = null;
        
    }

    @PostConstruct
    public void init() {
        
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
        filtrosCursos.add("Engenharia de Automacao e Robotica");
        filtrosCursos.add("Engenharia de Energia");
        filtrosCursos.add("Engenharia de Gestao");
        filtrosCursos.add("Engenharia de Informacao");
        filtrosCursos.add("Engenharia de Materiais");
        filtrosCursos.add("Filosofia");
        filtrosCursos.add("Fisica");
        filtrosCursos.add("Licenciaturas");
        filtrosCursos.add("Quimica");
        
    }
   

    
    
//----------------------------------------AutoComplete----------------------------------------------------------------------------------------
    
    
//Disciplina----------------------------------------------------------------------------------------------------------    
    public List<Disciplina> completeDisciplina(String query) {
        
        query = query.toLowerCase();
        
        List<Disciplina> allDisciplinas = this.listarTodas();
        List<Disciplina> filteredDisciplinas = new ArrayList<>();

        for (Disciplina d : allDisciplinas) {
            if (d.getNome().toLowerCase().startsWith(query)) {
                filteredDisciplinas.add(d);
            }
        }
        return filteredDisciplinas;
    }
    
 //Eixo----------------------------------------------------------------------------------------------------------
    public List<String> completeEixo(String query){
        
        query = query.toLowerCase();
        
        List<String> eixos = new ArrayList<>();
        eixos.add("Estrutura da Materia");
        eixos.add("Processos de Transformacao");
        eixos.add("Comunicacao e Informacao");
        eixos.add("Representacao e Simulacao");
        eixos.add("Estado, Sociedade e Mercado");
        eixos.add("Pensamento, Expressao e Significado");
        eixos.add("Espaco, Cultura e Temporalidade");
        eixos.add("Ciencia, Tecnologia e Inovacao");
        eixos.add("Mais de um eixo");
        
        List<String> filteredEixos = new ArrayList<>();

        for (String e : eixos ) {
            if (e.toLowerCase().startsWith(query)) {
                filteredEixos.add(e);
            }
        }
        return filteredEixos;
        
    }
    
    //Curso-----------------------------------------------------------------------------------------------
    public List<String> completeCurso(String query){
        
        query = query.toLowerCase();
        
        List<String> cursos = new ArrayList<>();
        cursos.add("Bacharelado em Matemática");
        cursos.add("Bacharelado em Neurociência");
        cursos.add("Licenciatura em Matemática");
        cursos.add("Bacharelado em Políticas Públicas");
        cursos.add("Engenharia Aeroespacial");
        cursos.add("Engenharia Ambiental e Urbana");
        cursos.add("Engenharia de Automação e Robótica");
        cursos.add("Engenharia Biomédica");
        cursos.add("Engenharia de Energia");
        cursos.add("Engenharia de Gestão");
        cursos.add("Engenharia de Informação");
        cursos.add("Engenharia de Materiais");
        cursos.add("Bacharelado em Economia");
        cursos.add("Bacharelado em Relações Internacionais");
        cursos.add("Bacharelado em Planejamento Territorial");
        cursos.add("Ciências Biológicas");
        cursos.add("Filosofia");
        cursos.add("Física");
        cursos.add("Química");
        cursos.add("Licenciaturas");
        
        List<String> filteredCursos = new ArrayList<>();

        for (String c : cursos ) {
            if (c.toLowerCase().startsWith(query)) {
                filteredCursos.add(c);
            }
        }
        return filteredCursos;
        
    }

    //----------------------------------------------------------------------------------------------------

//    private boolean incluirRemovidas;
//
//    public boolean isIncluirRemovidas() {
//        return incluirRemovidas;
//    }
//
//    public void setIncluirRemovidas(boolean incluirRemovidas) {
//        this.incluirRemovidas = incluirRemovidas;
//    }
//    
//    
//    
//    private List<Afinidades> afinidadesPorDisciplina;
//
//    public List<Afinidades> getAfinidadesPorDisciplina() {
//        return afinidadesPorDisciplina;
//    }
//
//    public void setAfinidadesPorDisciplina(List<Afinidades> afinidadesPorDisciplina) {
//        this.afinidadesPorDisciplina = afinidadesPorDisciplina;
//    }
    

    
//--------------------------------------------Cadastro-------------------------------------------------------------------------------------------
    
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

                switch (sigla.charAt(3)) {

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
                    //Mudar
                    default:
                        return "";
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
                    //Mudar
                    default:
                        return "";
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
                    //Mudar
                    default:
                        return "";

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
                    d.setCodigo(palavras[1]);

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
        
        JsfUtil.addSuccessMessage("Cadastro de disciplinas realizado com sucesso", "");
   
    }
  
}
