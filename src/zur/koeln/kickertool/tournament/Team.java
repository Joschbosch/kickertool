/**
 * 
 */
package zur.koeln.kickertool.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;

@Getter
@Setter
@AllArgsConstructor
public class Team {

    private Player p1;
    private Player p2;
}
