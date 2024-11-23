package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.dto.PlanoCursoDTO;
import ucan.edu.academic.entities.Disciplina;
import ucan.edu.academic.entities.PlanoCurso;
import ucan.edu.academic.services.PlanoCursoService;

import java.util.List;
import java.util.Set;

// Controller
@RestController
@RequestMapping("/plano-curso")
public class PlanoCursoController {

    @Autowired
    private PlanoCursoService planoCursoService;

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<PlanoCurso>> getPlanosByCurso(@PathVariable Integer cursoId) {
        return ResponseEntity.ok(planoCursoService.getPlanosByCurso(cursoId));
    }

    @GetMapping("/curso/{cursoId}/detalhado")
    public ResponseEntity<PlanoCursoDTO> getPlanoCursoComDisciplinasEPreRequisitos(@PathVariable Integer cursoId) {
        PlanoCursoDTO planoCursoDTO = planoCursoService.getPlanoCursoComDisciplinasEPreRequisitos(cursoId);
        return ResponseEntity.ok(planoCursoDTO);
    }


    @GetMapping("/curso/{cursoId}/semestre/{semestre}")
    public ResponseEntity<List<PlanoCurso>> getPlanosByCursoESemestre(
            @PathVariable Integer cursoId,
            @PathVariable Integer semestre) {
        return ResponseEntity.ok(planoCursoService.getPlanosByCursoESemestre(cursoId, semestre));
    }

    @PostMapping
    public ResponseEntity<PlanoCurso> createPlanoCurso(@RequestBody PlanoCurso planoCurso) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(planoCursoService.savePlanoCurso(planoCurso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanoCurso(@PathVariable Integer id) {
        planoCursoService.deletePlanoCurso(id);
        return ResponseEntity.noContent().build();
    }
}

  /*@GetMapping("/curso/{cursoId}/disciplinas-disponiveis")
    public ResponseEntity<List<Disciplina>> getDisciplinasDisponiveis(
            @PathVariable Long cursoId,
            @RequestParam Set<Long> disciplinasConcluidas) {
        return ResponseEntity.ok(planoCursoService.getDisciplinasDisponiveis(cursoId, disciplinasConcluidas));
    }*/ //uso futuro