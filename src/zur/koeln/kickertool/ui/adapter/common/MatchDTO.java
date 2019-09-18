package zur.koeln.kickertool.ui.adapter.common;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.tournament.MatchStatus;

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
