package zur.koeln.kickertool.application.handler.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDTO {

    private UUID player1Id;
    private UUID player2Id;
}
