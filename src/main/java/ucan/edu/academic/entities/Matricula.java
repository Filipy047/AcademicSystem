package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "matricula")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkMatricula;
    
    @ManyToOne
    @JoinColumn(name = "fk_estudante")
    private Estudante estudante;
    
    @ManyToOne
    @JoinColumn(name = "fk_curso")
    private Curso curso;

    private LocalDate dataMatricula;
    private Integer anoLetivo;
}