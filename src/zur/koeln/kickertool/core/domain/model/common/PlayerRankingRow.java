package zur.koeln.kickertool.core.domain.model.common;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;

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
