package zur.koeln.kickertool.infrastructure.adapter.persistence.orm.player.persistencemodel;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;
import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament.persistencemodel.TournamentPersistenceModelObject;


@Entity
@Table(name = "player")
@Getter
@Setter
public class PlayerPersistenceModelObject {

    @Id
    private UUID uid;

    private String firstName;

    private String lastName;

    private PlayerStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "TOURNAMENT_ID")
    private List<TournamentPersistenceModelObject> playedTournaments;

}
