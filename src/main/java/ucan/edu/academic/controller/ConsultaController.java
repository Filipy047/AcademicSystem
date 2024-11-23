package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucan.edu.academic.dto.AlunoCursoDTO;
import ucan.edu.academic.dto.CursoHistoricoDTO;
import ucan.edu.academic.services.ConsultaService;


@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    /*@GetMapping("/disciplinas-concluidas/{matriculaId}")
    public ResponseEntity<List<MatriculaDisciplinaDTO>> consultarDisciplinasConcluidas(
            @PathVariable("matriculaId") Long matriculaId) {
        List<MatriculaDisciplinaDTO> disciplinasConcluidas = consultaService.consultarDisciplinasConcluidas(matriculaId);
        return ResponseEntity.ok(disciplinasConcluidas);
    }*/

    @GetMapping("/disciplinas-concluidas/{matriculaId}")
    public ResponseEntity<AlunoCursoDTO> consultarDisciplinasConcluidas(@PathVariable("matriculaId") Integer matriculaId) {
        AlunoCursoDTO alunoCurso = consultaService.consultarDisciplinasConcluidasPorAluno(matriculaId);
        return ResponseEntity.ok(alunoCurso);
    }


   /* @GetMapping("/disciplinas-cursadas/{matriculaId}")
    public ResponseEntity<List<MatriculaDisciplinaDTO>> consultarDisciplinasCursadasPorAno(
            @PathVariable("matriculaId") Long matriculaId) {
        List<MatriculaDisciplinaDTO> disciplinasCursadas = consultaService.consultarDisciplinasCursadasPorAno(matriculaId);
        return ResponseEntity.ok(disciplinasCursadas);
    }*/

    @GetMapping("/historico-curso/{estudanteId}") //tem
    public ResponseEntity<CursoHistoricoDTO> gerarHistoricoCurso(@PathVariable("estudanteId") int estudanteId) {
        CursoHistoricoDTO cursoHistorico = consultaService.gerarHistoricoCurso(estudanteId);
        return ResponseEntity.ok(cursoHistorico);
    }

    @GetMapping("/disciplinas-por-aluno/{matriculaId}")
    public ResponseEntity<AlunoCursoDTO> consultarDisciplinasPorAluno(
            @PathVariable("matriculaId") int matriculaId) {
        AlunoCursoDTO alunoCurso = consultaService.consultarDisciplinasPorAluno(matriculaId);
        return ResponseEntity.ok(alunoCurso);
    }
}
