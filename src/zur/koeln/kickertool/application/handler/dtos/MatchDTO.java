package zur.koeln.kickertool.application.handler.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;

@Getter
@Setter
@NoArgsConstructor
public class MatchDTO {

    private UUID matchID;

    private int roundNumber;

    private int scoreHome;

    private int scoreVisiting;

    private MatchStatus status;

    private TeamDTO homeTeam;

    private TeamDTO visitingTeam;

    private String gameTableDescription;

}
