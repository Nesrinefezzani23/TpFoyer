package tn.esprit.tpfoyer.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bloc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idBloc;
    String nomBloc;
    String capaciteBloc;

    @ManyToOne
    Foyer foyer;
    @OneToMany(mappedBy = "bloc")
    Set<Chambre> chambres;
}
