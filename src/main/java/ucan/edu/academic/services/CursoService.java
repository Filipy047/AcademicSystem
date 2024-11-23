package ucan.edu.academic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ucan.edu.academic.entities.Curso;
import ucan.edu.academic.repositories.CursoRepository;

import java.util.*;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private HashMap<Integer, Curso> cursoByPkCursoCache;
    private HashMap<String, Curso> cursoByNomeCache;
    private List<Curso> cursos;
    private boolean initialized = false;

    @Autowired
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoByPkCursoCache = new HashMap<>();
        this.cursoByNomeCache = new HashMap<>();
    }

    @Async
    public void init() {
        initializeCursosAsync();
        initializeCache();
    }


    protected void initializeCursosAsync() {
        if (cursoRepository.count() > 0) {
            System.out.println("Já existem cursos cadastrados.");
            initialized = true;
            return;
        }

        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso("Engenharia Informática", "Curso de Engenharia Informática", 4, 3000, "Bacharelado"));
        cursos.add(new Curso("Telecomunicações", "Curso de Engenharia de Telecomunicações", 4, 3000, "Bacharelado"));
        cursos.add(new Curso("Engenharia Civil", "Curso de Engenharia Civil", 5, 3200, "Bacharelado"));
        cursos.add(new Curso("Direito", "Curso de Direito", 5, 2800, "Bacharelado"));
        cursos.add(new Curso("Economia", "Curso de Economia", 4, 2600, "Bacharelado"));

        cursoRepository.saveAll(cursos);
        System.out.println("Cursos cadastrados com sucesso.");
        initialized = true;
    }

    private void initializeCache() {
        cursoByPkCursoCache.clear();
        cursoByNomeCache.clear();

        cursos = cursoRepository.findAll();
        sortCursosAlphabetically(cursos);

        for (Curso curso : cursos) {
            cursoByPkCursoCache.put(curso.getPkCurso(), curso);
            cursoByNomeCache.put(curso.getNomeCurso(), curso);
        }
    }

    private void sortCursosAlphabetically(List<Curso> cursos) {
        for (int i = 0; i < cursos.size(); i++) {
            for (int j = i + 1; j < cursos.size(); j++) {
                if (cursos.get(i).getNomeCurso().compareTo(cursos.get(j).getNomeCurso()) > 0) {
                    Curso temp = cursos.get(i);
                    cursos.set(i, cursos.get(j));
                    cursos.set(j, temp);
                }
            }
        }
    }

    // Métodos de leitura (Read)
    public List<Curso> findAll() {
        return cursos; // Já ordenado alfabeticamente
    }

    public Curso findById(Integer pkCurso) {
        return cursoByPkCursoCache.get(pkCurso);
    }

    public Curso findByNome(String nome) {
        return cursoByNomeCache.get(nome);
    }



    // Método Create
    public Curso create(Curso curso) {
        Curso novoCurso = cursoRepository.save(curso);

        cursoByPkCursoCache.put(novoCurso.getPkCurso(), novoCurso);
        cursoByNomeCache.put(novoCurso.getNomeCurso(), novoCurso);
        cursos.add(novoCurso);
        sortCursosAlphabetically(cursos);

        System.out.println("Cache atualizado após criação: " + cursoByPkCursoCache);
        System.out.println("Lista atualizada: " + cursos);

        return novoCurso;
    }

    // Método Update
    public Curso update(Curso curso) {
        Curso cursoAtualizado = cursoRepository.save(curso);

        Curso cursoAntigo = cursoByPkCursoCache.get(curso.getPkCurso());
        if (cursoAntigo != null && !cursoAntigo.getNomeCurso().equals(cursoAtualizado.getNomeCurso())) {
            cursoByNomeCache.remove(cursoAntigo.getNomeCurso());
        }

        cursoByPkCursoCache.put(cursoAtualizado.getPkCurso(), cursoAtualizado);
        cursoByNomeCache.put(cursoAtualizado.getNomeCurso(), cursoAtualizado);

        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getPkCurso().equals(cursoAtualizado.getPkCurso())) {
                cursos.set(i, cursoAtualizado);
                break;
            }
        }
        sortCursosAlphabetically(cursos);

        System.out.println("Cache atualizado após atualização: " + cursoByPkCursoCache);
        System.out.println("Lista atualizada: " + cursos);

        return cursoAtualizado;
    }

    // Método Delete
    public void delete(Integer pkCurso) {
        Curso cursoParaRemover = cursoByPkCursoCache.get(pkCurso);
        cursoRepository.deleteById(pkCurso);

        if (cursoParaRemover != null) {
            cursoByPkCursoCache.remove(pkCurso);
            cursoByNomeCache.remove(cursoParaRemover.getNomeCurso());
        }

        for (int i = 0; i < cursos.size(); i++) {
            if (cursos.get(i).getPkCurso().equals(pkCurso)) {
                cursos.remove(i);
                break;
            }
        }

        System.out.println("Cache atualizado após exclusão: " + cursoByPkCursoCache);
        System.out.println("Lista atualizada: " + cursos);
    }


    public Map<Integer, Curso> getCacheByPkCurso() {
        return cursoByPkCursoCache;
    }

    public Map<String, Curso> getCacheByNome() {
        return cursoByNomeCache;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void awaitInitialization() throws InterruptedException {
        while (!isInitialized()) {
            Thread.sleep(1000);
        }
    }
}