/**
 * 
 */
package zur.koeln.kickertool.tournament;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayTable {

    private final int tableNumber;

    private boolean active = true;

    private boolean inUse = false;

}
