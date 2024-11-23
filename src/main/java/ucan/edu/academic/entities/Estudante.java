package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "estudante")
public class Estudante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkEstudante;
    private String nomeCompleto;
    private LocalDate dataDeNascimento;
    private String genero;
    private String numDocumento;
    private String email;
    private String telefone;

    private LocalDate dataDeAdmissao;

    @ManyToOne
    @JoinColumn(name = "fk_local_trabalho")
    private Localidade localTrabalho;

    @ManyToOne
    @JoinColumn(name = "fk_local_residencia")
    private Localidade localResidencia;

    @ManyToMany
    @JoinTable(
            name = "estudante_desporto",
            joinColumns = @JoinColumn(name = "fk_estudante"),
            inverseJoinColumns = @JoinColumn(name = "fk_desporto")
    )
    private List<Desporto> desportosPracticados;

    public Estudante(String nomeCompleto, LocalDate dataDeNascimento, String genero,
                     String numDocumento, String email, String telefone,
                     LocalDate dataDeAdmissao, /*Localidade localTrabalho, Localidade localResidencia,*/  List<Desporto>  desportos) {
        this.nomeCompleto = nomeCompleto;
        this.dataDeNascimento = dataDeNascimento;
        this.genero = genero;
        this.numDocumento = numDocumento;
        this.email = email;
        this.telefone = telefone;
        this.dataDeAdmissao = dataDeAdmissao;
        /*this.localTrabalho = localTrabalho;
        this.localResidencia = localResidencia;*/
        this.desportosPracticados = desportos;
    }
}