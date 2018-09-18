/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.MatchResult;

public class TournamentMatch
    implements Match {


    private final UUID matchID = UUID.randomUUID();

    private Integer roundNumber;

    private TournamentTeam homeTeam;

    private TournamentTeam visitingTeam;

    private int matchNo;

    private int tableNo = -1;

    private int scoreHome;

    private int scoreVisiting;

    private MatchResult result = null;

    public TournamentMatch() {

    }
    public TournamentMatch(
        Integer roundNumber,
        TournamentTeam homeTeam,
        TournamentTeam visitingTeam,
        int matchNo) {
        this.roundNumber = roundNumber;
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.matchNo = matchNo;
    }

    public void setResultScores(int scoreHome, int scoreVisiting) {
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

    public TournamentTeam getOpposingTeam(UUID playerId) {
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
        return homeTeam.getPlayer1Id().equals(playerId) || homeTeam.getPlayer2Id().equals(playerId);
    }


    /**
     * @param player
     * @return
     */
    public UUID getPartner(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            if (homeTeam.getPlayer1Id().equals(playerId)) {
                return homeTeam.getPlayer2Id();
            }
            return homeTeam.getPlayer1Id();
        }
        if (homeTeam.getPlayer1Id().equals(playerId)) {
            return visitingTeam.getPlayer2Id();
        }
        return visitingTeam.getPlayer1Id();
    }

    /**
     * @param player
     * @return
     */
    public Set<UUID> getOppenents(UUID playerId) {
        if (isPlayerInHomeTeam(playerId)) {
            return new HashSet<>(Arrays.asList(visitingTeam.getPlayer1Id(), visitingTeam.getPlayer2Id()));
        }
        return new HashSet<>(Arrays.asList(homeTeam.getPlayer1Id(), homeTeam.getPlayer2Id()));
    }
	@Override
	public int compareTo(Match o) {
		if (o == null) {
			return -1;
		}
		int roundCompare = roundNumber.compareTo(((TournamentMatch) o).getRoundNumber());
		if (roundCompare != 0) {
			return roundCompare;
		}
		return Integer.compare(getMatchNo(), o.getMatchNo());
	}
    @JsonIgnore
    public boolean isDraw() {
        return result == MatchResult.DRAW;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }
    public TournamentTeam getHomeTeam() {
        return homeTeam;
    }
    public void setHomeTeam(TournamentTeam homeTeam) {
        this.homeTeam = homeTeam;
    }
    public TournamentTeam getVisitingTeam() {
        return visitingTeam;
    }
    public void setVisitingTeam(TournamentTeam visitingTeam) {
        this.visitingTeam = visitingTeam;
    }
    public int getMatchNo() {
        return matchNo;
    }
    public void setMatchNo(int matchNo) {
        this.matchNo = matchNo;
    }
    public int getScoreHome() {
        return scoreHome;
    }
    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }
    public int getScoreVisiting() {
        return scoreVisiting;
    }
    public void setScoreVisiting(int scoreVisiting) {
        this.scoreVisiting = scoreVisiting;
    }
    public MatchResult getResult() {
        return result;
    }
    public void setResult(MatchResult result) {
        this.result = result;
    }
    public UUID getMatchID() {
        return matchID;
    }
    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

}
