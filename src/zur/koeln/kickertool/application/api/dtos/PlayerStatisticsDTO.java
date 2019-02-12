package zur.koeln.kickertool.application.api.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.model.Player;

@Getter
@Setter
@NoArgsConstructor
public class PlayerStatisticsDTO {

    private UUID uid;

    private Player player;

    private List<MatchDTO> playedMatches;

}
