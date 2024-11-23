package ucan.edu.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public  class LocalidadeDetalheDTO {
    private int pkLocalidade;
    private String designacao;
    private String localidadePai;
}
