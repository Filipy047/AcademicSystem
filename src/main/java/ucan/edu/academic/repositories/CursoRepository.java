package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.edu.academic.entities.Curso;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    boolean existsByNomeCurso(String nomeCurso);

    Optional<Curso> findByNomeCurso(String nomeCurso);
}