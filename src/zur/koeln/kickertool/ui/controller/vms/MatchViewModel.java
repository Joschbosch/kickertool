package zur.koeln.kickertool.ui.controller.vms;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;

@Getter
@Setter
public class MatchViewModel {

    private UUID matchID;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;

    private UUID tournamentId;

    private TeamViewModel homeTeam;

    private TeamViewModel visitingTeam;

    private GameTableViewModel table;
	
}
