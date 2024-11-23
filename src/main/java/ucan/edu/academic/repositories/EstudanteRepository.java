package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.entities.Localidade;

import java.util.List;

public interface EstudanteRepository extends JpaRepository<Estudante, Integer> {
    boolean existsByNumDocumento(String numDocumento);

    List<Estudante> findEstudanteByLocalResidencia_PkLocalidade(int pkLocalidade);
    List<Estudante> findByLocalResidencia(Localidade localidade);
}