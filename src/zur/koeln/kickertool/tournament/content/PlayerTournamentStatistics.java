/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.base.PlayerPoolService;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.TournamentConfig;

public class PlayerTournamentStatistics
    implements Comparable<PlayerTournamentStatistics> {

    @JsonIgnore
    @Autowired
    private PlayerPoolService playerPool;

    @JsonIgnore
    @Autowired
    private TournamentConfig config;

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
        return matches.stream().mapToInt(m -> getUidToMatch().get(m).getGoalsForPlayer(playerId)).sum();
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
        return matches.stream().filter(m -> !getUidToMatch().get(m).didPlayerWin(playerId) && !getUidToMatch().get(m).isDraw()).count();
    }
    @JsonIgnore
    public long getMatchesDrawCount() {
        return matches.stream().filter(m -> getUidToMatch().get(m).isDraw()).count();
    }
    @JsonIgnore
    public long getPointsForConfiguration(TournamentConfig config) {
        if (playerPool.getPlayerById(playerId).isDummy()) {
            return 0;
        }

        long won = getMatchesWonCount();
        long draw = getMatchesDrawCount();

        return won * config.getPointsForWinner() + draw * config.getPointsForDraw();

    }
    @JsonIgnore
    public double getMeanPoints(TournamentConfig config) {
        return getPointsForConfiguration(config) / (double) matches.size();
    }
    @Override
    public int compareTo(PlayerTournamentStatistics o2) {
        Player player1 = playerPool.getPlayerById(this.getPlayerId());
        Player player2 = playerPool.getPlayerById(o2.getPlayerId());
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
        long pointsForConfiguration = this.getPointsForConfiguration(config);
        long pointsForConfiguration2 = o2.getPointsForConfiguration(config);
        if (pointsForConfiguration < pointsForConfiguration2) {
            return 1;
        } else if (pointsForConfiguration > pointsForConfiguration2) {
            return -1;
        }
        int goalDiff = this.getGoalDiff();
        int goalDiff2 = o2.getGoalDiff();

        if (goalDiff < goalDiff2) {
            return 1;
        }
        if (goalDiff > goalDiff2) {
            return -1;
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
        // result.append(String.format("%n %-20s\t %s\t%s\t%s\t%s\t%s\t%s %s", player.getName(), String.valueOf(matches.size()),
        // //$NON-NLS-1$
        // Integer.valueOf(matchesWon), Integer.valueOf(matchesLost), Integer.valueOf(matchesDraw),
        // Integer.valueOf(getGoalDiff()), Integer.valueOf(points), player.isPausingTournament() ? " (pausing)" : "")); //$NON-NLS-1$
        // //$NON-NLS-2$

        return result.toString();

    }
    public void getClone(PlayerTournamentStatistics tournamentStatistics) {
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
        this.player = playerPool.getPlayerById(playerId);
    }
}
