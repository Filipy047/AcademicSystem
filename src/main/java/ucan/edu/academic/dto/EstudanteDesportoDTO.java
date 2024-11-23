package ucan.edu.academic.dto;

import ucan.edu.academic.entities.Desporto;

import java.util.List;
import java.util.stream.Collectors;


public class EstudanteDesportoDTO {
    private Integer id;
    private String nomeCompleto;
    private List<String> desportos;

    public EstudanteDesportoDTO(Integer id, String nomeCompleto, List<String> desportos) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.desportos = desportos;
    }



    // Getters
    public Integer getId() { return id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public List<String> getDesportos() { return desportos; }


}