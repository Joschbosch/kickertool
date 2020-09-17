package zur.koeln.kickertool.ui.rest.model;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.tournament.TournamentStatus;

@Getter
@Setter
@NoArgsConstructor
public class TournamentDTO {
    private UUID uid;

    private String name;

    private TournamentStatus status;

    private SettingsDTO settings;

    private List<MatchDTO> matches;

    private List<GameTableDTO> playtables;

    private int currentRound;
}
