package zur.koeln.kickertool.application.handler.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;

@Getter
@Setter
@NoArgsConstructor
public class TournamentDTO {
    private UUID uid;

    private String name;

    private TournamentStatus status;

    private SettingsDTO settings;

    private List<UUID> participants;

    private List<MatchDTO> matches;

    private List<GameTableDTO> playtables;

    private int currentRound;
}
