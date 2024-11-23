package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucan.edu.academic.entities.Matricula;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    boolean existsByEstudante_PkEstudanteAndCurso_PkCurso(Long estudanteId, Integer cursoId);

   boolean existsByEstudante_PkEstudante(Integer estudanteId);

    Optional<Matricula> findByEstudante_PkEstudante(Integer estudanteId);
}
