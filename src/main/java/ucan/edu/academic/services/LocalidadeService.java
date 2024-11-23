package ucan.edu.academic.services;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ucan.edu.academic.entities.Localidade;
import ucan.edu.academic.repositories.LocalidadeRepository;
import ucan.edu.academic.utils.ListUtils;
import ucan.edu.academic.utils.Sitio;

import java.util.*;

@Service
public class LocalidadeService {

    private final LocalidadeRepository localidadeRepository;
    private static List<Localidade> localidadesAngolanas = new ArrayList<>();
    private static final Random random = new Random();


    public final String INDEFINIDO = "Indefinido";
    public final String INDEFINIDA = "Indefinida";
    @Getter
    private boolean initialized = false;

    @Autowired
    public LocalidadeService(LocalidadeRepository localidadeRepository) {
        this.localidadeRepository = localidadeRepository;
    }


    // Additional method to create list of Angolan localities
    @PostConstruct
    @CacheEvict(value = "localidadesAngolanas", allEntries = true)
    public void criarLocalidadesAngolanas() {
        if (localidadeRepository.count() == 0) {
            initializeAsync();
        }

        List<Localidade> todasLocalidades = localidadeRepository.findAll();

        /*
            para cada loc de localidades
                se loc.forAngolna()
                    adicionar loc em localidadesAngolanas
         */
        for (Localidade loc : todasLocalidades) {
            if (forAngolna(loc))
                localidadesAngolanas.add(loc);
        }
        System.out.println("LocalidadeService.criarLocalidadesAngolanas()\tlocalidadesAngolanas: " +
                ListUtils.toString(localidadesAngolanas, Localidade::getDesignacao));
    }

    private boolean forAngolna(Localidade loc )
    {
        if (loc.getDesignacao().equalsIgnoreCase("Angola"))
            return true;
        for (Localidade l = loc ; l.getFkLocalidadePai() != null ; l = l.getFkLocalidadePai())
        {
            if (l.getFkLocalidadePai().getDesignacao().equalsIgnoreCase("Angola"))
                return true;
        }
        return false;
    }

    @Async
    @CacheEvict(value = {"localidades", "continentes", "paises", "localidadesAngolanas", "filhosPorPai", "filhosPorAvoPai"}, allEntries = true)
    public void initializeAsync() {
        if (localidadeRepository.count() > 0) {
            System.out.println("Já existem localidades cadastradas.");
            initialized = true;
            return;
        }

        List<Sitio> sitios = createSitiosList();
        saveAll(sitios);
        System.out.println("Localidades base cadastradas com sucesso.");
        initialized = true;
    }


    private List<Sitio> createSitiosList() {
        return Arrays.asList(
                // continentes
                new Sitio("Africa"),
                new Sitio("America"),
                new Sitio("Europa"),
                new Sitio("Asia"),
                // continente indefinido
                new Sitio("Indefinido"),
                // paises
                new Sitio("Angola", "Africa"),
                new Sitio("Mocambique", "Africa"),
                new Sitio("Africa do Sul", "Africa"),
                new Sitio("Nigeria", "Africa"),
                new Sitio("Brasil", "America"),
                // provincias de Angola
                new Sitio("Luanda", "Angola", "Africa"),
                new Sitio("Cabinda", "Angola", "Africa"),
                new Sitio("Zaire", "Angola", "Africa"),
                new Sitio("Uige", "Angola", "Africa"),
                new Sitio("Lunda-Norte", "Angola", "Africa"),
                new Sitio("Lunda-Sul", "Angola", "Africa"),
                new Sitio("Malange", "Angola", "Africa"),
                new Sitio("Bengo", "Angola", "Africa"),
                new Sitio("Kwanza-Norte", "Angola", "Africa"),
                new Sitio("Kwanza-Sul", "Angola", "Africa"),
                new Sitio("Huambo", "Angola", "Africa"),
                new Sitio("Bie", "Angola", "Africa"),
                new Sitio("Moxico", "Angola", "Africa"),
                new Sitio("Kuando Kubango", "Angola", "Africa"),
                new Sitio("Benguela", "Angola", "Africa"),
                new Sitio("Huila", "Angola", "Africa"),
                new Sitio("Cunene", "Angola", "Africa"),
                new Sitio("Namibe", "Angola", "Africa"),
                // provincia indefinida de Angola
                new Sitio("Indefinida", "Angola", "Africa"),
            /*
                Municios de Luanda:
                Belas, Cacuaco, Cazenga, Icolo e Bengo,
                Luanda, Kissama, Kilamba Kiaxi, Talatona, Viana
             */
                new Sitio("Belas", "Luanda", "Angola"),
                new Sitio("Cacuaco", "Luanda", "Angola"),
                new Sitio("Cazenga", "Luanda", "Angola"),
                new Sitio("Icolo e Bengo", "Luanda", "Angola"),
                new Sitio("Luanda", "Luanda", "Angola"),
                new Sitio("Kissama", "Luanda", "Angola"),
                new Sitio("Kilamba Kiaxi", "Luanda", "Angola"),
                new Sitio("Talatona", "Luanda", "Angola"),
                new Sitio("Viana", "Luanda", "Angola"),
                // municipio indefinido da provincia de Luanda
                new Sitio("Indefinido", "Luanda", "Angola"),
                // pais indefinido de Africa
                new Sitio("Indefinido", "Africa"),
                // pais indefinido da America
                new Sitio("Indefinido", "America"),
                // pais indefinido da Europa
                new Sitio("Indefinido", "Europa"),
                // pais indefinido da Asia
                new Sitio("Indefinido", "Asia"),
                // provincia indefinida de Mocambique
                new Sitio("Indefinida", "Mocambique", "Africa"),
                // provincia indefinida de Africa do Sul
                new Sitio("Indefinida", "Africa do Sul", "Africa"),
                // estado indefinido de Nigeria
                new Sitio("Indefinido", "Nigeria", "Africa"),
                // estado indefinido de Brasil
                new Sitio("Indefinido", "Brasil", "America"),
                // municipio indefinidos da provincia de Cabinda
                new Sitio("Indefinido", "Cabinda", "Angola"),
                // municipio indefinidos da provincia de Zaire
                new Sitio("Indefinido", "Zaire", "Angola"),
                // municipio indefinidos da provincia de Uige
                new Sitio("Indefinido", "Uige", "Angola"),
                // municipio indefinidos da provincia de Lunda-Norte
                new Sitio("Indefinido", "Lunda-Norte", "Angola"),
                // municipio indefinidos da provincia de Lunda-Sul
                new Sitio("Indefinido", "Lunda-Sul", "Angola"),
                // municipio indefinidos da provincia de Malange
                new Sitio("Indefinido", "Malange", "Angola"),
                // municipio indefinidos da provincia de Bengo
                new Sitio("Indefinido", "Bengo", "Angola"),
                // municipio indefinidos da provincia de Kwanza-Norte
                new Sitio("Indefinido", "Kwanza-Norte", "Angola"),
                // municipio indefinidos da provincia de Kwanza-Sul
                new Sitio("Indefinido", "Kwanza-Sul", "Angola"),
                // municipio indefinidos da provincia de Huambo
                new Sitio("Indefinido", "Huambo", "Angola"),
                // municipio indefinidos da provincia de Bie
                new Sitio("Indefinido", "Bie", "Angola"),
                // municipio indefinidos da provincia de Moxico
                new Sitio("Indefinido", "Moxico", "Angola"),
                // municipio indefinidos da provincia de Kuando Kubango
                new Sitio("Indefinido", "Kuando Kubango", "Angola"),
                // municipio indefinidos da provincia de Benguela
                new Sitio("Indefinido", "Benguela", "Angola"),
                // municipio indefinidos da provincia de Huila
                new Sitio("Indefinido", "Huila", "Angola"),
                // municipio indefinidos da provincia de Cunene
                new Sitio("Indefinido", "Cunene", "Angola"),
                // municipio indefinidos da provincia de Namibe
                new Sitio("Indefinido", "Namibe", "Angola")
        );
    }

    private void saveAll(List<Sitio> sitios) {
        for (Sitio sitio : sitios) {
            System.out.println("Saving: " + sitio);
            Localidade localidade = generateLocalidade(sitio);
            localidadeRepository.save(localidade);
        }
    }

    private Localidade generateLocalidade(Sitio sitio) {
        Localidade localidade = new Localidade(sitio.getNome());
        String pai = sitio.getPai();
        String avo = sitio.getAvo();

        if (pai != null) {
            localidade.setFkLocalidadePai(
                    avo == null
                            ? localidadeRepository.findLocalidadeByNome(pai)
                            : localidadeRepository.findLocalidadeByNomeByPai(pai, avo)
            );
        }
        return localidade;
    }

    public void awaitInitialization() throws InterruptedException {
        while (!isInitialized()) {
            Thread.sleep(1000);  // Espera ativa até que a inicialização seja concluída
        }
    }


    /**
     * Escolhe aleatoriamente uma localidade angolana
     *
     * @return Localidade aleatória de Angola
     * @throws IllegalStateException se não existirem localidades angolanas
     */
    @Cacheable(value = "localidadesAngolanas")
    public Localidade escolherAleatoriamenteLocalidadeAngolana() {
        int size = localidadesAngolanas.size();
        int posicao = random.nextInt(size);
        return localidadesAngolanas.get(posicao);
    }


    /**
     * Busca todos os filhos de uma localidade pelo código do pai
     */
    @Cacheable(value = "filhosPorPai", key = "#codigoPai")
    public List<Localidade> findFilhosbyCodigoPai(int codigoPai) {
        List<Localidade> filhos = localidadeRepository.findByFkLocalidadePai_PkLocalidade(codigoPai);
        return ordenarComIndefinidoNoFinal(filhos);
    }

    /**
     * Busca todos os filhos através do avô e do pai
     */
    @Cacheable(value = "filhosPorAvoPai", key = "#nomePai + '-' + #nomeAvo")
    public List<Localidade> findFilhosbyAvoAndPai(String nomePai, String nomeAvo) {
        List<Localidade> filhos = localidadeRepository.findAllFilhosbyavoAndPai(nomePai, nomeAvo);
        return ordenarComIndefinidoNoFinal(filhos);
    }


    /**
     * Busca todos os continentes
     */
    @Cacheable(value = "continentes")
    public List<Localidade> buscarContinentes() {
        List<Localidade> continentes = localidadeRepository.findByFkLocalidadePaiIsNull();
        return ordenarComIndefinidoNoFinal(continentes);
    }

    /**
     * Busca todos os países
     */
    @Cacheable(value = "paises")
    public List<Localidade> findAllPaises() {
        List<Localidade> continentes = buscarContinentes();
        List<Localidade> paises = new ArrayList<>();

        for (Localidade continente : continentes) {
            if (!continente.getDesignacao().equals(INDEFINIDO)) {
                List<Localidade> paisesDoContinente = localidadeRepository
                        .findByFkLocalidadePai_PkLocalidade(continente.getPkLocalidade());
                paises.addAll(paisesDoContinente);
            }
        }

        return ordenarComIndefinidoNoFinal(paises);
    }

    /**
     * Ordena a lista colocando os elementos "Indefinido" e "Indefinida" no final
     */
    private List<Localidade> ordenarComIndefinidoNoFinal(List<Localidade> localidades) {
        if (localidades == null || localidades.isEmpty()) {
            return new ArrayList<>();
        }

        List<Localidade> resultado = new ArrayList<>();
        List<Localidade> indefinidos = new ArrayList<>();

        for (Localidade localidade : localidades) {
            if (localidade.getDesignacao().equals(INDEFINIDO) || localidade.getDesignacao().equals(INDEFINIDA)) {
                indefinidos.add(localidade);
            } else {
                resultado.add(localidade);
            }
        }

        // Ordena os elementos não-indefinidos alfabeticamente
        resultado.sort(Comparator.comparing(Localidade::getDesignacao));
        // Adiciona os indefinidos no final
        resultado.addAll(indefinidos);

        return resultado;
    }
}
