package controller;

import facade.AfinidadesFacade;
import facade.DisciplinaFacade;
import facade.PessoaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Afinidades;
import model.Disciplina;
import model.Pessoa;

@Named(value = "disciplinaController2")
@SessionScoped
public class DisciplinaController2 implements Serializable {

    public DisciplinaController2() {
        disponivel = new Disciplina();
        escolhida = new Disciplina();
       

    }

    @EJB
    private DisciplinaFacade disciplinaFacade;

    @EJB
    private PessoaFacade pessoaFacade;
    
    @EJB
    private AfinidadesFacade afinidadesFacade;
    
    private List<Afinidades> afinidadesAtivas;
    
    private CadastroBean cadastro;

    //Guarda a disciplina disponivel selecionada
    private Disciplina disponivel;

    //Guarda a disciplina escolhida selecionada
    private Disciplina escolhida;

    private Pessoa pessoa;
    
    private Disciplina disciplina;

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    

    //Guarda as disciplinas disponiveis
    private List<Disciplina> disponiveis;

    //Guarda as disciplinas escolhidas
    private List<Disciplina> escolhidas;

    public Disciplina getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Disciplina disponivel) {
        this.disponivel = disponivel;
    }

    public Disciplina getEscolhida() {
        return escolhida;
    }

    public void setEscolhida(Disciplina escolhida) {
        this.escolhida = escolhida;
    }

    public List<Disciplina> getDisponiveis() {

        if (disponiveis == null) {
            
           
            pessoa = LoginBean.getUsuario();

            afinidadesAtivas = pessoa.getAfinidades();
            
            escolhidas = new ArrayList<>();
            
            for(Afinidades a: afinidadesAtivas){
                
                if(a.getEstado().equals("Adicionada")){
                    escolhidas.add(a.getDisciplina());
                }
                
            }

            disponiveis = disciplinaFacade.findAll();

            for (Disciplina e : escolhidas) {
                disponiveis.remove(e);
            }

        }

        return disponiveis;
    }

    public void setDisponiveis(List<Disciplina> disponiveis) {
        this.disponiveis = disponiveis;
    }

    public List<Disciplina> getEscolhidas() {
        return escolhidas;
    }

    public void setEscolhidas(List<Disciplina> escolhidas) {
        this.escolhidas = escolhidas;
    }

    public void salvarNoBanco() {
        

        
        Calendar cal = Calendar.getInstance();
        disponivel = disciplinaFacade.inicializarColecaoAfinidades(disponivel);
        Afinidades afinidade = new Afinidades("Adicionada", cal.getTime(), pessoa, disponivel);
        afinidadesFacade.merge(afinidade);
        disponiveis = null;
        disponivel = null;
    }
    
    public void removerDisciplina(){
        
        for(Afinidades a: afinidadesAtivas){
            
            if(a.getDisciplina() == escolhida){
                
                
                
                Calendar cal = Calendar.getInstance();
                a.setDataAcao(cal.getTime());
                a.setEstado("Removida");
                afinidadesFacade.edit(a);
            }
            
        }
        
        disponiveis = null;
        escolhida = null;
        disponivel = null;
        
        
        
        
//        try{
//            
//            
//            List<Disciplina> disciplinas = pessoa.getDisciplinas();
//            disciplinas.remove(escolhida);
//
//            pessoa.setDisciplinas(disciplinas);
//
//            pessoaFacade.edit(pessoa);
//            
//            
//            
//        }
//        catch(Exception e){
//            
//        }
//        
        
    }
    


    public Disciplina buscar(Long id) {

        return disciplinaFacade.find(id);
    }

    private List<Disciplina> listarTodas() {
        return disciplinaFacade.findAll();

    }
    
//AutoComplete----------------------------------------------------------------------------------------
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

    private boolean incluirRemovidas;

    public boolean isIncluirRemovidas() {
        return incluirRemovidas;
    }

    public void setIncluirRemovidas(boolean incluirRemovidas) {
        this.incluirRemovidas = incluirRemovidas;
    }
    
    
    
    private List<Afinidades> afinidadesPorDisciplina;

    public List<Afinidades> getAfinidadesPorDisciplina() {
        return afinidadesPorDisciplina;
    }

    public void setAfinidadesPorDisciplina(List<Afinidades> afinidadesPorDisciplina) {
        this.afinidadesPorDisciplina = afinidadesPorDisciplina;
    }
    
    
    
//    public List<Afinidades> encontrarAfinidadesDisciplina(){
//        
//        disciplina = disciplinaFacade.inicializarColecaoAfinidades(disciplina);
//        afinidadesPorDisciplina = disciplina.getAfinidades();
//        
//        if(!incluirRemovidas){
//            
//            for(Afinidades a : afinidadesPorDisciplina){
//                if(a.getEstado().equals("Removida")){
//                    afinidadesPorDisciplina.remove(a);
//                }
//            }
//        }
//
//        return afinidadesPorDisciplina;
//    }
    
//    private AfinidadesLazyModel afinidadesLazyModel;
//
//    public AfinidadesLazyModel getAfinidadesLazyModel() {
//        return afinidadesLazyModel;
//    }
//
//    public void setAfinidadesLazyModel(AfinidadesLazyModel afinidadesLazyModel) {
//        this.afinidadesLazyModel = afinidadesLazyModel;
//    }
    
    
    
//    public void povoarLazyModel() {
////        if(afinidadesLazyModel == null){
////            afinidadesLazyModel = new AfinidadesLazyModel(this.listarTodas());
////        }
//        
//        disciplina = disciplinaFacade.inicializarColecaoAfinidades(disciplina);
//        afinidadesPorDisciplina = disciplina.getAfinidades();
//        
//        if(!incluirRemovidas){
//            
//            for(Afinidades a : afinidadesPorDisciplina){
//                if(a.getEstado().equals("Removida")){
//                    afinidadesPorDisciplina.remove(a);
//                }
//            }
//        }
//        
//        afinidadesLazyModel = new AfinidadesLazyModel(afinidadesPorDisciplina);
//    }     
}
