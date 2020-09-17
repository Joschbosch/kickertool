package zur.koeln.kickertool.infrastructure.persistence.persistencemodel.tournament;

import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.tournament.MatchStatus;

@Entity
@Table(name = "matches")
@Getter
@Setter
public class MatchPersistenceModelObject {

    @Id
    private UUID matchID;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private TournamentPersistenceModelObject tournament;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "HOME_UID")
    private TeamPersistenceModelObject homeTeam;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "VISITING_UID")
    private TeamPersistenceModelObject visitingTeam;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "TABLE_ID")
    private GameTablePersistenceModelObject table;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;
}
