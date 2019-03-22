package zur.koeln.kickertool.ui.controller.shared.vms;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;

@Getter
@Setter
public class MatchDTOViewModel {

    private UUID matchID;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;

    private UUID tournamentId;

    private TeamDTOViewModel homeTeam;

    private TeamDTOViewModel visitingTeam;

    private GameTableDTOViewModel table;
	
}
