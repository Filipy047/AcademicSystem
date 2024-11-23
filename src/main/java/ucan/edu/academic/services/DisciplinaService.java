package ucan.edu.academic.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ucan.edu.academic.entities.Disciplina;
import ucan.edu.academic.entities.DisciplinaPreRequisito;
import ucan.edu.academic.repositories.DisciplinaPreRequisitoRepository;
import ucan.edu.academic.repositories.DisciplinaRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final DisciplinaPreRequisitoRepository disciplinaPreRequisitoRepository;

    private HashMap<Integer, Disciplina> disciplinaByIdCache;
    private HashMap<String, Disciplina> disciplinaByCodigoCache;
    private List<Disciplina> disciplinas;
    @Getter
    private boolean initialized = false;

    private final Object initializationLock = new Object(); // Objeto de sincronização


    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository, DisciplinaPreRequisitoRepository disciplinaPreRequisitoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.disciplinaPreRequisitoRepository = disciplinaPreRequisitoRepository;

        this.disciplinaByIdCache = new HashMap<Integer, Disciplina>();
        this.disciplinaByCodigoCache = new HashMap<>();
    }

    @Async
    public void init() {
        initializeAsync();
    }

    protected void initializeAsync() {
        synchronized (initializationLock) {
            if (disciplinaRepository.count() > 0) {
                System.out.println("Já existem disciplinas cadastradas.");
                initialized = true;
                return;
            }

            List<Disciplina> disciplinas = Arrays.asList(
                    // Disciplinas do 1º Ano
                    new Disciplina("Análise Matemática I", "AM101", 5, 60, 1),
                    new Disciplina("Álgebra", "ALG101", 5, 60, 1),
                    new Disciplina("English Técnico I", "ENG101", 3, 30, 1),
                    new Disciplina("Sistemas Digitais I", "SD101", 4, 45, 1),
                    new Disciplina("Fundamentos de Programação I", "FP101", 6, 75, 1),
                    new Disciplina("Tópicos de Física Moderna", "TFM101", 4, 45, 1),
                    // Disciplinas do 2º Ano
                    new Disciplina("Análise Matemática II", "AM102", 5, 60, 2),
                    new Disciplina("Fundamentos de Programação II", "FP102", 6, 75, 2),
                    new Disciplina("Sistemas Operativos I", "SO101", 4, 45, 2),
                    new Disciplina("Network I", "NET101", 4, 45, 2),
                    new Disciplina("Cristianismo e Desenvolvimento", "CD101", 3, 30, 2),
                    new Disciplina("English Técnico II", "ENG102", 3, 30, 2)
            );

            disciplinaRepository.saveAll(disciplinas);
            System.out.println("Disciplinas base cadastradas com sucesso.");

            // Atualizar cache após salvar as disciplinas
            initializeCache();

            // Adicionar pré-requisitos após o cache estar pronto
            initializePreRequisitos();

            initialized = true;
        }
    }


    private void initializeCache() {
        try {
            // Limpar caches existentes
            disciplinaByIdCache.clear();
            disciplinaByCodigoCache.clear();

            // Buscar todas as disciplinas do repositório
            disciplinas = disciplinaRepository.findAll();

            // Ordenar alfabeticamente por nome
            disciplinas.sort(Comparator.comparing(Disciplina::getNomeDisciplina));

            // Popular caches
            for (Disciplina disciplina : disciplinas) {
                disciplinaByIdCache.put(disciplina.getPkDisciplina(), disciplina);
                disciplinaByCodigoCache.put(disciplina.getCodigoDisciplina(), disciplina);
            }

            System.out.println("Cache de disciplinas inicializado. Total de disciplinas: " + disciplinas.size());
        } catch (Exception e) {
            System.err.println("Erro ao inicializar cache de disciplinas: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do cache de disciplinas", e);
        }
    }


    private void initializePreRequisitos() {
        try {
            // Verificação adicional de segurança
            if (disciplinaByCodigoCache.isEmpty()) {
                throw new RuntimeException("Cache de disciplinas está vazio. Não é possível adicionar pré-requisitos.");
            }

            // Adicionar pré-requisitos
            addPreRequisito("AM102", "AM101");
            addPreRequisito("ENG102", "ENG101");
            addPreRequisito("FP102", "FP101");
            addPreRequisito("FP102", "ALG101");
            addPreRequisito("SO101", "FP101");
            addPreRequisito("SO101", "ALG101");

            System.out.println("Pré-requisitos inicializados com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar pré-requisitos: " + e.getMessage());

            // Log detalhado dos conteúdos do cache
            System.err.println("Disciplinas no cache:");
            disciplinaByCodigoCache.forEach((codigo, disciplina) ->
                    System.err.println("Código: " + codigo + ", Disciplina: " + disciplina.getNomeDisciplina())
            );

            throw new RuntimeException("Falha na inicialização de pré-requisitos", e);
        }
    }

    private void addPreRequisito(String codigoDisciplina, String codigoPreRequisito) {
        // Busca segura no cache
        Disciplina disciplina = disciplinaByCodigoCache.get(codigoDisciplina);
        Disciplina preRequisito = disciplinaByCodigoCache.get(codigoPreRequisito);

        // Verificações de segurança
        if (disciplina == null) {
            throw new RuntimeException("Disciplina não encontrada no cache: " + codigoDisciplina);
        }
        if (preRequisito == null) {
            throw new RuntimeException("Pré-requisito não encontrado no cache: " + codigoPreRequisito);
        }

        // Criar e salvar pré-requisito
        DisciplinaPreRequisito disciplinaPreRequisito = new DisciplinaPreRequisito();
        disciplinaPreRequisito.setDisciplina(disciplina);
        disciplinaPreRequisito.setPreRequisito(preRequisito);

        disciplinaPreRequisitoRepository.save(disciplinaPreRequisito);
        System.out.println("Pré-requisito adicionado: " + preRequisito.getCodigoDisciplina() + " -> " + disciplina.getCodigoDisciplina());
    }

    // Métodos CRUD com suporte a cache

    public List<Disciplina> findAll() {
        return disciplinas; // Já ordenado alfabeticamente
    }

    public Disciplina findById(Integer id) {
        return disciplinaByIdCache.get(id);
    }

    public Disciplina findByCodigo(String codigo) {
        return disciplinaByCodigoCache.get(codigo);
    }

    public Disciplina save(Disciplina disciplina) {
        Disciplina novaDisciplina = disciplinaRepository.save(disciplina);

        // Atualizar caches
        disciplinaByIdCache.put(novaDisciplina.getPkDisciplina(), novaDisciplina);
        disciplinaByCodigoCache.put(novaDisciplina.getCodigoDisciplina(), novaDisciplina);

        // Atualizar lista
        disciplinas.add(novaDisciplina);
        disciplinas.sort(Comparator.comparing(Disciplina::getNomeDisciplina));

        return novaDisciplina;
    }

    public Disciplina update(Integer id, Disciplina disciplinaAtualizada) {
        Disciplina disciplina = findById(id);

        if (disciplina != null) {
            disciplina.setNomeDisciplina(disciplinaAtualizada.getNomeDisciplina());
            disciplina.setCodigoDisciplina(disciplinaAtualizada.getCodigoDisciplina());
            disciplina.setCreditos(disciplinaAtualizada.getCreditos());
            disciplina.setCargaHoraria(disciplinaAtualizada.getCargaHoraria());

            Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);

            // Atualizar caches
            disciplinaByIdCache.put(disciplinaSalva.getPkDisciplina(), disciplinaSalva);
            disciplinaByCodigoCache.put(disciplinaSalva.getCodigoDisciplina(), disciplinaSalva);

            // Atualizar lista
            disciplinas = disciplinaRepository.findAll();
            disciplinas.sort(Comparator.comparing(Disciplina::getNomeDisciplina));

            return disciplinaSalva;
        }

        throw new RuntimeException("Disciplina não encontrada com o ID: " + id);
    }

    public void delete(Integer id) {
        Disciplina disciplina = findById(id);

        if (disciplina != null) {
            disciplinaRepository.deleteById(id);

            // Remover das caches
            disciplinaByIdCache.remove(id);
            disciplinaByCodigoCache.remove(disciplina.getCodigoDisciplina());

            // Atualizar lista
            disciplinas.remove(disciplina);
        }
    }

    public void awaitInitialization() throws InterruptedException {
        while (!isInitialized()) {
            Thread.sleep(1000); // Espera ativa
        }
    }
}