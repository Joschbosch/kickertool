package zur.koeln.kickertool.application.api.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;

@Getter
@Setter
@NoArgsConstructor
public class MatchDTO
    extends DTO {

    private UUID matchID;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;

    private TournamentDTO tournament;

    private TeamDTO homeTeam;

    private TeamDTO visitingTeam;

    private GameTableDTO table;

}
