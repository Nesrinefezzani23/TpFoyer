package tn.esprit.tpfoyer.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.TypeChambre;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByTypeC(TypeChambre type);
    Optional<Chambre> findByNumeroChambre(long numero);
    long countByTypeC(TypeChambre type);
    @Query("SELECT c FROM Chambre c LEFT JOIN FETCH c.reservation")
    List<Chambre> findAllWithReservations();
    @Query("SELECT c FROM Chambre c JOIN c.reservation r JOIN r.etudiants e WHERE e.cin = :cinEtudiant")
    Chambre findChambreByEtudiantCinJPQL(@Param("cinEtudiant") long cinEtudiant);
}
