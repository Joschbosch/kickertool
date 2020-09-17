package zur.koeln.kickertool.core.bl.model.tournament;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Settings {

    private Tournament tournament;

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
