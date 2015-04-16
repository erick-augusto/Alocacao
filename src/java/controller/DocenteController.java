package controller;

import facade.DocenteFacade;
import facade.PessoaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Docente;
import model.Pessoa;
import util.PessoaLazyModel;

@Named(value = "docenteController")
@SessionScoped
public class DocenteController implements Serializable{
    
    public DocenteController(){
        
    }
    
    //Docente atual
    private Docente docente;
    
    //Docente para salvar
    private Docente docenteSalvar;
    
    @EJB
    private DocenteFacade docenteFacade;
    
    @EJB
    private PessoaFacade pessoaFacade;
    
    //----------------------------------------Getters e Setters----------------------------------------------------

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Docente getDocenteSalvar() {
        
        if(docenteSalvar == null){
            docenteSalvar = new Docente();
        }
        
        return docenteSalvar;
    }

    public void setDocenteSalvar(Docente docenteSalvar) {
        this.docenteSalvar = docenteSalvar;
    }
    
    //-----------------------------------------LazyDataModel------------------------------------------------------
    
    private PessoaLazyModel docenteLazyModel;

    public PessoaLazyModel getDocenteLazyModel() {
        
        if(docenteLazyModel == null){
            docenteLazyModel = new PessoaLazyModel(pessoaFacade.listDocentes());
        }
        
        return docenteLazyModel;
    }
    
    @PostConstruct
    public void init() {
        docenteLazyModel = new PessoaLazyModel(pessoaFacade.listDocentes());
    }
    
    
    //------------------------------------------CRUD---------------------------------------------------------------------------------------------
    
    public Docente buscar(Long id) {

        return docenteFacade.find(id);
    }
    
    public void salvar(){
        try {
            docenteFacade.save(docenteSalvar);
            JsfUtil.addSuccessMessage("Docente " + docenteSalvar.getNome() + " cadastrado com sucesso!");
            docenteSalvar = null;
            docenteLazyModel = null;
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Não foi possível cadastrar o docente");
        }
    }

    public void editar() {
        try {
            docenteFacade.edit(docente);
            JsfUtil.addSuccessMessage("Docente editado com sucesso!");
            docente = null;
            docenteLazyModel = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar o docente: " + e.getMessage());

        }
    }

    public void delete() {
        docente = (Docente) docenteLazyModel.getRowData();
        try {
            docenteFacade.remove(docente);
            docente = null;
            JsfUtil.addSuccessMessage("Docente Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }

        docenteLazyModel = null;
    }
    
    //----------------------------------------Páginas web------------------------------------------------------
    
    public String prepareEdit() {
        docente = (Docente) (Pessoa) docenteLazyModel.getRowData();
        return "/Cadastro/editDocente";
    }
    
    //------------------------------------------Cadastro-------------------------------------------------------------------------------------------
   
    public void cadastrarDocentes() {

        String[] palavras;

        try {

            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("/home/charles/NetBeansProjects/Arquivos CSV/docentes.csv"), "UTF-8"))) {
                
                String linha = lerArq.readLine(); //cabeçalho
                
                linha = lerArq.readLine();
                
                while (linha != null) {
                    
                    linha = linha.replaceAll("\"", "");
                    
                    palavras = linha.split(",");
                    
                    List<Docente> docentes = docenteFacade.findByName(trataNome(palavras[1]));
                    
                    if (docentes.isEmpty()) {
                        
                        Docente d = new Docente();
                        
                        d.setNome(trataNome(palavras[1]));
                        d.setSiape(palavras[2]);
                        d.setEmail(palavras[3]);
                        d.setCentro(palavras[4]);
                        d.setAdm(false);
                        
                        docenteFacade.save(d);
                        
                    }
                    
                    linha = lerArq.readLine();
                }
            } 

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        
        docenteLazyModel = null;
        JsfUtil.addSuccessMessage("Cadastro de docentes realizado com sucesso", "");

    }
  
    private String trataNome(String nome) { 
        
     String retorno = "";
     String[] palavras = nome.split(" ");
     
     for(String p: palavras){
         
         if(p.equals("DAS") || p.equals("DOS") || p.length() <= 2){
             p = p.toLowerCase();
             retorno += p + " ";
         }
        
         
         else{
             p = p.charAt(0) + p.substring(1, p.length()).toLowerCase();
             retorno += p + " ";
         }
         
     }
        
return retorno;

} 
   
}
