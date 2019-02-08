package zur.koeln.kickertool.application.handler.commands.player;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.model.Player;

@Getter
@Setter
public class PlayerStatisticsDTO {

    private UUID uid;

    private Player player;

    private List<MatchDTO> playedMatches;

}
