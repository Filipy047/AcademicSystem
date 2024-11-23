package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.*;
import ucan.edu.academic.enums.StatusMatricula;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "matricula_disciplina")
public class MatriculaDisciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkMatriculaDisciplina;
    
    @ManyToOne
    @JoinColumn(name = "fk_matricula")
    private Matricula matricula;
    
    @ManyToOne
    @JoinColumn(name = "fk_disciplina")
    private Disciplina disciplina;

    @Enumerated(EnumType.STRING)
    private StatusMatricula status;

    private Double nota;
    private Integer semestre;

    //atualizar status baseado na nota
    public void atualizarStatus() {
        if (this.nota == null) {
            this.status = StatusMatricula.EM_CURSO;
        } else if (this.nota >= 10.0) {
            this.status = StatusMatricula.APROVADO;
        } else {
            this.status = StatusMatricula.REPROVADO;
        }
    }

    // setter para nota que atualiza o status automaticamente
    public void setNota(Double nota) {
        this.nota = nota;
        atualizarStatus();
    }
}