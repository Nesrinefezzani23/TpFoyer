package tn.esprit.tpfoyer.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Reservation {
    @Id
    String idReservation;
    long anneeUniversitaire;
    boolean estValide;

    @ManyToMany
    Set<Etudiant> etudiants;
}
