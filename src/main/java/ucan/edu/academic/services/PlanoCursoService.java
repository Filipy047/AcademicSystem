package ucan.edu.academic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.dto.DisciplinaDTO;
import ucan.edu.academic.dto.PlanoCursoDTO;
import ucan.edu.academic.dto.PreRequisitoDTO;
import ucan.edu.academic.entities.Curso;
import ucan.edu.academic.entities.Disciplina;
import ucan.edu.academic.entities.DisciplinaPreRequisito;
import ucan.edu.academic.entities.PlanoCurso;
import ucan.edu.academic.repositories.CursoRepository;
import ucan.edu.academic.repositories.DisciplinaRepository;
import ucan.edu.academic.repositories.PlanoCursoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class PlanoCursoService {

    private final PlanoCursoRepository planoCursoRepository;
    private final CursoRepository cursoRepository;
    private final DisciplinaRepository disciplinaRepository;

    // Caches
    private HashMap<Integer, PlanoCurso> planoCursoByIdCache;
    private HashMap<Integer, List<PlanoCurso>> planoCursoByCursoIdCache;

    @Autowired
    public PlanoCursoService(PlanoCursoRepository planoCursoRepository,
                             CursoRepository cursoRepository,
                             DisciplinaRepository disciplinaRepository) {
        this.planoCursoRepository = planoCursoRepository;
        this.cursoRepository = cursoRepository;
        this.disciplinaRepository = disciplinaRepository;

        // Inicialização dos caches
        this.planoCursoByIdCache = new HashMap<>();
        this.planoCursoByCursoIdCache = new HashMap<>();
    }

    public void initialize() {
        if (planoCursoRepository.count() > 0) {
            System.out.println("Já existem planos de curso cadastrados.");
            return;
        }

        // Inicializa plano para Engenharia Informática
        initializeInformatica();

        // Inicializa plano para Telecomunicações
        initializeTelecomunicacoes();

        System.out.println("Planos de curso inicializados com sucesso.");
    }

    private void initializeInformatica() {
        Curso curso = cursoRepository.findByNomeCurso("Engenharia Informática")
                .orElseThrow(() -> new RuntimeException("Curso de Engenharia Informática não encontrado"));

        // Lista de disciplinas do 1º semestre
        String[] disciplinas1Semestre = {"AM101", "ALG101", "ENG101", "SD101", "FP101", "TFM101"};
        for (int i = 0; i < disciplinas1Semestre.length; i++) {
            addDisciplinaAoPlano(curso, disciplinas1Semestre[i], 1);
        }

        // Lista de disciplinas do 2º semestre
        String[] disciplinas2Semestre = {"AM102", "FP102", "SO101", "NET101", "CD101", "ENG102"};
        for (int i = 0; i < disciplinas2Semestre.length; i++) {
            addDisciplinaAoPlano(curso, disciplinas2Semestre[i], 2);
        }
    }

    private void initializeTelecomunicacoes() {
        Curso curso = cursoRepository.findByNomeCurso("Telecomunicações")
                .orElseThrow(() -> new RuntimeException("Curso de Telecomunicações não encontrado"));

        // Adiciona FP101 no 2º semestre
        addDisciplinaAoPlano(curso, "FP101", 2);
    }

    private void addDisciplinaAoPlano(Curso curso, String codigoDisciplina, int semestre) {
        Disciplina disciplina = disciplinaRepository.findByCodigoDisciplina(codigoDisciplina)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada: " + codigoDisciplina));

        if (!planoCursoRepository.existsByCurso_PkCursoAndDisciplina_PkDisciplina(
                curso.getPkCurso(), disciplina.getPkDisciplina())) {
            PlanoCurso planoCurso = new PlanoCurso(curso, disciplina, semestre);
            planoCursoRepository.save(planoCurso);
            // Atualiza cache após salvar o novo plano de curso
            addToCache(planoCurso);
            System.out.println("Disciplina " + codigoDisciplina + " adicionada ao plano de " + curso.getNomeCurso());
        }
    }

    // Método para adicionar planos de curso ao cache
    private void addToCache(PlanoCurso planoCurso) {
        planoCursoByIdCache.put(planoCurso.getPkPlanoCurso(), planoCurso);

        List<PlanoCurso> planosCurso = planoCursoByCursoIdCache.get(planoCurso.getCurso().getPkCurso());
        if (planosCurso == null) {
            planosCurso = new ArrayList<>();
            planoCursoByCursoIdCache.put(planoCurso.getCurso().getPkCurso(), planosCurso);
        }
        planosCurso.add(planoCurso);
    }

    public List<PlanoCurso> getPlanosByCurso(Integer cursoId) {
        // Retorna os planos de curso do cache se existirem, ou do banco de dados se não
        List<PlanoCurso> planosCurso = planoCursoByCursoIdCache.get(cursoId);
        if (planosCurso != null) {
            return planosCurso;
        } else {
            planosCurso = planoCursoRepository.findByCurso_PkCurso(cursoId);
            for (int i = 0; i < planosCurso.size(); i++) {
                addToCache(planosCurso.get(i)); // Atualiza cache
            }
            return planosCurso;
        }
    }

    public List<PlanoCurso> getPlanosByCursoESemestre(Integer cursoId, Integer semestre) {
        // Buscar do cache ou banco de dados
        List<PlanoCurso> planosCurso = planoCursoRepository.findByCurso_PkCursoAndSemestre(cursoId, semestre);
        for (int i = 0; i < planosCurso.size(); i++) {
            addToCache(planosCurso.get(i)); // Atualiza cache
        }
        return planosCurso;
    }

    public PlanoCursoDTO getPlanoCursoComDisciplinasEPreRequisitos(Integer cursoId) {
        // Obtém o curso e as disciplinas associadas
        List<PlanoCurso> planosCurso = getPlanosByCurso(cursoId);

        // Pega o nome do curso do primeiro plano (supondo que todas as disciplinas pertencem ao mesmo curso)
        String nomeCurso = "Curso não encontrado";
        if (!planosCurso.isEmpty()) {
            nomeCurso = planosCurso.get(0).getCurso().getNomeCurso();
        }

        // Lista para armazenar os DTOs das disciplinas
        List<DisciplinaDTO> disciplinasDTO = new ArrayList<>();

        for (PlanoCurso plano : planosCurso) {
            Disciplina disciplina = plano.getDisciplina();

            // Lista para armazenar os DTOs dos pré-requisitos
            List<PreRequisitoDTO> preRequisitosDTO = new ArrayList<>();

            // Iterar sobre o Set de pré-requisitos
            for (DisciplinaPreRequisito pr : disciplina.getPreRequisitos()) {
                PreRequisitoDTO preRequisitoDTO = new PreRequisitoDTO(pr.getPreRequisito().getPkDisciplina(), pr.getPreRequisito().getNomeDisciplina());
                preRequisitosDTO.add(preRequisitoDTO);
            }

            // Cria o DTO da disciplina com os pré-requisitos e semestre
            DisciplinaDTO disciplinaDTO = new DisciplinaDTO(disciplina.getPkDisciplina(), disciplina.getNomeDisciplina(), plano.getSemestre(), preRequisitosDTO);
            disciplinasDTO.add(disciplinaDTO);
        }

        // Retorna o PlanoCursoDTO com o nome do curso e as disciplinas associadas
        return new PlanoCursoDTO(nomeCurso, disciplinasDTO);
    }

    public PlanoCurso savePlanoCurso(PlanoCurso planoCurso) {
        PlanoCurso planoCursoSalvo = planoCursoRepository.save(planoCurso);
        addToCache(planoCursoSalvo); // Atualiza cache
        return planoCursoSalvo;
    }

    public void deletePlanoCurso(Integer id) {
        PlanoCurso planoCurso = planoCursoByIdCache.get(id);
        if (planoCurso != null) {
            planoCursoRepository.deleteById(id);
            // Remove da cache após exclusão
            planoCursoByIdCache.remove(id);
            List<PlanoCurso> planosCurso = planoCursoByCursoIdCache.get(planoCurso.getCurso().getPkCurso());
            if (planosCurso != null) {
                for (int i = 0; i < planosCurso.size(); i++) {
                    if (planosCurso.get(i).getPkPlanoCurso().equals(id)) {
                        planosCurso.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public List<PlanoCurso> findAll() {
        if (planoCursoByIdCache.isEmpty()) {
            // Se o cache estiver vazio, busca no banco de dados e atualiza o cache
            List<PlanoCurso> planosCurso = planoCursoRepository.findAll();
            for (PlanoCurso planoCurso : planosCurso) {
                addToCache(planoCurso);
            }
            return planosCurso;
        } else {
            // Retorna do cache
            return new ArrayList<>(planoCursoByIdCache.values());
        }
    }

    public PlanoCurso findById(Integer id) {
        // Verifica no cache
        PlanoCurso planoCurso = planoCursoByIdCache.get(id);

        if (planoCurso == null) {
            // Se não estiver no cache, busca no banco de dados
            planoCurso = planoCursoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Plano de curso não encontrado"));

            // Atualiza o cache
            addToCache(planoCurso);
        }

        return planoCurso;
    }

}


  /*public List<Disciplina> getDisciplinasDisponiveis(Long cursoId, Set<Long> disciplinasConcluidas) {
        // Busca todas as disciplinas do curso
        List<PlanoCurso> todasDisciplinas = planoCursoRepository.findByCurso_PkCurso(cursoId);

        // Lista para armazenar as disciplinas disponíveis
        List<Disciplina> disciplinasDisponiveis = new ArrayList<>();

        for (PlanoCurso plano : todasDisciplinas) {
            Disciplina disciplina = plano.getDisciplina();

            // Verifica se a disciplina já foi concluída
            if (disciplinasConcluidas.contains(disciplina.getPkDisciplina())) {
                continue;
            }

            // Busca os pré-requisitos da disciplina
            Set<DisciplinaPreRequisito> preRequisitos = disciplina.getPreRequisitos();

            // Verifica se todos os pré-requisitos foram cumpridos
            boolean preRequisitosAtendidos = true;
            for (DisciplinaPreRequisito preRequisito : preRequisitos) {
                if (!disciplinasConcluidas.contains(preRequisito.getPreRequisito().getPkDisciplina())) {
                    preRequisitosAtendidos = false;
                    break;
                }
            }

            if (preRequisitosAtendidos) {
                disciplinasDisponiveis.add(disciplina);
            }
        }
        return disciplinasDisponiveis;
    }*/ //uso futuro