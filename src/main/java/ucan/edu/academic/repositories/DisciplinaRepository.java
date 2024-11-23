package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.edu.academic.entities.Disciplina;

import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
    boolean existsByCodigoDisciplina(String codigoDisciplina);

    Optional<Disciplina> findByCodigoDisciplina(String codigoDisciplina);
}