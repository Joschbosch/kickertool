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

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private TournamentEntity tournament;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "HOME_UID")
    private TeamEntity homeTeam;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "VISITING_UID")
    private TeamEntity visitingTeam;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "TABLE_ID")
    private GameTableEntity table;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;
}
