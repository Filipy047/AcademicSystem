package ucan.edu.academic.dto;

import lombok.Getter;
import lombok.Setter;
import ucan.edu.academic.entities.MatriculaDisciplina;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CursoHistoricoDTO {
    private String nomeEstudante;
    private String nomeCurso;
    private String grauAcademico;
    private int duracao;
    private int cargaHorariaTotal;
    private List<MatriculaDisciplinaDTO> disciplinasCursadas;
    private List<MatriculaDisciplinaDTO> disciplinasConcluidas;

    public CursoHistoricoDTO(CursoHistorico cursoHistorico) {
        this.nomeEstudante = cursoHistorico.getNomeEstudante();
        this.nomeCurso = cursoHistorico.getNomeCurso();
        this.grauAcademico = cursoHistorico.getGrauAcademico();
        this.duracao = cursoHistorico.getDuracao();
        this.cargaHorariaTotal = cursoHistorico.getCargaHorariaTotal();

        this.disciplinasCursadas = new ArrayList<>();
        for (MatriculaDisciplina disciplina : cursoHistorico.getDisciplinasCursadas()) {
            this.disciplinasCursadas.add(new MatriculaDisciplinaDTO(disciplina));
        }

        this.disciplinasConcluidas = new ArrayList<>();
        for (MatriculaDisciplina disciplina : cursoHistorico.getDisciplinasConcluidas()) {
            this.disciplinasConcluidas.add(new MatriculaDisciplinaDTO(disciplina));
        }
    }

    //Getters e Setters
}

