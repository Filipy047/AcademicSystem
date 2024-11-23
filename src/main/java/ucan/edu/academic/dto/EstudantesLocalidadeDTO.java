package ucan.edu.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.entities.Localidade;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public  class EstudantesLocalidadeDTO {
    private List<Estudante> estudantes;
    private List<
            Localidade> hierarquiaLocalidades;

    /**
     * Método para obter nomes das localidades na hierarquia
     */
    public List<String> getNomesLocalidades() {
        return hierarquiaLocalidades.stream()
                .map(Localidade::getDesignacao)
                .collect(Collectors.toList());
    }

    /**
     * Método para obter detalhes completos da hierarquia
     */
    public List<LocalidadeDetalheDTO> getDetalhesLocalidades() {
        return hierarquiaLocalidades.stream()
                .map(localidade -> new LocalidadeDetalheDTO(
                        localidade.getPkLocalidade(),
                        localidade.getDesignacao(),
                        localidade.getFkLocalidadePai() != null
                                ? localidade.getFkLocalidadePai().getDesignacao()
                                : null
                ))
                .collect(Collectors.toList());
    }
}