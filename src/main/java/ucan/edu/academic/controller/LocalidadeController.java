package ucan.edu.academic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucan.edu.academic.entities.Localidade;
import ucan.edu.academic.services.LocalidadeService;

import java.util.List;

@RestController
@RequestMapping("/localidades")
public class LocalidadeController {

    @Autowired
    private LocalidadeService localidadeService;

    //find all paises
    @RequestMapping("/paises")
    public List<Localidade> findAllPaises() {
        return localidadeService.findAllPaises();
    }

    //find all filhos
    @GetMapping("/filhos/{codepai}")
    public List<Localidade> findAllFilhos(@PathVariable int codepai) {
        return localidadeService.findFilhosbyCodigoPai(codepai);
    }

    //find all filhos by pai and avo
    @GetMapping("/filhos/{nomepai}/{nomeavo}")
    public List<Localidade> findAllFilhosByPai(@PathVariable String nomepai, @PathVariable String nomeavo) {
        return localidadeService.findFilhosbyAvoAndPai(nomepai, nomeavo);
    }

    @GetMapping("/findAllFilhosByPaiByAvo")
    public List<Localidade> findAllFilhosByPaiByAvo(
            @RequestParam(name = "nomePai") String nomePai,
            @RequestParam(name = "nomeAvo") String nomeAvo)
    {
        return this.localidadeService.findFilhosbyAvoAndPai(nomePai, nomeAvo);
    }
}
