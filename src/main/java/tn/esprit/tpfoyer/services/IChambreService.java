package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Reservation;

import java.util.List;

public interface IChambreService {
    Chambre addOrUpdateChambre(Chambre chambre);
    void deleteChambre(long id);
    List<Chambre> findAllChambre();
    Chambre findChambreById(long id);
    //Affectation & Desaffectation
    Chambre addChambreAndReservation(Chambre chambre, Reservation reservation);
    Chambre assignReservationToChambre(long idChambre, String idReservation);
    Chambre desassignReservationFromChambre(long idChambre, String idReservation);
}
