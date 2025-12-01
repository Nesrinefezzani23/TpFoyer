package tn.esprit.tpfoyer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.entities.TypeChambre;
import tn.esprit.tpfoyer.repositories.BlocRepository;
import tn.esprit.tpfoyer.repositories.ChambreRepository;
import tn.esprit.tpfoyer.repositories.ReservationRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
@Slf4j
public class ChambreServiceImpl implements IChambreService{
    final ChambreRepository chambreRepository;
    final ReservationRepository reservationRepository;
    final BlocRepository blocRepository;

    @Override
    public Chambre addOrUpdateChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    @Override
    public void deleteChambre(long id) {
        chambreRepository.deleteById(id);
    }

    @Override
    public List<Chambre> findAllChambre() {
        return chambreRepository.findAll();
    }

    @Override
    public Chambre findChambreById(long id) {
        return chambreRepository.findById(id).get();
    }

    @Override
    public Chambre assignReservationToChambre(long idChambre, String idReservation) {
        Chambre chambre = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        chambre.getReservation().add(reservation);
        return chambreRepository.save(chambre);
    }

    @Override
    public Chambre desassignReservationFromChambre(long idChambre, String idReservation) {
        Chambre chambre = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        chambre.getReservation().remove(reservation);
        return chambreRepository.save(chambre);
    }

    @Override
    public List<Chambre> findByTypeC(String type) {
        return chambreRepository.findByTypeC(type);
    }

    @Override
    public Optional<Chambre> findByNumeroChambre(long numero) {
        return chambreRepository.findByNumeroChambre(numero);
    }

    @Scheduled(cron = "0 * * * * *")
    public void listeChambresParBloc() {
        List<Bloc> blocs = blocRepository.findAllWithChambres();

        for (Bloc bloc : blocs) {
            log.info("Bloc => {} ayant une capacité {}",
                    bloc.getNomBloc() != null ? bloc.getNomBloc() : "Non Nommé",
                    bloc.getCapaciteBloc());

            Set<Chambre> chambres = bloc.getChambres();

            if (chambres != null && !chambres.isEmpty()) {
                // Log 2 : La liste des chambres pour ce bloc:
                log.info("La liste des chambres pour ce bloc:");

                for (Chambre chambre : chambres) {
                    log.info("NumChambre: {} type: {}",
                            chambre.getNumeroChambre(),
                            chambre.getTypeC() != null ? chambre.getTypeC().name() : "INCONNU");
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("*****************************");
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambres = chambreRepository.count();

        log.info("Nombre total des chambre: {}", totalChambres);

        if (totalChambres == 0) {
            log.info("Aucune chambre trouvée pour calculer les pourcentages.");
            return;
        }

        for (TypeChambre type : TypeChambre.values()) {
            long countByType = chambreRepository.countByTypeC(type);
            double pourcentage = (double) countByType / totalChambres * 100.0;
            String formattedPercentage = String.format(Locale.US, "%.1f", pourcentage);

            log.info("Le pourcentage des chambres pour le type {} est égale à {}",
                    type.name(),
                    formattedPercentage);
        }
    }

    private int getCapaciteParType(TypeChambre type) {
        switch (type) {
            case SIMPLE: return 1;
            case DOUBLE: return 2;
            case TRIPLE: return 3;
            default: return 0;
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        List<Chambre> chambres = chambreRepository.findAllWithReservations();

        for (Chambre chambre : chambres) {
            TypeChambre type = chambre.getTypeC();
            long numero = chambre.getNumeroChambre();
            int capaciteTotale = getCapaciteParType(type);
            int nbPlacesOccupees = chambre.getReservation() != null ? chambre.getReservation().size() : 0;
            int nbPlacesDisponibles = capaciteTotale - nbPlacesOccupees;
            if (nbPlacesDisponibles <= 0) {
                log.info("La chambre {} {} est complete", type, numero);
            } else {
                log.info("Le nombre de place disponible pour la chambre {} {} est {}",
                        type,
                        numero,
                        nbPlacesDisponibles);
            }
        }
    }
}
