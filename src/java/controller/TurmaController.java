package controller;

import facade.DisciplinaFacade;
import facade.TurmaFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Disciplina;
import model.Horario;
import model.Turma;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import util.TurmaDataModel;


@Named(value = "turmaController")
@SessionScoped
public class TurmaController implements Serializable {
    
    public TurmaController() {
        
    }

    @EJB
    private DisciplinaFacade disciplinaFacade;
    
    @EJB
    private TurmaFacade turmaFacade;
    
    //Cadastro-------------------------------------------------------------------------------------------

    
    public void cadastrarTurmas(){
        
        String[] palavras;
        
        
            try {
          
            try (BufferedReader lerArq = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Juliana\\Documents\\NetBeansProjects\\alocacao\\Arquivos Alocação\\Arquivos CSV\\turmas.csv"), "UTF-8"))) {
                String linha = lerArq.readLine(); 
                
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
                    String[] horariosCompletos = palavras[18]
                            .trim().split("(?<=semanal,)|(?<=quinzenal I)|(?<=quinzenal II)");
                    
                    List<Horario> horarios = new ArrayList<Horario>();
                    
                    for(String horarioCompleto: horariosCompletos){
                        
                        horarioCompleto = horarioCompleto.trim();
                        String[] partes = horarioCompleto.split("das|,");
                        Horario h = new Horario();
                        h.setDia(partes[0]);
                        h.setHora(partes[1]);
                        h.setSala(partes[2]);
                        h.setPeriodicidade(partes[3]);
                        
                        horarios.add(h);
                 
                    }
                    
                    t.setHorarios(horarios);
                    turmaFacade.save(t);
                    
                    linha = lerArq.readLine();
                  
//                linha = linha.replaceAll("\"", "");
                }
            } //cabeçalho
                

            } catch (IOException e) {
                System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            }
        
    }
    
    //Fase II Disponibilidade -------------------------------------------------------------------------------
    
    private TurmaDataModel turmaDataModel;

    public TurmaDataModel getTurmaDataModel() {
        
        if(turmaDataModel == null){
            
            List<Turma> turmas = turmaFacade.findAll();
            turmaDataModel = new TurmaDataModel(turmas);
            
        }
        
        return turmaDataModel;
    }

    public void setTurmaDataModel(TurmaDataModel turmaDataModel) {
        this.turmaDataModel = turmaDataModel;
    }
    
    //Turma que sera selecionada para visualizacao
    private Turma selectedTurma;

    public Turma getSelectedTurma() {
        return selectedTurma;
    }

    public void setSelectedTurma(Turma selectedTurma) {
        this.selectedTurma = selectedTurma;
    }
    
    public void onRowSelect(SelectEvent event) {
       
        turmasSchedule.clear();
        Turma t = (Turma) event.getObject();
        preencherTurma(t);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        
        
                
        
    }
    
    //Mostrara as turmas da disciplina selecionada
    private ScheduleModel turmasSchedule;
    
    //Mostrara as turmas já selecionadas pelo docente
    private ScheduleModel docenteSchedule;

    public ScheduleModel getTurmasSchedule() {
        return turmasSchedule;
    }

    public void setTurmasSchedule(ScheduleModel turmasSchedule) {
        this.turmasSchedule = turmasSchedule;
    }

    public ScheduleModel getDocenteSchedule() {
        return docenteSchedule;
    }

    public void setDocenteSchedule(ScheduleModel docenteSchedule) {
        this.docenteSchedule = docenteSchedule;
    }
    
    @PostConstruct
    public void init() {
        turmasSchedule = new DefaultScheduleModel();
        
        
//        Calendar i = Calendar.getInstance();
//        i.set(Calendar.AM_PM, Calendar.PM);
//        i.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
//        i.set(Calendar.HOUR, 5);
//        i.set(Calendar.MINUTE, 0);
//        
//        Calendar e = Calendar.getInstance();
//        i.set(Calendar.AM_PM, Calendar.PM);
//        i.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
//        i.set(Calendar.HOUR, 7);
//        i.set(Calendar.MINUTE, 0);
//        turmasSchedule.addEvent(new DefaultScheduleEvent("Turma A", i.getTime(), e.getTime()));
       
    }
    
    public void preencherTurma(Turma turma){
        
        List<Horario> horarios = turma.getHorarios();
        
        for(Horario h: horarios){
            
            int dia = conversorDia(h.getDia());
            
            String hora = h.getHora().trim();
            int horaInicio = Integer.parseInt(hora.substring(0, 2));
            int minutoInicio = Integer.parseInt(hora.substring(3,5));
            
            int horaFim = Integer.parseInt(hora.substring(9, 11));
            int minutoFim = Integer.parseInt(hora.substring(12,14));        
            
            //Inicio do horario
            Calendar inicio = Calendar.getInstance();
            inicio.set(Calendar.DAY_OF_WEEK, dia);
            if(horaInicio > 12){
                inicio.set(Calendar.AM_PM, Calendar.PM);
                inicio.set(Calendar.HOUR, horaInicio - 12);
                
            }
            else{
                inicio.set(Calendar.AM_PM, Calendar.AM);
                inicio.set(Calendar.HOUR, horaInicio);
            }
            inicio.set(Calendar.MINUTE, minutoInicio);
            
            //Fim do horario
            Calendar fim = Calendar.getInstance();
            fim.set(Calendar.DAY_OF_WEEK, dia);
            if(horaFim > 12){
                fim.set(Calendar.AM_PM, Calendar.PM);
                fim.set(Calendar.HOUR, horaFim - 12);
                
            }
            else{
                fim.set(Calendar.AM_PM, Calendar.AM);
                fim.set(Calendar.HOUR, horaFim);
            }
            fim.set(Calendar.MINUTE, minutoFim);
            
            turmasSchedule.addEvent(new DefaultScheduleEvent("Turma x", inicio.getTime(), fim.getTime()));
            
            
            
        }
             
    }
    
    private int conversorDia(String dia){
        
        dia = dia.trim();
        
        switch(dia){
            case "segunda":
                return Calendar.MONDAY;
            case "terca":
                return Calendar.TUESDAY;
            case "quarta":
                return Calendar.WEDNESDAY;
            case "quinta":
                return Calendar.THURSDAY;
            case "sexta":
                return Calendar.FRIDAY;
            case "sabado":
                return Calendar.SATURDAY;
        }
        
        return 0;
    }
    
    
    
 
}