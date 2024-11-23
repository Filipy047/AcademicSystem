package ucan.edu.academic.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.entities.Matricula;
import ucan.edu.academic.repositories.CursoRepository;
import ucan.edu.academic.repositories.EstudanteRepository;
import ucan.edu.academic.repositories.MatriculaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaService {
    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    public Matricula realizarMatricula(Matricula matricula) {
        // Imprime informações iniciais da matrícula
        System.out.println("Iniciando matrícula...");
        System.out.println("Estudante ID: " + matricula.getEstudante().getPkEstudante());
        System.out.println("Curso ID: " + matricula.getCurso().getPkCurso());

        if (matricula.getEstudante() == null || matricula.getCurso() == null) {
            throw new IllegalArgumentException("Estudante e Curso não podem ser nulos");
        }

        //verifica se o estudante jã está matriculado em algum curso
        boolean jaMatriculado = matriculaRepository
                .existsByEstudante_PkEstudante(matricula.getEstudante().getPkEstudante());

        if (jaMatriculado) {
            throw new RuntimeException("Estudante já está matriculado em aglum curso de");
        }

        // Verifica se estudante existe
        System.out.println("Verificando existência do estudante...");
        estudanteRepository.findById(matricula.getEstudante().getPkEstudante())
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));
        System.out.println("Estudante encontrado.");

        // Verifica se curso existe
        System.out.println("Verificando existência do curso...");
        cursoRepository.findById(matricula.getCurso().getPkCurso())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        System.out.println("Curso encontrado.");

        // Salva a matrícula e retorna o objeto salvo
        Matricula matriculaSalva = matriculaRepository.save(matricula);
        System.out.println("Matrícula realizada com sucesso. ID da matrícula: " + matriculaSalva.getPkMatricula());

        return matriculaSalva;
    }

    // Realizar Matricula list . saveAll()
    @Transactional
    public List<Matricula> realizarMatriculasList(List<Matricula> matriculas) {
        List<Matricula> matriculasSalvas = new ArrayList<>();

        for (Matricula matricula : matriculas) {
            System.out.println("Iniciando matrícula...");
            System.out.println("Estudante ID: " + matricula.getEstudante().getPkEstudante());
            System.out.println("Curso ID: " + matricula.getCurso().getPkCurso());

            if (matricula.getEstudante() == null || matricula.getCurso() == null) {
                //throw new IllegalArgumentException("Estudante e Curso não podem ser nulos");
                System.out.println("Estudante e Curso não podem ser nulos");
                continue;
            }


            // Verifica se o estudante já está matriculado em algum curso
            boolean jaMatriculado = matriculaRepository
                    .existsByEstudante_PkEstudante(matricula.getEstudante().getPkEstudante());

            if (jaMatriculado) {
                throw new RuntimeException("Estudante já está matriculado em algum curso: "
                        + matricula.getEstudante().getPkEstudante());
            }

            // Verifica se o estudante existe
            System.out.println("Verificando existência do estudante...");
            estudanteRepository.findById(matricula.getEstudante().getPkEstudante())
                    .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));
            System.out.println("Estudante encontrado.");

            // Verifica se o curso existe
            System.out.println("Verificando existência do curso...");
            cursoRepository.findById(matricula.getCurso().getPkCurso())
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
            System.out.println("Curso encontrado.");

            // Salva a matrícula e adiciona à lista
            Matricula matriculaSalva = matriculaRepository.save(matricula);
            matriculasSalvas.add(matriculaSalva);

            System.out.println("Matrícula realizada com sucesso. ID da matrícula: " + matriculaSalva.getPkMatricula());
        }

        return matriculasSalvas;
    }

    //update matricula
    @Transactional
    public Matricula atualizarMatricula(Integer id, Matricula matricula) {
        Matricula matriculaExistente = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula nao encontrada"));

        matriculaExistente.setEstudante(matricula.getEstudante());
        matriculaExistente.setCurso(matricula.getCurso());
        matriculaExistente.setDataMatricula(matricula.getDataMatricula());
        matriculaExistente.setAnoLetivo(matricula.getAnoLetivo());

        return matriculaRepository.save(matriculaExistente);
    }
}
