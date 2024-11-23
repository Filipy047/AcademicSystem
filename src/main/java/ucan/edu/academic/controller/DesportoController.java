package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.dto.EstudanteDesportoDTO;
import ucan.edu.academic.entities.Desporto;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.services.DesportoService;
import ucan.edu.academic.services.EstudanteService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("desportos")
public class DesportoController {

    @Autowired
    private  DesportoService desportoService;

    @GetMapping("/estudantes/{nomeDesporto}")
    public ResponseEntity<List<EstudanteDesportoDTO>> getEstudantesByDesporto(@PathVariable(name = "nomeDesporto") String nomeDesporto) {

        List<EstudanteDesportoDTO> estudantes = desportoService.findEstudantesByDesporto(nomeDesporto);

        //return estudantes with verification response
        if (estudantes.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(estudantes);
        }

    }

}