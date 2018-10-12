/**
 * 
 */
package zur.koeln.kickertool.tournament.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;

public class TournamentRound
    implements Round {

    private final int roundNo;

    private  List<Match> matches = new LinkedList<>();

    private  List<Match> completeMatches = new LinkedList<>();

    private Map<UUID, PlayerTournamentStatistics> scoreTableAtEndOfRound;


    public TournamentRound(
        int nextRoundNumber) {
        this.roundNo = nextRoundNumber;
    }

    public void addMatchResult(Match m) throws MatchException {
        if (matches.contains(m)) {
            matches.remove(m);
            completeMatches.add(m);

        } else {
            throw new MatchException();
        }
    }

    @Override
    public void addMatch(Match m) {
        matches.add(m);
    }

    @JsonIgnore
    public boolean isComplete() {
        return matches.isEmpty();
    }
    @JsonIgnore
    public List<Match> getAllMatches() {
        List<Match> allMatches = new LinkedList<>(matches);
        allMatches.addAll(completeMatches);
        return allMatches;
    }
    
    @Override
    public int compareTo(Round o) {
    	if (o == null) {
    		return -1;
    	}
    
    	return Integer.compare(getRoundNo(), o.getRoundNo());
    }

    public int getRoundNo() {
        return roundNo;
    }


    public Map<UUID, PlayerTournamentStatistics> getScoreTableAtEndOfRound() {
        return scoreTableAtEndOfRound;
    }

    public void setScoreTableAtEndOfRound(Map<UUID, PlayerTournamentStatistics> scoreTableAtEndOfRound) {
        this.scoreTableAtEndOfRound = scoreTableAtEndOfRound;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Match> getCompleteMatches() {
        return completeMatches;
    }

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public void setCompleteMatches(List<Match> completeMatches) {
		this.completeMatches = completeMatches;
	}



}
