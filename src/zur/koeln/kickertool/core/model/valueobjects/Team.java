/**
 *
 */
package zur.koeln.kickertool.core.model.valueobjects;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.model.aggregates.Player;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private UUID player1Id;
    private UUID player2Id;

    public boolean hasPlayer(Player player) {
        return player1Id.equals(player.getUid()) || player2Id.equals(player.getUid());
    }

}
