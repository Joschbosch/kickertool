/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.*;

public class PlayerStatistics {
    private Map<UUID, Match> uidToMatch = new HashMap<>();

	private UUID playerId;

    private Player player;

	private List<UUID> matches = new LinkedList<>();

	private boolean pausing = false;

    public void addMatchResult(Match match) {
		matches.add(match.getMatchID());
        getUidToMatch().put(match.getMatchID(), match);
	}

    public Map<UUID, Match> getUidToMatch() {
		return uidToMatch;
	}

    public void setUidToMatch(Map<UUID, Match> uidToMatch) {
		this.uidToMatch = uidToMatch;
	}

	public List<UUID> getMatches() {
		return matches;
	}

    public Player getPlayer() {
		return player;
	}

	public UUID getPlayerId() {
		return playerId;
	}

    public void setPlayer(Player player) {
		this.player = player;
	}

	public void setMatches(List<UUID> matches) {
		this.matches = matches;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}
	
	public boolean isPlayerPausing() {
		return pausing ;
	}

	public void setPlayerPausing(boolean b) {
		pausing = b;
	}

}
