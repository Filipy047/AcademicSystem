package ucan.edu.academic.dto;

import lombok.Getter;
import lombok.Setter;
import ucan.edu.academic.entities.MatriculaDisciplina;
import ucan.edu.academic.enums.StatusMatricula;

@Getter
@Setter
public class MatriculaDisciplinaDTO {
    private Integer id;
    private String nomeDisciplina;
    private double notaFinal;
    private String status;

    public MatriculaDisciplinaDTO(MatriculaDisciplina matriculaDisciplina) {
        this.id = matriculaDisciplina.getPkMatriculaDisciplina();
        this.nomeDisciplina = matriculaDisciplina.getDisciplina().getNomeDisciplina();

        this.notaFinal = matriculaDisciplina.getNota() != null ? matriculaDisciplina.getNota() : 0.0;

        this.status = matriculaDisciplina.getNota() == null
                ? StatusMatricula.EM_CURSO.name()
                : matriculaDisciplina.getStatus().name();
    }

    // Getters e Setters
}

