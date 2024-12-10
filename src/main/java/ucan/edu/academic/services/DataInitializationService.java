package ucan.edu.academic.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.entities.InitializationFlag;
import ucan.edu.academic.repositories.InitializationFlagRepository;

@Service
public class DataInitializationService {

    private final CursoService cursoService;
    private final EstudanteService estudanteService;
    private final DisciplinaService disciplinaService;
    private final PlanoCursoService planoCursoService;
    private final LocalidadeService localidadeService;
    private final InitializationFlagRepository initializationFlagRepository;

    @Autowired
    public DataInitializationService(InitializationFlagRepository initializationFlagRepository,
                                     CursoService cursoService, EstudanteService estudanteService,
                                     DisciplinaService disciplinaService, PlanoCursoService planoCursoService, LocalidadeService localidadeService) {
        this.initializationFlagRepository = initializationFlagRepository;
        this.cursoService = cursoService;
        this.estudanteService = estudanteService;
        this.disciplinaService = disciplinaService;
        this.planoCursoService = planoCursoService;
        this.localidadeService = localidadeService;
    }

    @PostConstruct
    public void initData() {
        // Verificar se os dados já foram inicializados
        boolean alreadyInitialized = initializationFlagRepository.findById(1L).isPresent();
        if (alreadyInitialized) {
            System.out.println("Dados já foram inicializados anteriormente.");
            return;
        }

        // Inicializar cursos e disciplinas assíncronamente
        try {
            cursoService.init();
            disciplinaService.init();
            localidadeService.init();
            estudanteService.initializeEstudantes();
        } catch (Exception e) {
            System.err.println("Erro durante inicialização: " + e.getMessage());
            e.printStackTrace();
        }


        // Esperar pelas inicializações de cursos e disciplinas
        try {
            cursoService.awaitInitialization();
            disciplinaService.awaitInitialization();
            localidadeService.awaitInitialization();
        } catch (InterruptedException e) {
            System.err.println("Erro ao aguardar a inicialização dos dados: " + e.getMessage());
        }

        planoCursoService.initialize();

        // Marcar como inicializado
        initializationFlagRepository.save(new InitializationFlag(1L, true));
        System.out.println("Inicialização concluída com sucesso.");
    }
}
