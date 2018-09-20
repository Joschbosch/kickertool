package zur.koeln.kickertool.api.tournament;

import java.util.UUID;

import zur.koeln.kickertool.api.player.Player;

public interface PlayerTournamentStatistics
    extends Comparable<PlayerTournamentStatistics> {

    Player getPlayer();

    long getPointsForConfiguration(TournamentSettings config);

    int getGoalDiff();

    long getMatchesWonCount();

    long getMatchesDrawCount();

    UUID getPlayerId();

    void addMatchResult(Match m);

    int getMatchesDone();

    long getMatchesLostCount();

    int getGoalsShot();

    int getGoalsConceded();
    
    boolean isPlayerPausing();

}
