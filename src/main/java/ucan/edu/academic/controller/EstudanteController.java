package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.dto.EstudanteDesportoDTO;
import ucan.edu.academic.dto.EstudantesLocalidadeDTO;
import ucan.edu.academic.entities.Desporto;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.services.EstudanteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

    @Autowired
    private EstudanteService estudanteService;

    // Criar estudante
    @PostMapping
    public ResponseEntity<Estudante> criarEstudante(@RequestBody Estudante estudante) {
        try {
            Estudante novoEstudante = estudanteService.criarEstudante(estudante);
            return new ResponseEntity<>(novoEstudante, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Listar todos os estudantes
    @GetMapping
    public ResponseEntity<List<Estudante>> listarEstudantes() {
        List<Estudante> estudantes = estudanteService.findAllEstudantes();
        return new ResponseEntity<>(estudantes, HttpStatus.OK);
    }

    // Obter estudante por ID
    @GetMapping("/{id}")
    public ResponseEntity<Estudante> obterEstudante(@PathVariable int id) {
        try {
            Estudante estudante = estudanteService.obterEstudantePorId(id);
            return new ResponseEntity<>(estudante, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Atualizar estudante
    @PutMapping("/{id}")
    public ResponseEntity<Estudante> atualizarEstudante(@PathVariable Integer id, @RequestBody Estudante estudante) {
        try {
            Estudante estudanteAtualizado = estudanteService.atualizarEstudante(id, estudante);
            return new ResponseEntity<>(estudanteAtualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Deletar estudante
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarEstudante(@PathVariable Integer id) {
        try {
            estudanteService.deletarEstudante(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //delete and return estudante deleted
    @DeleteMapping("/delete-estudante/{id}")
    public ResponseEntity<Estudante> deleteEstudante(@PathVariable Integer id) {
        try {
            Estudante estudanteDeleted = estudanteService.deletarEstudante(id);
            return new ResponseEntity<>(estudanteDeleted, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/residencia/{pkLocalidade}")
    public ResponseEntity<EstudantesLocalidadeDTO> buscarPorLocalResidencia(
            @PathVariable("pkLocalidade") int pkLocalidade) {
        try {
            EstudantesLocalidadeDTO resultado = estudanteService.findEstudantesByLocalResidencia(pkLocalidade);

            if (resultado.getEstudantes().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAllEstudantesWithdesportos")
    public ResponseEntity<List<EstudanteDesportoDTO>> listarEstudantesComDesportos() {
        List<EstudanteDesportoDTO> estudantesComDesportos = estudanteService.findAllEstudantesWithDesportos();

        if (estudantesComDesportos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(estudantesComDesportos);
    }

    @GetMapping("/findEstudanteWithdesportos/{id}")
    public ResponseEntity<EstudanteDesportoDTO> obterEstudanteDesporto(@PathVariable int id) {
        try {
            EstudanteDesportoDTO estudanteDesporto = estudanteService.findEstudanteWithDesportosById(id);
            return ResponseEntity.ok(estudanteDesporto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}


