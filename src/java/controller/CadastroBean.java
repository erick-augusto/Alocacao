package controller;

import facade.DisciplinaFacade;
import facade.PessoaFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Disciplina;
import model.Pessoa;
import util.DisciplinaLazyModel;

@Named(value = "cadastroBean")
@SessionScoped
public class CadastroBean implements Serializable {

    public CadastroBean() {

    }

    @EJB
    private PessoaFacade pessoaFacade;

    @EJB
    private DisciplinaFacade disciplinaFacade;

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

    public void cadastrarPessoas() {

        String[] palavras;

        try {

            FileReader arq = new FileReader("/home/charles/NetBeansProjects/Arquivos CSV/docentes.csv");

            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); //cabeçalho
            // lê a primeira linha 
            // a variável "linha" recebe o valor "null" quando o processo 
            // de repetição atingir o final do arquivo texto 

            linha = lerArq.readLine();

            while (linha != null) {
//                linha = linha.replace("\"", "");

                palavras = linha.split(",");

                Pessoa p = new Pessoa();

                p.setNome(palavras[1]);
                p.setSiape(palavras[2]);
                p.setEmail(palavras[3]);
                p.setCentro(palavras[4]);

                pessoaFacade.save(p);

                linha = lerArq.readLine();
            }

            arq.close();

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        Pessoa p = new Pessoa();

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
        
      

    }

}
