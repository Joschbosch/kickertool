package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;

@Entity
@Table(name = "matches")
@Getter
@Setter
public class MatchEntity {

    @Id
    private UUID matchID;

    @OneToOne
    private TournamentEntity tournament;

    @OneToOne
    @JoinColumn(name = "HOME_UID")
    private TeamEntity homeTeam;

    @OneToOne
    @JoinColumn(name = "VISITING_UID")
    private TeamEntity visitingTeam;

    @OneToOne
    @JoinColumn(name = "TABLE_ID")
    private GameTableEntity table;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;
}
