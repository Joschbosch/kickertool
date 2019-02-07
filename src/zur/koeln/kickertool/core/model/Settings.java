package zur.koeln.kickertool.core.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentMode;

@Getter
@Setter
public class Settings {

    private UUID uid;

    private TournamentMode mode = TournamentMode.SWISS_TUPEL;

    private int tableCount = 2;

    private int randomRounds = 2;

    private int matchesToWin = 1;

    private int goalsToWin = 10;

    private int minutesPerMatch = 5;

    private int pointsForWinner = 2;

    private int pointsForDraw = 1;

    private boolean fixedTeams = false;

    private int currentNoOfMatches = 0;

 

}
