package zur.koeln.kickertool.core.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.core.entities.Match;
import zur.koeln.kickertool.core.entities.MatchResult;
import zur.koeln.kickertool.core.entities.Team;

@Service
public class MatchService {

    public void setResultScores(Match m, int scoreHome, int scoreVisiting) {
        MatchResult result;
        if (scoreHome > scoreVisiting) {
            result = MatchResult.HOME;
        } else if (scoreVisiting > scoreHome) {
            result = MatchResult.VISITING;
        } else {
            result = MatchResult.DRAW;
        }
        m.setResultScores(scoreHome, scoreVisiting, result);
    }

    public boolean didPlayerWin(UUID playerId, Match m) {
        if (isDraw(m)) {
            return false;
        }
        if (isPlayerInHomeTeam(playerId, m)) {
            return m.getResult() == MatchResult.HOME;
        }
        return m.getResult() == MatchResult.VISITING;

    }

    public Team getOpposingTeam(UUID playerId, Match m) {
        if (isPlayerInHomeTeam(playerId, m)) {
            return m.getVisitingTeam();
        }
        return m.getHomeTeam();
    }
    /**
     * @param playerId
     * @return
     */
    public int getGoalsForPlayerInMatch(UUID playerId, Match m) {
        if (isPlayerInHomeTeam(playerId, m)) {
            return m.getScoreHome();
        }
        return m.getScoreVisiting();
    }

    /**
     * @param player
     * @return
     */
    public int getConcededGoalsForPlayer(UUID playerId, Match m) {
        if (isPlayerInHomeTeam(playerId, m)) {
            return m.getScoreVisiting();
        }
        return m.getScoreHome();
    }

    public boolean matchComplete(Match m) {
        return m.getResult() != null;
    }

    private boolean isPlayerInHomeTeam(UUID playerId, Match m) {
        return m.getHomeTeam().getPlayer1Id().equals(playerId) || m.getHomeTeam().getPlayer2Id().equals(playerId);
    }

    /**
     * @param player
     * @return
     */
    public UUID getPartner(UUID playerId, Match m) {
        if (isPlayerInHomeTeam(playerId, m)) {
            if (m.getHomeTeam().getPlayer1Id().equals(playerId)) {
                return m.getHomeTeam().getPlayer2Id();
            }
            return m.getHomeTeam().getPlayer1Id();
        }
        if (m.getHomeTeam().getPlayer1Id().equals(playerId)) {
            return m.getVisitingTeam().getPlayer2Id();
        }
        return m.getVisitingTeam().getPlayer1Id();
    }

    /**
     * @param player
     * @return
     */
    public Set<UUID> getOppenents(UUID playerId, Match m) {
        if (isPlayerInHomeTeam(playerId, m)) {
            return new HashSet<>(Arrays.asList(m.getVisitingTeam().getPlayer1Id(), m.getVisitingTeam().getPlayer2Id()));
        }
        return new HashSet<>(Arrays.asList(m.getHomeTeam().getPlayer1Id(), m.getHomeTeam().getPlayer2Id()));
    }

    public int compareMatches(Match match1, Match match2) {
        if (match2 == null) {
            return -1;
        }
        int roundCompare = match1.getRoundNumber().compareTo(match2.getRoundNumber());
        if (roundCompare != 0) {
            return roundCompare;
        }
        return Integer.compare(match1.getMatchNo(), match2.getMatchNo());
    }

    @JsonIgnore
    public boolean isDraw(Match m) {
        return m.getResult() == MatchResult.DRAW;
    }

    public Match createNewMatch(Integer roundNo, Team home, Team visiting, int matchNumber) {
        Match newMatch = new Match();
        newMatch.setRoundNumber(roundNo);
        newMatch.setHomeTeam(home);
        newMatch.setVisitingTeam(visiting);
        newMatch.setMatchNo(matchNumber);
        return newMatch;
    }
}
