package zur.koeln.kickertool.ui.vm;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.api.dtos.GameTableDTO;
import zur.koeln.kickertool.application.api.dtos.TeamDTO;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
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
