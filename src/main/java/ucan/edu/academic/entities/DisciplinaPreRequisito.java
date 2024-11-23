package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "disciplina_pre_requisito")
public class DisciplinaPreRequisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkDisciplinaPreRequisito;

    @ManyToOne
    @JoinColumn(name = "fk_disciplina")
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "fk_pre_requisito")
    private Disciplina preRequisito;

    public DisciplinaPreRequisito(Disciplina disciplina, Disciplina preRequisito) {
        this.disciplina = disciplina;
        this.preRequisito = preRequisito;
    }
}