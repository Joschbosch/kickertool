package zur.koeln.kickertool.application.handler.dtos;

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
