package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.entities.Curso;
import ucan.edu.academic.services.CursoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    // Benefícios da Cache - Operação de Listagem Rápida
    @GetMapping
    public ResponseEntity<List<Curso>> findAllCursos() {
        // Acesso extremamente rápido à lista pré-carregada e ordenada
        List<Curso> cursos = cursoService.findAll();

        // Tempo de resposta muito baixo, sem necessidade de consulta ao banco
        return ResponseEntity.ok(cursos);
    }

    // Benefícios da Cache - Busca por ID Otimizada
    @GetMapping("/{pkCurso}")
    public ResponseEntity<Curso> findCursoById(@PathVariable Integer pkCurso) {
        // Busca instantânea na cache, sem consulta ao banco de dados
        Curso curso = cursoService.findById(pkCurso);

        if (curso != null) {
            return ResponseEntity.ok(curso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Benefícios da Cache - Busca por Nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Curso> findCursoByNome(@PathVariable String nome) {
        // Busca direta na cache de nomes, extremamente rápida
        Curso curso = cursoService.findByNome(nome);

        if (curso != null) {
            return ResponseEntity.ok(curso);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Operações de Escrita - Manutenção da Consistência
    @PostMapping
    public ResponseEntity<Curso> createCurso(@RequestBody Curso curso) {
        // Persiste e atualiza cache simultaneamente
        Curso novoCurso = cursoService.create(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso);
    }

    @PutMapping("/{pkCurso}")
    public ResponseEntity<Curso> updateCurso(@PathVariable Integer pkCurso, @RequestBody Curso curso) {
        // Garante que o ID da URL corresponda ao do objeto
        curso.setPkCurso(pkCurso);

        // Atualiza e sincroniza cache
        Curso cursoAtualizado = cursoService.update(curso);
        return ResponseEntity.ok(cursoAtualizado);
    }

    @DeleteMapping("/{pkCurso}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Integer pkCurso) {
        // Remove do banco e da cache
        cursoService.delete(pkCurso);
        return ResponseEntity.noContent().build();
    }


    //------ CACHE ------
    @GetMapping("/cache/pkCurso")
    public ResponseEntity<Map<Integer, Curso>> getCacheByPkCurso() {
        // Retorna a cache por chave primária (ID)
        return ResponseEntity.ok(cursoService.getCacheByPkCurso());
    }

    @GetMapping("/cache/nome")
    public ResponseEntity<Map<String, Curso>> getCacheByNome() {
        // Retorna a cache por nome
        return ResponseEntity.ok(cursoService.getCacheByNome());
    }

    @GetMapping("/cache/lista")
    public ResponseEntity<List<Curso>> getListaCursosCache() {
        // Retorna a lista ordenada em cache
        return ResponseEntity.ok(cursoService.getCursos());
    }

}