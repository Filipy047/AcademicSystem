package ucan.edu.academic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PreRequisitoDTO {
    private int idDisciplina;
    private String nomeDisciplina;

    public PreRequisitoDTO(int idDisciplina, String nomeDisciplina) {
        this.idDisciplina = idDisciplina;
        this.nomeDisciplina = nomeDisciplina;
    }

    // Getters e Setters
}