package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentMode;

@Entity
@Table(name = "settings")
@Getter
@Setter
public class SettingsEntity {

    @Id
    private UUID uid;

    private TournamentMode mode = TournamentMode.SWISS_TUPEL;

    private int tableCount;

    private int randomRounds;

    private int matchesToWin;

    private int goalsToWin = 10;

    private int minutesPerMatch;

    private int pointsForWinner;

    private int pointsForDraw;

    private boolean fixedTeam;

    private int currentNoOfMatches;
}
