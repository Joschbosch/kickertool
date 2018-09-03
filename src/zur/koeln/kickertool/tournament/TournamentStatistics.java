/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.PlayerPool;

@RequiredArgsConstructor
@Getter
@Setter
public class TournamentStatistics {

    private final UUID playerId;

    private List<UUID> matches = new LinkedList<>();

    @JsonIgnore
    private Map<UUID, Match> uidToMatch = new HashMap<>();

    public void addMatchResult(Match match) {
        matches.add(match.getMatchID());
        uidToMatch.put(match.getMatchID(), match);
    }

    public int calcMatchesDone() {
        return matches.size();
    }

    public int calcGoalsShot() {
        return matches.stream().mapToInt(m -> uidToMatch.get(m).getGoalsForPlayer(playerId)).sum();
    }

    public int calcGoalsConceded() {
        return matches.stream().mapToInt(m -> uidToMatch.get(m).getConcededGoalsForPlayer(playerId)).sum();
    }

    public int calcGoalDiff() {
        return calcGoalsShot() - calcGoalsConceded();
    }

    public long calcMatchesWonCount() {
        return matches.stream().filter(m -> uidToMatch.get(m).didPlayerWin(playerId)).count();
    }

    public long calcMatchesLostCount() {
        return matches.stream().filter(m -> !uidToMatch.get(m).didPlayerWin(playerId) && !uidToMatch.get(m).isDraw()).count();
    }

    public long calcMatchesDrawCount() {
        return matches.stream().filter(m -> uidToMatch.get(m).isDraw()).count();
    }

    public long calcPointsForConfiguration(TournamentConfiguration config) {
        if (PlayerPool.getInstance().getPlayerById(playerId).isDummy()) {
            return 0;
        }

        long won = calcMatchesWonCount();
        long draw = calcMatchesDrawCount();

        return won * config.getPointsForWinner() + draw * config.getPointsForDraw();

    }

    public double calcMeanPoints(TournamentConfiguration config) {
        return calcPointsForConfiguration(config) / (double) matches.size();
    }

    public void importMatchMapping(Map<UUID, Match> mapping) {
        uidToMatch = mapping;
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
