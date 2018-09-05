/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.base.PlayerPoolService;

@Getter
@Setter
public class Match {

    private enum MatchResult {
        HOME, VISITING, DRAW;
    }

    @JsonIgnore
    @Autowired
    private PlayerPoolService playerPool;

    private UUID matchID = UUID.randomUUID();

    private Integer roundNumber;

    private Team homeTeam;

    private Team visitingTeam;

    private int matchNo;

    private int tableNo = -1;

    private int scoreHome;

    private int scoreVisiting;

    private MatchResult result = null;

    public Match() {

    }
    public Match(
        Integer roundNumber,
        Team homeTeam,
        Team visitingTeam,
        int matchNo) {
        this.roundNumber = roundNumber;
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.matchNo = matchNo;
    }

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

    public boolean didPlayerWin(UUID playerId) {
        if (isDraw()) {
            return false;
        }
        if (isPlayerInHomeTeam(playerId)) {
            return result == MatchResult.HOME;
        }
        return result == MatchResult.VISITING;

    }

    public Team getOpposingTeam(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            return visitingTeam;
        }
        return homeTeam;
    }

    public boolean matchComplete() {
        return result != null;
    }

    /**
     * @param playerId
     * @return
     */
    public int getGoalsForPlayer(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            return scoreHome;
        }
        return scoreVisiting;
    }

    /**
     * @param player
     * @return
     */
    public int getConcededGoalsForPlayer(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            return scoreVisiting;
        }
        return scoreHome;
    }

    private boolean isPlayerInHomeTeam(UUID playerId) {
        return homeTeam.getP1().equals(playerId) || homeTeam.getP2().equals(playerId);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // builder.append(String.format("%n%-25s vs. %-25sTable %s\t" + (matchComplete() ? "%s-%s : %s" : ""),
        // homeTeam.getP1().getName() + " / " + homeTeam.getP2().getName(),
        // visitingTeam.getP1().getName() + " / " + visitingTeam.getP2().getName(),
        // table != null ? Integer.valueOf(table.getTableNumber()) : "Not assigned", Integer.valueOf(scoreHome),
        // Integer.valueOf(scoreVisiting), result));
        return builder.toString();
    }

    /**
     * @param player
     * @return
     */
    public UUID getPartner(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            if (homeTeam.getP1().equals(playerId)) {
                return homeTeam.getP2();
            }
            return homeTeam.getP1();
        }
        if (homeTeam.getP1().equals(playerId)) {
            return visitingTeam.getP2();
        }
        return visitingTeam.getP1();
    }

    /**
     * @param player
     * @return
     */
    public Set<UUID> getOppenents(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            return new HashSet<>(Arrays.asList(visitingTeam.getP1(), visitingTeam.getP2()));
        }
        return new HashSet<>(Arrays.asList(homeTeam.getP1(), homeTeam.getP2()));
    }

    /**
     * @return
     */
    @JsonIgnore
    public String createHomeTeamString() {
        return playerPool.getPlayerById(homeTeam.getP1()).getName() + " / " //$NON-NLS-1$
                + playerPool.getPlayerById(homeTeam.getP2()).getName();
    }
    @JsonIgnore
    public String createVisitingTeamString() {
        return playerPool.getPlayerById(visitingTeam.getP1()).getName() + " / " //$NON-NLS-1$
                + playerPool.getPlayerById(visitingTeam.getP2()).getName();
    }
    @JsonIgnore
    public boolean isDraw() {
        return result == MatchResult.DRAW;
    }

}
