package ucan.edu.academic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DisciplinaDTO {
    private int idDisciplina;
    private String nomeDisciplina;
    private Integer semestre;
    private List<PreRequisitoDTO> preRequisitos;

    public DisciplinaDTO(int idDisciplina, String nomeDisciplina,  Integer semestre,List<PreRequisitoDTO> preRequisitos) {
        this.idDisciplina = idDisciplina;
        this.nomeDisciplina = nomeDisciplina;
        this.semestre = semestre;
        this.preRequisitos = preRequisitos;
    }

    // Getters e Setters
}