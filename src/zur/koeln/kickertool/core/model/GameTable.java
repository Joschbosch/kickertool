/**
 * 
 */
package zur.koeln.kickertool.core.model;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;

@RequiredArgsConstructor
@Getter
@Setter
public class GameTable {

    private UUID id;

    private int tableNumber;

    private GameTableStatus status;

}
