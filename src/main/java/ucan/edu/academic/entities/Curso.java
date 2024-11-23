package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkCurso;
    private String nomeCurso;
    private String descricao;
    private Integer duracao;
    private Integer cargaHorariaTotal;
    private String grauAcademico;

    public Curso(String nomeCurso, String descricao, Integer duracao, Integer cargaHorariaTotal, String grauAcademico) {
        this.nomeCurso = nomeCurso;
        this.descricao = descricao;
        this.duracao = duracao;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.grauAcademico = grauAcademico;
    }
}