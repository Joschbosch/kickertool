package zur.koeln.kickertool.application.api.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentMode;

@NoArgsConstructor
@Getter
@Setter
public class SettingsDTO {

    private UUID uid;

    private TournamentMode mode;

    private int tableCount;

    private int randomRounds;

    private int matchesToWin;

    private int goalsToWin;

    private int minutesPerMatch;

    private int pointsForWinner;

    private int pointsForDraw;

    private boolean fixedTeams;

    private int currentNoOfMatches;

    private TournamentDTO tournament;

}
