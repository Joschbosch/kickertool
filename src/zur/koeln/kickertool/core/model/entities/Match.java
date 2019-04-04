/**
 * 
 */
package zur.koeln.kickertool.core.model.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.model.valueobjects.Team;

@Getter
@Setter
@NoArgsConstructor
public class Match {

    private UUID matchID;
	
	private Tournament tournament;

	private Team homeTeam;

	private Team visitingTeam;

	private GameTable table;
	
	private int roundNumber;
	
	private int scoreHome;

	private int scoreVisiting;
	
	private MatchStatus status;


    public Match(
        UUID randomUUID) {
        this.matchID = randomUUID;
    }


    public boolean hasPlayerWon(Player player) {
        if (status == MatchStatus.FINISHED) {
            if (homeTeam.hasPlayer(player)) {
                return scoreHome > scoreVisiting;
            }
            if (visitingTeam.hasPlayer(player)) {
                return scoreHome < scoreVisiting;
            }
        }
        return false;
    }

    public boolean isDraw() {
        return status == MatchStatus.FINISHED && scoreHome == scoreVisiting;
    }

}
