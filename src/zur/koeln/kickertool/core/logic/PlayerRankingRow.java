package zur.koeln.kickertool.core.logic;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.model.aggregates.Player;

@Getter
@Setter
public class PlayerRankingRow {

    private Player player;

    private int rank;

    private int matchesPlayed;
    private int matchesWon;
    private int matchesLost;
    private int matchesDraw;
    private int goals;
    private int concededGoals;
    private int goaldiff;
    private int score;
}
