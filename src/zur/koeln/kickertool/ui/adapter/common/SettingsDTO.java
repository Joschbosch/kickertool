package zur.koeln.kickertool.ui.adapter.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.tournament.TournamentMode;

@NoArgsConstructor
@Getter
@Setter
public class SettingsDTO {

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

}
