package ucan.edu.academic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucan.edu.academic.entities.Localidade;

import java.util.List;

@Repository
public interface LocalidadeRepository extends JpaRepository<Localidade, Integer> {

    @Query("SELECT l FROM Localidade l WHERE l.fkLocalidadePai.designacao = :pai AND l.fkLocalidadePai.fkLocalidadePai.designacao = :avo")
    List<Localidade> findAllFilhosbyavoAndPaiUp(@Param("pai") String pai, @Param("avo") String avo);

    @Query("SELECT l FROM Localidade l WHERE (l.designacao LIKE :nome) AND (l.fkLocalidadePai is NULL)")
    public Localidade findLocalidadeByNome(String nome);

    public Localidade findByDesignacao(String designacao);

    @Query("SELECT l FROM Localidade l WHERE (l.designacao LIKE :nome) AND (l.fkLocalidadePai.designacao LIKE :pai)")
    public Localidade findLocalidadeByNomeByPai(String nome, String pai);

    @Query("SELECT l FROM Localidade l WHERE (l.fkLocalidadePai IS NOT NULL) AND (l.fkLocalidadePai.fkLocalidadePai IS NOT NULL) AND (l.fkLocalidadePai.designacao ILIKE :nomePai)  AND (l.fkLocalidadePai.fkLocalidadePai.designacao ILIKE :nomeAvo) ORDER BY l.designacao")
    public List<Localidade> findAllFilhosbyavoAndPai(String nomePai, String nomeAvo);



   /*@Query("SELECT l FROM Localidade l WHERE l.fkLocalidadePai IS NOT NULL AND l.fkLocalidadePai.pkLocalidade = :pkLocalidade ORDER BY l.designacao")
    public List<Localidade> findAllFilhos(int pkLocalidade);

    List<Localidade> findByFkLocalidadePaiPkLocalidadedeOrderByDesignacao(Integer pkLocalidade);*/ //findAllfihos

    List<Localidade> findByFkLocalidadePai_PkLocalidade(int codigoPai);
    List<Localidade> findByFkLocalidadePaiIsNull();
}
