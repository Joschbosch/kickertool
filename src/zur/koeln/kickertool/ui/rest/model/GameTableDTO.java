package zur.koeln.kickertool.ui.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.tournament.GameTableStatus;

@Getter
@Setter
@NoArgsConstructor
public class GameTableDTO {

    private int tableNumber;

    private GameTableStatus status;
}
