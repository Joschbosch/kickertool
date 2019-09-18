package zur.koeln.kickertool.ui.adapter.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerRankingRowDTO {
    private PlayerDTO player;

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
