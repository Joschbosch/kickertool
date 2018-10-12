/**
 * 
 */
package zur.koeln.kickertool.tournament.data;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.TournamentSettings;

public class PlayerTournamentStatisticsImpl
    implements PlayerTournamentStatistics {

    private UUID playerId;

    private List<UUID> matches = new LinkedList<>();

    private boolean pausing = false;

    @JsonIgnore
    private Player player;

    @JsonIgnore
    private Map<UUID, Match> uidToMatch = new HashMap<>();

    public PlayerTournamentStatisticsImpl(
        Player p) {
        this.player = p;
        this.playerId = p.getUid();
    }

    public void addMatchResult(Match match) {
		matches.add(match.getMatchID());
        getUidToMatch().put(match.getMatchID(), match);
	}

	@JsonIgnore
	public int getMatchesDone() {
		return matches.size();
	}

	@JsonIgnore
	public int getGoalsShot() {
		return matches.stream().mapToInt(m -> ((TournamentMatch) getUidToMatch().get(m)).getGoalsForPlayer(playerId)).sum();
	}

	@JsonIgnore
	public int getGoalsConceded() {
		return matches.stream().mapToInt(m -> getUidToMatch().get(m).getConcededGoalsForPlayer(playerId)).sum();
	}

	@JsonIgnore
	public int getGoalDiff() {
		return getGoalsShot() - getGoalsConceded();
	}

	@JsonIgnore
	public long getMatchesWonCount() {
		return matches.stream().filter(m -> getUidToMatch().get(m).didPlayerWin(playerId)).count();
	}

	@JsonIgnore
	public long getMatchesLostCount() {
		return matches.stream()
				.filter(m -> !getUidToMatch().get(m).didPlayerWin(playerId) && !getUidToMatch().get(m).isDraw())
				.count();
	}

	@JsonIgnore
	public long getMatchesDrawCount() {
		return matches.stream().filter(m -> getUidToMatch().get(m).isDraw()).count();
	}

	@JsonIgnore
    public long getPointsForConfiguration(TournamentSettings config2) {
		if (player.isDummy()) {
			return 0;
		}

		long won = getMatchesWonCount();
		long draw = getMatchesDrawCount();

        return won * config2.getPointsForWinner() + draw * config2.getPointsForDraw();

	}

	@JsonIgnore
    public double getMeanPoints(TournamentSettings config) {
		return getPointsForConfiguration(config) / (double) matches.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(""); //$NON-NLS-1$
        result.append(player.getName());
		return result.toString();

	}

	public void getClone(PlayerTournamentStatisticsImpl tournamentStatistics) {
		tournamentStatistics.getMatches().addAll(getMatches());
		tournamentStatistics.setUidToMatch(getUidToMatch());
		tournamentStatistics.setPlayer(player);
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
	
	@Override
	public boolean isPlayerPausing() {
		return pausing ;
	}

	public void setPlayerPausing(boolean b) {
		pausing = b;
	}

}
