package ucan.edu.academic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PlanoCursoDTO {
    private String nomeCurso;
    private List<DisciplinaDTO> disciplinas;

    public PlanoCursoDTO(String nomeCurso, List<DisciplinaDTO> disciplinas) {
        this.nomeCurso = nomeCurso;
        this.disciplinas = disciplinas;
    }

    // Getters e Setters
}
