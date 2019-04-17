/**
 *
 */
package zur.koeln.kickertool.core.model.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;

@RequiredArgsConstructor
@Getter
@Setter
public class GameTable {

    private int tableNumber;

    private GameTableStatus status;

}
