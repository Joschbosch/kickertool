package zur.koeln.kickertool.application.api.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDTO {

    private UUID uid;

    private PlayerDTO player1;
    private PlayerDTO player2;
}
