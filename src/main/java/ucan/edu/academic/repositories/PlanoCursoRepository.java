package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucan.edu.academic.entities.Curso;
import ucan.edu.academic.entities.Disciplina;
import ucan.edu.academic.entities.PlanoCurso;

import java.util.List;

@Repository
public interface PlanoCursoRepository extends JpaRepository<PlanoCurso, Integer> {
    List<PlanoCurso> findByCurso_PkCurso(Integer cursoId);
    List<PlanoCurso> findByCurso_PkCursoAndSemestre(Integer cursoId, Integer semestre);
    //Optional<PlanoCurso> findByCursoIdAndDisciplinaId(Long cursoId, Long disciplinaId);

    boolean existsByCurso_PkCursoAndDisciplina_PkDisciplina(Integer cursoId, int disciplinaId);

    //boolean existsByCursoAndDisciplinaAndSemestre(Curso curso, Disciplina disciplina, Integer semestre);
    boolean existsByCurso_PkCursoAndDisciplina_PkDisciplinaAndSemestre(Integer cursoId, Integer disciplinaId, Integer semestre);

}