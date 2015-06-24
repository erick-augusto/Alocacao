package controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Disciplina;
import model.OfertaDisciplina;


@Named(value = "turmaController")
@SessionScoped
public class TurmaController implements Serializable {
    
    public TurmaController() {
        
    }

    
    //Cadastro-------------------------------------------------------------------------------------------

    
    //Cadastrar oferta primeiro quadrimestre
    public void cadastrarTurmas(){
        
        String[] palavras;
        
        //Primeiro quadrimestre
            try {
          
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Juliana\\Documents\\NetBeansProjects\\alocacao\\Arquivos Alocação\\Arquivos CSV\\turmas.csv"), "UTF-8"))) {
                String linha = lerArq.readLine(); //cabeçalho
                
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
            } //cabeçalho
                ofertas1LazyModel = null;

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        
    }
 
}