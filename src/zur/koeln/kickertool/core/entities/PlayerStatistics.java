/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStatistics {

    private UUID uid;
	
    private Player player;

	private final List<Match> playedMatches = new LinkedList<>();

}
