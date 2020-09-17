/**
 *
 */
package zur.koeln.kickertool.core.bl.model.tournament;

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
