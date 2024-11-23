package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ucan.edu.academic.entities.Disciplina;
import ucan.edu.academic.entities.DisciplinaPreRequisito;

import java.util.List;

public interface DisciplinaPreRequisitoRepository extends JpaRepository<DisciplinaPreRequisito, Integer> {
    @Query("SELECT dp FROM DisciplinaPreRequisito dp WHERE dp.disciplina.pkDisciplina = :disciplinaId")
    List<DisciplinaPreRequisito> findPreRequisitosByDisciplinaId(Integer disciplinaId);

    //existsByDisciplinaAndPreRequisito
    boolean existsByDisciplinaAndPreRequisito(Disciplina disciplina, Disciplina preRequisito);
    //Busca todos os prerequisitos de uma disciplina. retorna lista de ids
    List<DisciplinaPreRequisito> findDisciplinaPreRequisitosByDisciplina_PkDisciplina(Integer disciplinaId);
}
