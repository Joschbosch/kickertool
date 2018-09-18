package zur.koeln.kickertool.api.tournament;

import java.util.UUID;

public interface Match extends Comparable<Match> {

    MatchResult getResult();

    void setResultScores(int homeScore, int guestScore);

    UUID getMatchID();

    int getTableNo();

    Team getHomeTeam();

    Team getVisitingTeam();

    int getMatchNo();

    int getConcededGoalsForPlayer(UUID playerId);

    boolean didPlayerWin(UUID playerId);

    boolean isDraw();

    int getScoreHome();

    int getScoreVisiting();
}
