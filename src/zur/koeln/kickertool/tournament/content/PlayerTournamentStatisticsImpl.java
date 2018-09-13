/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.TournamentSettings;

public class PlayerTournamentStatisticsImpl
    implements Comparable<PlayerTournamentStatistics>, PlayerTournamentStatistics {

	@JsonIgnore
	@Autowired
    private TournamentSettings config;

	@JsonIgnore
    private Map<UUID, Match> uidToMatch = new HashMap<>();

	private UUID playerId;

	@JsonIgnore
    private Player player;

	private List<UUID> matches = new LinkedList<>();

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

	@Override
    public int compareTo(PlayerTournamentStatistics o2) {
        Player player1 = player;
        Player player2 = o2.getPlayer();
		if (player1 == null) {
			return 1;
		}
		if (player2 == null) {
			return -1;
		}
		if (player1.isDummy()) {
			return 1;
		}
		if (player2.isDummy()) {
			return -1;
		}

		long pointsForConfiguration = this.getPointsForConfiguration(config) - o2.getPointsForConfiguration(config);
		if (pointsForConfiguration != 0) {
			return -Long.compare(this.getPointsForConfiguration(config), o2.getPointsForConfiguration(config));
		}

		int goalDiff = this.getGoalDiff() - o2.getGoalDiff();
		if (goalDiff != 0) {
			return -Integer.compare(this.getGoalDiff(), o2.getGoalDiff());
		}

		int wonDiff = (int) (this.getMatchesWonCount() - o2.getMatchesWonCount());
		if (wonDiff != 0) {
			return -Long.compare(this.getMatchesWonCount(), o2.getMatchesWonCount());
		}

		int drawDiff = (int) (this.getMatchesDrawCount() - o2.getMatchesDrawCount());
		if (drawDiff != 0) {
			return -Long.compare(this.getMatchesDrawCount(), o2.getMatchesDrawCount());
		}

		return player1.getName().compareTo(player2.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(""); //$NON-NLS-1$
		// result.append(String.format("%n %-20s\t %s\t%s\t%s\t%s\t%s\t%s %s",
		// player.getName(), String.valueOf(matches.size()),
		// //$NON-NLS-1$
		// Integer.valueOf(matchesWon), Integer.valueOf(matchesLost),
		// Integer.valueOf(matchesDraw),
		// Integer.valueOf(getGoalDiff()), Integer.valueOf(points),
		// player.isPausingTournament() ? " (pausing)" : "")); //$NON-NLS-1$
		// //$NON-NLS-2$

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
}
