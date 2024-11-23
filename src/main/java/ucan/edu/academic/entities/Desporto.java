package ucan.edu.academic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "desporto")
@Getter
@Setter
@NoArgsConstructor
public class Desporto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkDesporto;
    
    private String nome;

    public Desporto(String nome) {
        this.nome = nome;
    }
}