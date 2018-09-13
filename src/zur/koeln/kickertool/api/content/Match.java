package zur.koeln.kickertool.api.content;

import java.util.UUID;

import zur.koeln.kickertool.api.MatchResult;

public interface Match {

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

}
