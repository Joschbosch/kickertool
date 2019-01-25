/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.UUID;

import zur.koeln.kickertool.core.kernl.MatchResult;

public class Match {

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

	public Match(Integer roundNumber, Team homeTeam, Team visitingTeam, int matchNo) {
		this.roundNumber = roundNumber;
		this.homeTeam = homeTeam;
		this.visitingTeam = visitingTeam;
		this.matchNo = matchNo;
	}

    public void setResultScores(int scoreHome, int scoreVisiting, MatchResult result) {
		this.scoreHome = scoreHome;
		this.scoreVisiting = scoreVisiting;
        this.result = result;

	}

	public Integer getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(Integer roundNumber) {
		this.roundNumber = roundNumber;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getVisitingTeam() {
		return visitingTeam;
	}

	public void setVisitingTeam(Team visitingTeam) {
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

	public void setMatchID(UUID matchID) {
		this.matchID = matchID;
	}
}
