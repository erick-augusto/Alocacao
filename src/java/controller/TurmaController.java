package controller;

import facade.DisciplinaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Disciplina;
import model.OfertaDisciplina;
import model.Turma;


@Named(value = "turmaController")
@SessionScoped
public class TurmaController implements Serializable {
    
    public TurmaController() {
        
    }

    @EJB
    private DisciplinaFacade disciplinaFacade;
    
    //Cadastro-------------------------------------------------------------------------------------------

    
    //Cadastrar oferta primeiro quadrimestre
    public void cadastrarTurmas(){
        
        String[] palavras;
        
        
            try {
          
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Juliana\\Documents\\NetBeansProjects\\alocacao\\Arquivos Alocação\\Arquivos CSV\\turmas.csv"), "UTF-8"))) {
                String linha = lerArq.readLine(); //cabeçalho
                
//                linha = lerArq.readLine();
                

//            linha = linha.replaceAll("\"", "");
                while (linha != null) {

                    linha = linha.replaceAll("\"", "");

                    palavras = linha.split(";", -1);
                    
                    Turma t = new Turma();
                    
                    String codigo = palavras[2];
                    String nome = palavras[4];
                    
                    Disciplina d = disciplinaFacade.findByCodOrName(codigo, nome);
                    
                    t.setDisciplina(d);
                    
                    palavras[18] = palavras[18].replaceAll(" ,", ",");
                    palavras[18] = palavras[18].replaceAll("\"", "");
                    String[] horarios = palavras[18]
                            .trim().split("(?<=semanal,)|(?<=quinzenal I)|(?<=quinzenal II)");
                    
                    for(String h: horarios){
                        
                        h = h.trim();
                        String[] partes = h.split("das|,");
                        System.out.println(partes[0]);
                        
                    }
                  
//                linha = linha.replaceAll("\"", "");
                }
            } //cabeçalho
                

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        
    }
 
}