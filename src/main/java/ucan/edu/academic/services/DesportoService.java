package ucan.edu.academic.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.dto.EstudanteDesportoDTO;
import ucan.edu.academic.entities.Desporto;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.repositories.DesportoRepository;
import ucan.edu.academic.repositories.EstudanteRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

        List<String> nomesDesportos = Arrays.asList(
                "Futebol", "Basquetebol", "Andebol", "Atletismo", "Voleibol",
                "Tênis de Mesa", "Tênis de Campo", "Yoga", "Ginástica", "Xadrez"
        );

        List<Desporto> desportos = new ArrayList<>();
        for (String nome : nomesDesportos) {
            desportos.add(new Desporto(nome));
        }


        desportoRepository.saveAll(desportos);
    }

    public Desporto escolherAleatoriamenteDesporto() {
        List<Desporto> desportos = desportoRepository.findAll();
        if (desportos.isEmpty()) {
            throw new IllegalStateException("Nenhum desporto cadastrado");
        }
        Random random = new Random();
        return desportos.get(random.nextInt(desportos.size()));
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