/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;

@RequiredArgsConstructor
@Getter
@Setter
public class TournamentStatistics {

    private final Player player;

    private int matchesDone = 0;

    private int matchesWon = 0;

    private int matchesLost = 0;

    private int matchesDraw = 0;

    private int points = 0;

    private int goals = 0;

    private int goalsConceded = 0;

    private List<Match> matches = new LinkedList<>();

    private Set<Player> opponents = new HashSet<>();

    private Set<Player> partners = new HashSet<>();

    public void addMatchResult(Match match, TournamentConfiguration config) {
        matchesDone++;
        if (!player.isDummy()) {
            if (match.isDraw()) {
                matchesDraw++;
                points += config.getPointsForDraw();
                goals += match.getScoreHome();
                goalsConceded += match.getScoreVisiting();
            } else {
                if (match.didPlayerWin(player)) {
                    matchesWon++;
                    points += config.getPointsForWinner();
                } else {
                    matchesLost++;
                }
                goals += match.getGoalsForPlayer(player);
                goalsConceded += match.getConcededGoalsForPlayer(player);
            }
        }
        partners.add(match.getPartner(player));
        opponents.addAll(match.getOppenents(player));
    }

    public int getGoalDiff() {
        return goals - goalsConceded;
    }

    public double meanPoints() {
        return points / (double) matchesDone;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(""); //$NON-NLS-1$
        result.append(String.format("%n %-20s\t %s\t%s\t%s\t%s\t%s\t%s %s", player.getName(), Integer.valueOf(matchesDone), //$NON-NLS-1$
                Integer.valueOf(matchesWon), Integer.valueOf(matchesLost), Integer.valueOf(matchesDraw),
                Integer.valueOf(getGoalDiff()), Integer.valueOf(points), player.isPausingTournament() ? " (pausing)" : "")); //$NON-NLS-1$ //$NON-NLS-2$

        return result.toString();

    }
}
