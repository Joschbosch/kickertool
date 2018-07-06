/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;

@Getter
@Setter
@RequiredArgsConstructor
public class Match {

    private enum MatchResult {
        HOME, VISITING, DRAW;
    }

    private static int MATCH_COUNTER;

    private UUID matchID = UUID.randomUUID();

    private final Integer roundNumber;

    private final Team homeTeam;

    private final Team visitingTeam;

    private int matchNo = MATCH_COUNTER++;

    private PlayTable table;

    private int scoreHome;

    private int scoreVisiting;

    private MatchResult result = null;

    public void setResult(int scoreHome, int scoreVisiting) {
        this.scoreHome = scoreHome;
        this.scoreVisiting = scoreVisiting;
        if (scoreHome > scoreVisiting) {
            result = MatchResult.HOME;
        } else if (scoreVisiting > scoreHome) {
            result = MatchResult.VISITING;
        } else {
            result = MatchResult.DRAW;
        }
    }

    public boolean isDraw() {
        return result == MatchResult.DRAW;
    }

    public boolean didPlayerWin(Player p) {
        if (isPlayerInHomeTeam(p)) {
            return result == MatchResult.HOME;
        } else {
            return result == MatchResult.VISITING;
        }

    }

    public Team getOpposingTeam(Player p) {
        if (isPlayerInHomeTeam(p)) {
            return visitingTeam;
        }
        return homeTeam;
    }

    public boolean matchComplete() {
        return result != null;
    }

    /**
     * @param player
     * @return
     */
    public int getGoalsForPlayer(Player player) {
        if (isPlayerInHomeTeam(player)) {
            return scoreHome;
        }
        return scoreVisiting;
    }

    /**
     * @param player
     * @return
     */
    public int getConcededGoalsForPlayer(Player player) {
        if (isPlayerInHomeTeam(player)) {
            return scoreVisiting;
        }
        return scoreHome;
    }

    private boolean isPlayerInHomeTeam(Player p) {
        return homeTeam.getP1().equals(p) || homeTeam.getP2().equals(p);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\n%-25s vs. %-25sTable %s\t" + (matchComplete() ? "%s-%s : %s" : ""),
                homeTeam.getP1().getName() + " / " + homeTeam.getP2().getName(),
                visitingTeam.getP1().getName() + " / " + visitingTeam.getP2().getName(),
                table != null ? table.getTableNumber() : "Not assigned", scoreHome, scoreVisiting, result));
        return builder.toString();
    }

    /**
     * @param player
     * @return
     */
    public Player getPartner(Player player) {
        if (isPlayerInHomeTeam(player)) {
            if (homeTeam.getP1().equals(player)) {
                return homeTeam.getP2();
            }
            return homeTeam.getP1();
        }
        if (homeTeam.getP1().equals(player)) {
            return visitingTeam.getP2();
        }
        return visitingTeam.getP1();
    }

    /**
     * @param player
     * @return
     */
    public Set<Player> getOppenents(Player player) {
        if (isPlayerInHomeTeam(player)) {
            return new HashSet<>(Arrays.asList(visitingTeam.getP1(), visitingTeam.getP2()));
        }
        return new HashSet<>(Arrays.asList(homeTeam.getP1(), homeTeam.getP2()));
    }

    /**
     * @return
     */
    public String getHomeTeamString() {
        return homeTeam.getP1().getName() + " / " + homeTeam.getP2().getName();
    }

    public String getVisitingTeamString() {
        return visitingTeam.getP1().getName() + " / " + visitingTeam.getP2().getName();
    }

}
