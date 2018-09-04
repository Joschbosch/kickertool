/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.player.PlayerPool;

@Getter
@Setter
public class TournamentStatistics {

    private UUID playerId;

    private List<UUID> matches = new LinkedList<>();

    @JsonIgnore
    private Map<UUID, Match> uidToMatch = new HashMap<>();

    public TournamentStatistics() {

    }

    public TournamentStatistics(UUID playerId) {
        this.playerId = playerId;
    }

    public void addMatchResult(Match match) {
        matches.add(match.getMatchID());
        uidToMatch.put(match.getMatchID(), match);
    }
    @JsonIgnore
    public int getMatchesDone() {
        return matches.size();
    }
    @JsonIgnore
    public int getGoalsShot() {
        return matches.stream().mapToInt(m -> uidToMatch.get(m).getGoalsForPlayer(playerId)).sum();
    }
    @JsonIgnore
    public int getGoalsConceded() {
        return matches.stream().mapToInt(m -> uidToMatch.get(m).getConcededGoalsForPlayer(playerId)).sum();
    }
    @JsonIgnore
    public int getGoalDiff() {
        return getGoalsShot() - getGoalsConceded();
    }
    @JsonIgnore
    public long getMatchesWonCount() {
        return matches.stream().filter(m -> uidToMatch.get(m).didPlayerWin(playerId)).count();
    }
    @JsonIgnore
    public long getMatchesLostCount() {
        return matches.stream().filter(m -> !uidToMatch.get(m).didPlayerWin(playerId) && !uidToMatch.get(m).isDraw()).count();
    }
    @JsonIgnore
    public long getMatchesDrawCount() {
        return matches.stream().filter(m -> uidToMatch.get(m).isDraw()).count();
    }
    @JsonIgnore
    public long getPointsForConfiguration(TournamentConfiguration config) {
        if (PlayerPool.getInstance().getPlayerById(playerId).isDummy()) {
            return 0;
        }

        long won = getMatchesWonCount();
        long draw = getMatchesDrawCount();

        return won * config.getPointsForWinner() + draw * config.getPointsForDraw();

    }
    @JsonIgnore
    public double getMeanPoints(TournamentConfiguration config) {
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
        // result.append(String.format("%n %-20s\t %s\t%s\t%s\t%s\t%s\t%s %s", player.getName(), String.valueOf(matches.size()),
        // //$NON-NLS-1$
        // Integer.valueOf(matchesWon), Integer.valueOf(matchesLost), Integer.valueOf(matchesDraw),
        // Integer.valueOf(getGoalDiff()), Integer.valueOf(points), player.isPausingTournament() ? " (pausing)" : "")); //$NON-NLS-1$
        // //$NON-NLS-2$

        return result.toString();

    }

}
