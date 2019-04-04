package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentMode;

@Entity
@Table(name = "settings")
@Getter
@Setter
public class SettingsEntity {

    @Id
    @GeneratedValue
    private UUID uid;

    private TournamentMode mode = TournamentMode.SWISS_TUPEL;

    private int tableCount;

    private int randomRounds;

    private int matchesToWin;

    private int goalsToWin;

    private int minutesPerMatch;

    private int pointsForWinner;

    private int pointsForDraw;

    private boolean fixedTeam;

    private int currentNoOfMatches;

    @OneToOne
    @JoinColumn(name = "TOURNAMENT_UID")
    private TournamentEntity tournament;


}
