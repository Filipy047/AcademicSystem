package ucan.edu.academic.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.dto.EstudanteDesportoDTO;
import ucan.edu.academic.entities.Desporto;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.repositories.DesportoRepository;
import ucan.edu.academic.repositories.EstudanteRepository;

import java.util.*;

@Service
public class DesportoService {
    private final DesportoRepository desportoRepository;

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    public DesportoService(DesportoRepository desportoRepository) {
        this.desportoRepository = desportoRepository;
    }

    @PostConstruct
    public void initializeDesportos() {
        if (desportoRepository.count() > 0) {
            return;
        }

        desportoRepository.saveAll(createDesportos());
    }

    public List<Desporto> createDesportos() {
        return Arrays.asList(
                new Desporto("Futebol"),
                new Desporto("Basquetebol"),
                new Desporto("Andebol"),
                new Desporto("Atletismo"),
                new Desporto("Voleibol"),
                new Desporto("Tênis de Mesa"),
                new Desporto("Tênis de Campo"),
                new Desporto("Yoga"),
                new Desporto("Ginástica"),
                new Desporto("Xadrez")
        );
    }

    public List<Desporto> escolherAleatoriamenteDesporto() {
        int quantidadeDesejada = new Random().nextInt(3) + 1;
        List<Desporto> todosDesportos = desportoRepository.findAll();

        if (todosDesportos.isEmpty()) {
            throw new IllegalStateException("Nenhum desporto cadastrado");
        }

        // Embaralha a lista de todos os desportos e pega os primeiros N elementos
        Collections.shuffle(todosDesportos);
        return todosDesportos.subList(0, Math.min(quantidadeDesejada, todosDesportos.size()));
    }

    public List<EstudanteDesportoDTO> findEstudantesByDesporto(String nomeDesporto) {
        List<EstudanteDesportoDTO> estudantesDTO = new ArrayList<>();
        List<Estudante> estudantes = estudanteRepository.findAll();

        for (Estudante estudante : estudantes) {
            List<String> nomesDesportos = new ArrayList<>();
            boolean praticaDesporto = false;

            for (Desporto desporto : estudante.getDesportosPracticados()) {
                nomesDesportos.add(desporto.getNome());
                if (desporto.getNome().equalsIgnoreCase(nomeDesporto)) {
                    praticaDesporto = true;
                }
            }

            if (praticaDesporto) {
                estudantesDTO.add(new EstudanteDesportoDTO(
                        estudante.getPkEstudante(),
                        estudante.getNomeCompleto(),
                        nomesDesportos
                ));
            }
        }
        return estudantesDTO;
    }
}