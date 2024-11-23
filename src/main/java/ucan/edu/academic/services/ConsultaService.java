package ucan.edu.academic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucan.edu.academic.dto.AlunoCursoDTO;
import ucan.edu.academic.dto.CursoHistorico;
import ucan.edu.academic.dto.CursoHistoricoDTO;
import ucan.edu.academic.dto.MatriculaDisciplinaDTO;
import ucan.edu.academic.entities.Matricula;
import ucan.edu.academic.entities.MatriculaDisciplina;
import ucan.edu.academic.enums.StatusMatricula;
import ucan.edu.academic.repositories.MatriculaDisciplinaRepository;
import ucan.edu.academic.repositories.MatriculaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private MatriculaDisciplinaRepository matriculaDisciplinaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    public CursoHistoricoDTO gerarHistoricoCurso(int estudanteId) {
        Matricula matricula = matriculaRepository.findByEstudante_PkEstudante(estudanteId).orElseThrow(() -> new RuntimeException("Matrícula não encontrada para o estudante com ID: " + estudanteId));

        List<MatriculaDisciplina> disciplinasCursadas = matriculaDisciplinaRepository.findByMatricula_Estudante_PkEstudante(estudanteId);
        List<MatriculaDisciplina> disciplinasConcluidas = matriculaDisciplinaRepository.findByMatricula_Estudante_PkEstudanteAndStatus(estudanteId, StatusMatricula.APROVADO);

        CursoHistorico cursoHistorico = new CursoHistorico(
                matricula.getEstudante().getNomeCompleto(),
                matricula.getCurso().getNomeCurso(),
                matricula.getCurso().getGrauAcademico(),
                matricula.getCurso().getDuracao(),
                matricula.getCurso().getCargaHorariaTotal(),
                disciplinasCursadas, disciplinasConcluidas
        );

        return new CursoHistoricoDTO(cursoHistorico);
    }

    public AlunoCursoDTO consultarDisciplinasPorAluno(int estudanteId) {
        Matricula matricula = matriculaRepository.findByEstudante_PkEstudante(estudanteId).orElseThrow(() -> new RuntimeException("Matrícula não encontrada para o estudante com ID: " + estudanteId));

        String nomeAluno = matricula.getEstudante().getNomeCompleto();
        String nomeCurso = matricula.getCurso().getNomeCurso();

        List<MatriculaDisciplina> matriculaDisciplinas = matriculaDisciplinaRepository.findByMatricula_Estudante_PkEstudante(estudanteId);
        List<MatriculaDisciplinaDTO> disciplinas = new ArrayList<>();

        for (MatriculaDisciplina matriculaDisciplina : matriculaDisciplinas) {
            disciplinas.add(new MatriculaDisciplinaDTO(matriculaDisciplina));
        }

        return new AlunoCursoDTO(nomeAluno, nomeCurso, disciplinas);
    }

    public AlunoCursoDTO consultarDisciplinasConcluidasPorAluno(Integer estudanteId) {
        Matricula matricula = matriculaRepository.findByEstudante_PkEstudante(estudanteId).orElseThrow(() -> new RuntimeException("Matrícula não encontrada para o estudante com ID: " + estudanteId));

        String nomeAluno = matricula.getEstudante().getNomeCompleto();
        String nomeCurso = matricula.getCurso().getNomeCurso();

        List<MatriculaDisciplina> matriculaDisciplinas = matriculaDisciplinaRepository.findByMatricula_Estudante_PkEstudanteAndStatus(estudanteId, StatusMatricula.APROVADO);

        List<MatriculaDisciplinaDTO> disciplinas = new ArrayList<>();
        for (MatriculaDisciplina matriculaDisciplina : matriculaDisciplinas) {
            disciplinas.add(new MatriculaDisciplinaDTO(matriculaDisciplina));
        }

        return new AlunoCursoDTO(nomeAluno, nomeCurso, disciplinas);
    }
}
