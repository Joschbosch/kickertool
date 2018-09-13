package zur.koeln.kickertool.tournament;

import zur.koeln.kickertool.api.TournamentMode;
import zur.koeln.kickertool.api.content.TournamentSettings;

public class TournamentConfig
    implements TournamentSettings {

    private TournamentMode mode = TournamentMode.SWISS_TUPEL;

    private int tableCount = 2;

    private int randomRounds = 2;

    private int matchesToWin = 1;

    private int goalsToWin = 10;

    private int minutesPerMatch = 5;

    private int pointsForWinner = 2;

    private int pointsForDraw = 1;

    private boolean fixedTeams = false;

    private int currentNoOfMatches = 0;

    public TournamentMode getMode() {
        return mode;
    }

    public void setMode(TournamentMode mode) {
        this.mode = mode;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public int getRandomRounds() {
        return randomRounds;
    }

    public void setRandomRounds(int randomRounds) {
        this.randomRounds = randomRounds;
    }

    public int getMatchesToWin() {
        return matchesToWin;
    }

    public void setMatchesToWin(int matchesToWin) {
        this.matchesToWin = matchesToWin;
    }

    public int getGoalsToWin() {
        return goalsToWin;
    }

    public void setGoalsToWin(int goalsToWin) {
        this.goalsToWin = goalsToWin;
    }

    public int getMinutesPerMatch() {
        return minutesPerMatch;
    }

    public void setMinutesPerMatch(int minutesPerMatch) {
        this.minutesPerMatch = minutesPerMatch;
    }

    public int getPointsForWinner() {
        return pointsForWinner;
    }

    public void setPointsForWinner(int pointsForWinner) {
        this.pointsForWinner = pointsForWinner;
    }

    public int getPointsForDraw() {
        return pointsForDraw;
    }

    public void setPointsForDraw(int pointsForDraw) {
        this.pointsForDraw = pointsForDraw;
    }

    public boolean isFixedTeams() {
        return fixedTeams;
    }

    public void setFixedTeams(boolean fixedTeams) {
        this.fixedTeams = fixedTeams;
    }

    public int getCurrentNoOfMatches() {
        return currentNoOfMatches;
    }

    public void setCurrentNoOfMatches(int currentNoOfMatches) {
        this.currentNoOfMatches = currentNoOfMatches;
    }

}
