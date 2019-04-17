package zur.koeln.kickertool.application.handler.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamDTO {

    private PlayerDTO player1;
    private PlayerDTO player2;
}
