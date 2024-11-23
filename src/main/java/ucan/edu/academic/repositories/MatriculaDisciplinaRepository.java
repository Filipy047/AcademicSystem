package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ucan.edu.academic.entities.MatriculaDisciplina;
import ucan.edu.academic.enums.StatusMatricula;

import java.util.List;
import java.util.Optional;

public interface MatriculaDisciplinaRepository extends JpaRepository<MatriculaDisciplina, Integer> {

    @Query("SELECT COUNT(md) FROM MatriculaDisciplina md " +
            "WHERE md.matricula.pkMatricula = :matriculaId " +
            "AND md.disciplina.pkDisciplina = :disciplinaId " +
            "AND md.semestre = :semestre")
    int countMatriculasDisciplina(Integer matriculaId, Integer disciplinaId, Integer semestre);

    @Query("SELECT md FROM MatriculaDisciplina md " +
            "WHERE md.matricula.pkMatricula = :matriculaId " +
            "AND md.disciplina.pkDisciplina IN :disciplinaIds " +
            "AND (md.nota >= 10 OR UPPER(md.status) = 'APROVADO')")
    List<MatriculaDisciplina> findApprovedPreRequisitosByNotaOrStatus(
            @Param("matriculaId") Integer matriculaId,
            @Param("disciplinaIds") List<Integer> disciplinaIds
    );

    @Query("SELECT COUNT(md) FROM MatriculaDisciplina md " +
            "WHERE md.matricula.pkMatricula = :matriculaId " +
            "AND md.disciplina.semestre < :periodo " +  // Alterado de :semestre para :periodo
            "AND (md.status = 'REPROVADO' OR md.status IS NULL)")
    int countPendingDisciplinasAnteriores(
            @Param("matriculaId") Integer matriculaId,
            @Param("periodo") Integer periodo
    );

    List<MatriculaDisciplina> findByMatricula_Estudante_PkEstudante(Integer estudanteId);

    List<MatriculaDisciplina> findByMatricula_Estudante_PkEstudanteAndStatus(Integer estudanteId, StatusMatricula statusMatricula);

    Optional<MatriculaDisciplina> findByMatricula_PkMatriculaAndDisciplina_PkDisciplina(Integer matriculaId, Integer disciplinaId);

}