package tn.esprit.tpfoyer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.dto.BlocDTO; // Import du DTO
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.services.IBlocService;

import java.util.List;

@RestController
@RequestMapping("/blocController")
@AllArgsConstructor
public class BlocController {
    final IBlocService blocService;

    @PostMapping("/addBloc")
    Bloc ajouterBloc(@RequestBody Bloc bloc){
        return blocService.addOrUpdateBloc(bloc);
    }

    @PutMapping("/updateBloc")
    Bloc modifierBloc(@RequestBody Bloc bloc){
        return blocService.addOrUpdateBloc(bloc);
    }

    @DeleteMapping("/deleteBloc/{idBloc}")
    void supprimerBloc(@PathVariable("idBloc") long id){
        blocService.deleteBloc(id);
    }

    @GetMapping("/getAllBlocs")
    List<BlocDTO> getAllBlocs(){ // Type de retour DTO
        return blocService.findAllBloc();
    }

    @GetMapping("/getBlocById/{idBloc}")
    BlocDTO getBlocById(@PathVariable("idBloc") long id){ // Type de retour DTO
        return blocService.findBlocById(id);
    }
}