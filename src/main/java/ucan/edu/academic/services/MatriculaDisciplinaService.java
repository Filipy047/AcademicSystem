package ucan.edu.academic.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.entities.*;
import ucan.edu.academic.enums.StatusMatricula;
import ucan.edu.academic.repositories.DisciplinaPreRequisitoRepository;
import ucan.edu.academic.repositories.MatriculaDisciplinaRepository;
import ucan.edu.academic.repositories.MatriculaRepository;
import ucan.edu.academic.repositories.PlanoCursoRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class MatriculaDisciplinaService {
    @Autowired
    private MatriculaDisciplinaRepository matriculaDisciplinaRepository;

    @Autowired
    private DisciplinaPreRequisitoRepository disciplinaPreRequisitoRepository;

    @Autowired
    private PlanoCursoRepository planoCursoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;


    @Transactional
    public MatriculaDisciplina matricularDisciplina(MatriculaDisciplina matriculaDisciplina) {
        // 1. Verificar se a disciplina pertence ao plano de curso do aluno
        verificarPlanoCurso(matriculaDisciplina);

        // 2. Verificar se já está matriculado na disciplina
        verificarMatriculaDuplicada(matriculaDisciplina);

        // 3. Verificar se tem disciplinas pendentes de períodos anteriores
        verificarDisciplinasPendentes(matriculaDisciplina);

        // 4. Verificar pré-requisitos
        verificarPreRequisitos(matriculaDisciplina);

        // Definir status inicial
        matriculaDisciplina.setStatus(StatusMatricula.EM_CURSO);

        return matriculaDisciplinaRepository.save(matriculaDisciplina);
    }

    @Transactional
    public MatriculaDisciplina atualizarNota(int id, Double nota) {
        MatriculaDisciplina matricula = matriculaDisciplinaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        matricula.setNota(nota);
        return matriculaDisciplinaRepository.save(matricula);
    }

    //actualizar nota by fk_matricula and id_disciplina
    @Transactional
    public MatriculaDisciplina atualizarNota(Integer idMatricula, Integer idDisciplina, Double nota) {
        MatriculaDisciplina matricula = matriculaDisciplinaRepository
                .findByMatricula_PkMatriculaAndDisciplina_PkDisciplina(idMatricula, idDisciplina)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        matricula.setNota(nota);
        return matriculaDisciplinaRepository.save(matricula);
    }



    private void verificarPlanoCurso(MatriculaDisciplina matriculaDisciplina) {

        System.out.println("Iniciando matrícula a disciplina...");

        Matricula matricula = matriculaRepository.findById(matriculaDisciplina.getMatricula().getPkMatricula())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));

        int idcurso = matricula.getCurso().getPkCurso();

        // Verificar se existe um plano de curso para esta combinação
        boolean existePlanoCurso = planoCursoRepository.existsByCurso_PkCursoAndDisciplina_PkDisciplinaAndSemestre(
                idcurso,
                matriculaDisciplina.getDisciplina().getPkDisciplina(),
                matriculaDisciplina.getSemestre()
        );

        if (!existePlanoCurso) {
            throw new RuntimeException(
                    "A disciplina não faz parte do plano de curso deste curso no semestre informado. " +
                            "Matrícula não permitida.\n"
            );
        }
    }

    private void verificarMatriculaDuplicada(MatriculaDisciplina matriculaDisciplina) {
        int count = matriculaDisciplinaRepository.countMatriculasDisciplina(
                matriculaDisciplina.getMatricula().getPkMatricula(),
                matriculaDisciplina.getDisciplina().getPkDisciplina(),
                matriculaDisciplina.getSemestre()
        );

        if (count > 0) {
            throw new RuntimeException("Estudante já matriculado nesta disciplina neste período");
        }
    }

    private void verificarDisciplinasPendentes(MatriculaDisciplina matriculaDisciplina) {
        Integer semestreAtual = matriculaDisciplina.getDisciplina().getSemestre();
        Integer matriculaId = matriculaDisciplina.getMatricula().getPkMatricula();

        Integer disciplinasPendentes = matriculaDisciplinaRepository
                .countPendingDisciplinasAnteriores(matriculaId, semestreAtual);

        if (disciplinasPendentes > 0) {
            throw new RuntimeException(
                    "Existem disciplinas pendentes de períodos anteriores. " +
                            "Não é possível se matricular em disciplinas avançadas."
            );
        }
    }


    private void verificarPreRequisitos(MatriculaDisciplina matriculaDisciplina) {
        // Obtém os pré-requisitos da disciplina que o aluno deseja cursar
        List<DisciplinaPreRequisito> preRequisitos = disciplinaPreRequisitoRepository
                .findDisciplinaPreRequisitosByDisciplina_PkDisciplina(matriculaDisciplina.getDisciplina().getPkDisciplina());

        // Se não houver pré-requisitos, a verificação é concluída com sucesso
        if (preRequisitos.isEmpty()) {
            return;
        }

        // Cria uma lista para armazenar os IDs dos pré-requisitos
        List<Integer> preRequisitosIds = new ArrayList<>();
        for (DisciplinaPreRequisito preRequisito : preRequisitos) {
            preRequisitosIds.add(preRequisito.getPreRequisito().getPkDisciplina());
        }

        // Busca os pré-requisitos que foram aprovados (considerando tanto nota quanto status)
        List<MatriculaDisciplina> preRequisitosAprovados =
                matriculaDisciplinaRepository.findApprovedPreRequisitosByNotaOrStatus(
                        matriculaDisciplina.getMatricula().getPkMatricula(),
                        preRequisitosIds
                );

        // Verifica se o aluno cumpriu todos os pré-requisitos
        if (preRequisitosAprovados.size() != preRequisitosIds.size()) {
            throw new RuntimeException(
                    "Aluno não atende aos pré-requisitos necessários para cursar esta disciplina"
            );
        }
    }
}

