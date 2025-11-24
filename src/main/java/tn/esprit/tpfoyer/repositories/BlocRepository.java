package tn.esprit.tpfoyer.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Bloc;

import java.util.List;

@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {
    List<Bloc> findByFoyerIsNull();
    List<Bloc> findByCapaciteGreaterThan(Integer capacite);
    List<Bloc> findByNomStartingWith(String prefix);
    List<Bloc> findByNomStartingWithAndCapaciteGreaterThan(String prefix, Integer capacite);

}
