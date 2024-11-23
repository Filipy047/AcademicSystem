package ucan.edu.academic.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class InitializationFlag {
    @Id
    private Long id = 1L;  // ID fixo para ter apenas um registro
    private boolean inicializacaoCompleta;

    public InitializationFlag(Long id, boolean inicializacaoCompleta) {
        this.id = id;
        this.inicializacaoCompleta = inicializacaoCompleta;
    }
}
