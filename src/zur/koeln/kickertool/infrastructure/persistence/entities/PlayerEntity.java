package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;


@Entity
@Table(name = "player")
@Getter
@Setter
public class PlayerEntity {

    @Id
    private UUID uid;

    private String firstName;

    private String lastName;

    private PlayerStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "TOURNAMENT_ID")
    private List<TournamentEntity> playedTournaments;

}
