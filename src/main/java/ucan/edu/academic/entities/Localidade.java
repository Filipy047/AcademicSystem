package ucan.edu.academic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Localidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pkLocalidade;
    private String designacao;

    @ManyToOne
    @JoinColumn(name = "fk_localidade_pai")
    private Localidade fkLocalidadePai;

    @OneToMany
    @JsonIgnore
    private List<Localidade> localidadeList;

    public Localidade(String designacao) {
        this.designacao = designacao;
    }


    //toString
    public String toString() {
        return this.designacao;
    }
}
