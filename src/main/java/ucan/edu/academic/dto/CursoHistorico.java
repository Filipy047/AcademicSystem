package ucan.edu.academic.dto;

import lombok.*;
import ucan.edu.academic.entities.MatriculaDisciplina;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CursoHistorico {
    private String nomeEstudante;
    private String nomeCurso;
    private String grauAcademico;
    private Integer duracao;
    private Integer cargaHorariaTotal;
    private List<MatriculaDisciplina> disciplinasCursadas;
    private List<MatriculaDisciplina> disciplinasConcluidas;

    // Getters, setters e construtor
}