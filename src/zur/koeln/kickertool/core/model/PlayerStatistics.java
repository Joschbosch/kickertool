/**
 * 
 */
package zur.koeln.kickertool.core.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerStatistics {

    private UUID uid;
	
    private Player player;

    private List<Match> playedMatches = new LinkedList<>();


    public PlayerStatistics(
        Player player) {
        this.player = player;
        this.uid = UUID.randomUUID();
    }

    public void addPlayedMatch(Match m) {
        playedMatches.add(m);
    }

    public void removedPlayedMatch(Match m) {
        playedMatches.remove(m);
    }
}
