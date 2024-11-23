package ucan.edu.academic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlunoCursoDTO {
    private String nomeAluno;
    private String nomeCurso;
    private List<MatriculaDisciplinaDTO> disciplinas;

    public AlunoCursoDTO(String nomeAluno, String nomeCurso, List<MatriculaDisciplinaDTO> disciplinas) {
        this.nomeAluno = nomeAluno;
        this.nomeCurso = nomeCurso;
        this.disciplinas = disciplinas;
    }
    // Getters e Setters
}