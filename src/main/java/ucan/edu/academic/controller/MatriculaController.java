package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.entities.Matricula;
import ucan.edu.academic.services.MatriculaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<Matricula> realizarMatricula(@RequestBody Matricula matricula) {
        return ResponseEntity.ok(matriculaService.realizarMatricula(matricula));
    }

    @PostMapping("/list")
    public ResponseEntity<List<Matricula>> realizarMatriculas(@RequestBody List<Matricula> matriculas) {
        return ResponseEntity.ok(matriculaService.realizarMatriculasList(matriculas));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matricula> atualizarMatricula(@PathVariable Integer id, @RequestBody Matricula matricula) {
        return ResponseEntity.ok(matriculaService.atualizarMatricula(id, matricula));
    }

}
