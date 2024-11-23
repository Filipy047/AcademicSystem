package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plano_curso")
@Getter
@Setter
@NoArgsConstructor
public class PlanoCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkPlanoCurso;
    
    @ManyToOne
    @JoinColumn(name = "fk_curso")
    private Curso curso;
    
    @ManyToOne
    @JoinColumn(name = "fk_disciplina")
    private Disciplina disciplina;
    
    private Integer semestre;

    public PlanoCurso(Curso curso, Disciplina disciplina, Integer semestre) {
        this.curso = curso;
        this.disciplina = disciplina;
        this.semestre = semestre;
    }
}