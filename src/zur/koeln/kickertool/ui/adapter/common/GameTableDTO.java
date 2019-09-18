package zur.koeln.kickertool.ui.adapter.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.tournament.GameTableStatus;

@Getter
@Setter
@NoArgsConstructor
public class GameTableDTO {

    private int tableNumber;

    private GameTableStatus status;
}
