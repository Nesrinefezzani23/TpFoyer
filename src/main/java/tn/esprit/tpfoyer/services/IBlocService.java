package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.dto.BlocDTO; // Import du DTO

import java.util.List;

public interface IBlocService {

    // Ajout/Modification utilise l'Entité pour la persistance
    Bloc addOrUpdateBloc(Bloc bloc);

    void deleteBloc(long id);

    // Consultation utilise le DTO pour l'API
    List<BlocDTO> findAllBloc();
    BlocDTO findBlocById(long id);
}