package ucan.edu.academic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ucan.edu.academic.dto.EstudanteDesportoDTO;
import ucan.edu.academic.dto.EstudantesLocalidadeDTO;
import ucan.edu.academic.entities.Desporto;
import ucan.edu.academic.entities.Estudante;
import ucan.edu.academic.entities.Localidade;
import ucan.edu.academic.repositories.EstudanteRepository;
import ucan.edu.academic.repositories.LocalidadeRepository;
import ucan.edu.academic.utils.DataUtils;
import ucan.edu.academic.utils.EstudanteUtils;
import ucan.edu.academic.utils.ListUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final LocalidadeRepository localidadeRepository;
    private final LocalidadeService localidadeService;
    private final DesportoService desportoService;

    @Autowired
    public EstudanteService(
            EstudanteRepository estudanteRepository,
            LocalidadeRepository localidadeRepository,
            LocalidadeService localidadeService,
            DesportoService desportoService
    ) {
        this.estudanteRepository = estudanteRepository;
        this.localidadeRepository = localidadeRepository;
        this.localidadeService = localidadeService;
        this.desportoService = desportoService;
    }

    public void initializeEstudantes() {
        if (!isInicializacaoNecessaria()) return;

        List<Estudante> estudantes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            estudantes.add(gerarEstudanteAleatorio());
        }

        estudanteRepository.saveAll(estudantes);
        System.out.println("Estudantes cadastrados com sucesso:\t" +
                ListUtils.toString(estudantes, Estudante::getNomeCompleto));
    }

    private Estudante gerarEstudanteAleatorio() {
        return new Estudante(
                EstudanteUtils.gerarNome(),
                DataUtils.escolherAleatoriamenteDataNascimento(20, 60),
                EstudanteUtils.gerarGenero(),
                EstudanteUtils.gerarNumeroEstudante(),
                EstudanteUtils.gerarEmail(),
                EstudanteUtils.gerarTelefone(),
                EstudanteUtils.gerarDataMatricula(),
                localidadeService.escolherAleatoriamenteLocalidadeAngolana(),
                localidadeService.escolherAleatoriamenteLocalidadeAngolana(),
                desportoService.escolherAleatoriamenteDesporto()
        );
    }

    private boolean isInicializacaoNecessaria() {
        if (estudanteRepository.count() > 0) {
            System.out.println("Estudantes já foram inicializados anteriormente.");
            return false;
        }
        return true;
    }

    // Criar novo estudante
    public Estudante createEstudante(Estudante estudante) {
        // Validação simples
        if (estudante == null) {
            throw new IllegalArgumentException("Estudante não pode ser nulo");
        }
        if (StringUtils.isEmpty(estudante.getNomeCompleto())) {
            throw new IllegalArgumentException("Nome completo é obrigatório");
        }
        if (estudante.getDataDeNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }
        // Validar se as localidades existem
        if (estudante.getLocalTrabalho() != null) {
            localidadeRepository.findById(estudante.getLocalTrabalho().getPkLocalidade())
                    .orElseThrow(() -> new IllegalArgumentException("Local de trabalho inválido"));
        }
        if (estudante.getLocalResidencia() != null) {
            localidadeRepository.findById(estudante.getLocalResidencia().getPkLocalidade())
                    .orElseThrow(() -> new IllegalArgumentException("Local de residência inválido"));
        }
        return estudanteRepository.save(estudante);
    }

    // Listar todos os estudantes
    public List<Estudante> findAllEstudantes() {
        return estudanteRepository.findAll();
    }

    // Obter estudante por ID
    public Estudante findEstudantebyId(int id) {
        Optional<Estudante> estudante = estudanteRepository.findById(id);
        return estudante.orElseThrow(() -> new IllegalArgumentException("Estudante não encontrado"));
    }

    // Atualizar estudante
    public Estudante updateEstudante(int id, Estudante estudante) {
        if (!estudanteRepository.existsById(id)) {
            throw new IllegalArgumentException("Estudante não encontrado para atualização");
        }

        // Validar localidades se fornecidas
        if (estudante.getLocalTrabalho() != null) {
            localidadeRepository.findById(estudante.getLocalTrabalho().getPkLocalidade())
                    .orElseThrow(() -> new IllegalArgumentException("Local de trabalho inválido"));
        }
        if (estudante.getLocalResidencia() != null) {
            localidadeRepository.findById(estudante.getLocalResidencia().getPkLocalidade())
                    .orElseThrow(() -> new IllegalArgumentException("Local de residência inválido"));
        }

        estudante.setPkEstudante(id);
        return estudanteRepository.save(estudante);
    }

    // Deletar estudante
    public Estudante deletarEstudante(int id) {
        if (!estudanteRepository.existsById(id)) {
            throw new IllegalArgumentException("Estudante não encontrado para deleção");
        }
        estudanteRepository.deleteById(id);
        return null;
    }


    public EstudantesLocalidadeDTO findEstudantesByLocalResidencia(int pkLocalResidencia) {
        // Buscar a localidade de residência
        Localidade localResidencia = localidadeRepository.findById(pkLocalResidencia)
                .orElseThrow(() -> new IllegalArgumentException("Localidade de residência não encontrada"));

        // Buscar estudantes na localidade
        List<Estudante> estudantes = estudanteRepository.findByLocalResidencia(localResidencia);

        // Criar hierarquia de localidades
        List<Localidade> hierarquiaLocalidades = new ArrayList<>();
        hierarquiaLocalidades.add(localResidencia);

        // Adicionar pai, se existir
        if (localResidencia.getFkLocalidadePai() != null) {
            hierarquiaLocalidades.add(localResidencia.getFkLocalidadePai());

            // Adicionar avô, se existir
            if (localResidencia.getFkLocalidadePai().getFkLocalidadePai() != null) {
                hierarquiaLocalidades.add(localResidencia.getFkLocalidadePai().getFkLocalidadePai());
            }
        }

        // Criar DTO para retorno
        return new EstudantesLocalidadeDTO(estudantes, hierarquiaLocalidades);
    }

    public List<EstudanteDesportoDTO> findAllEstudantesWithDesportos() {
        List<Estudante> estudantes = estudanteRepository.findAll();
        List<EstudanteDesportoDTO> resultado = new ArrayList<>();

        for (Estudante estudante : estudantes) {
            List<String> nomesDesportos = new ArrayList<>();
            for (Desporto desporto : estudante.getDesportosPracticados()) {
                nomesDesportos.add(desporto.getNome());
            }

            EstudanteDesportoDTO dto = new EstudanteDesportoDTO(
                    estudante.getPkEstudante(),
                    estudante.getNomeCompleto(),
                    nomesDesportos
            );
            resultado.add(dto);
        }

        return resultado;
    }

    //find estudante withs your desportos by id
    public EstudanteDesportoDTO findEstudanteWithDesportosById(int id) {
        Estudante estudante = estudanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Estudante nao encontrado"));
        List<String> nomesDesportos = new ArrayList<>();
        for (Desporto desporto : estudante.getDesportosPracticados()) {
            nomesDesportos.add(desporto.getNome());
        }
        EstudanteDesportoDTO dto = new EstudanteDesportoDTO(estudante.getPkEstudante(), estudante.getNomeCompleto(), nomesDesportos);
        return dto;
    }

      /*private void saveIfNotExists(Estudante estudante) {
        if (estudanteRepository.existsByNumDocumento(estudante.getNumDocumento())) {
            System.out.println("Estudante já existe: " + estudante.getNomeCompleto());
            return;
        }
        estudanteRepository.save(estudante);
        System.out.println("Estudante salvo: " + estudante.getNomeCompleto());
    }*/

}
