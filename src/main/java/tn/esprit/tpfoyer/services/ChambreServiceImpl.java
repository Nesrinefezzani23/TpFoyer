package tn.esprit.tpfoyer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.repositories.BlocRepository;
import tn.esprit.tpfoyer.repositories.ChambreRepository;
import tn.esprit.tpfoyer.repositories.ReservationRepository;

import java.util.List;
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
}
