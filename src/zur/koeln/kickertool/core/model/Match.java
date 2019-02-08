/**
 * 
 */
package zur.koeln.kickertool.core.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.MatchStatus;

@Getter
@Setter
public class Match {

	private UUID matchID;
	
	private Tournament tournament;

	private Team homeTeam;

	private Team visitingTeam;

	private GameTable table;
	
	private int roundNumber;
	
	private int scoreHome;

	private int scoreVisiting;
	
	private MatchStatus status;


}