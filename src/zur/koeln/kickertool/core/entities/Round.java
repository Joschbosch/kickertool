/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Component
public class Round {

    private int roundNo;

    @JsonIgnore
    private final Random r = new Random();

    private  List<Match> matches = new LinkedList<>();

    private  List<Match> completeMatches = new LinkedList<>();

    private Map<UUID, PlayerStatistics> scoreTableAtEndOfRound;


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
