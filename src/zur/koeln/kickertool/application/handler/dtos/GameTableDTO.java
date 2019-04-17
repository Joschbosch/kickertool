package zur.koeln.kickertool.application.handler.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;

@Getter
@Setter
@NoArgsConstructor
public class GameTableDTO {

    private int tableNumber;

    private GameTableStatus status;
}
