/**
 *
 */
package zur.koeln.kickertool.core.domain.model.entities.tournament;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class GameTable {

    private int tableNumber;

    private GameTableStatus status;

}
