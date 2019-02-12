package zur.koeln.kickertool.application.api.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.core.model.Settings;

@Getter
@Setter
@NoArgsConstructor
public class TournamentDTO {
    private UUID uid;

    private String name;

    private TournamentStatus status;

    private Settings settings;

    private List<PlayerDTO> participants;

    private List<PlayerDTO> dummyPlayer;

    private List<MatchDTO> matches;

    private List<GameTableDTO> playtables;

    private int currentRound;
}
