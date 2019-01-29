/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.UUID;

import zur.koeln.kickertool.core.kernl.MatchResult;
import zur.koeln.kickertool.core.kernl.MatchStatus;

public class Match {

	private UUID matchID;
	
	private Tournament tournament;

	private Team homeTeam;

	private Team visitingTeam;

	private GameTable table;
	
	private int roundNumber;
	
	private int scoreHome;

	private int scoreVisiting;

	private MatchResult result;
	
	private MatchStatus status;


}
