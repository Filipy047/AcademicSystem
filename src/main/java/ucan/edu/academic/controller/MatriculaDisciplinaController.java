package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.entities.MatriculaDisciplina;
import ucan.edu.academic.services.MatriculaDisciplinaService;

@RestController
@RequestMapping("/matriculas-disciplinas")
public class MatriculaDisciplinaController {
    @Autowired
    private MatriculaDisciplinaService matriculaDisciplinaService;

    @PostMapping
    public ResponseEntity<MatriculaDisciplina> matricularDisciplina(@RequestBody MatriculaDisciplina matriculaDisciplina) {
        return ResponseEntity.ok(matriculaDisciplinaService.matricularDisciplina(matriculaDisciplina));
    }

    @PatchMapping("/{id}/nota")
    public ResponseEntity<MatriculaDisciplina> atualizarNota(@PathVariable Integer id, @RequestBody Double nota) {
        return ResponseEntity.ok(matriculaDisciplinaService.atualizarNota(id, nota));
    }

    @PatchMapping("/{idMatricula}/{idDisciplina}/nota")
    public ResponseEntity<MatriculaDisciplina> atualizarNota(@PathVariable Integer idMatricula, @PathVariable Integer idDisciplina, @RequestBody Double nota) {
        return ResponseEntity.ok(matriculaDisciplinaService.atualizarNota(idMatricula, idDisciplina, nota));
    }
}