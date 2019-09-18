package zur.koeln.kickertool.ui.adapter.common;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatchResultDTO {

    private UUID matchId;
    private int homeScore;
    private int visitingScore;
}
