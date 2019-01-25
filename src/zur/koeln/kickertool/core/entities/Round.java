/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class Round {

    private int roundNo;

    private  List<Match> matches = new LinkedList<>();

    private  List<Match> completeMatches = new LinkedList<>();

    private Map<UUID, PlayerStatistics> scoreTableAtEndOfRound;


    public boolean isComplete() {
        return matches.isEmpty();
    }

    public List<Match> getAllMatches() {
        List<Match> allMatches = new LinkedList<>(matches);
        allMatches.addAll(completeMatches);
        return allMatches;
    }
    

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    public Map<UUID, PlayerStatistics> getScoreTableAtEndOfRound() {
        return scoreTableAtEndOfRound;
    }

    public void setScoreTableAtEndOfRound(Map<UUID, PlayerStatistics> scoreTableAtEndOfRound) {
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
