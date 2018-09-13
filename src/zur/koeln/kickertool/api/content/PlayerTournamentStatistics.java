package zur.koeln.kickertool.api.content;

import java.util.UUID;

import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.TournamentConfig;

public interface PlayerTournamentStatistics
    extends Comparable<PlayerTournamentStatistics> {

    Player getPlayer();

    long getPointsForConfiguration(TournamentConfig config);

    int getGoalDiff();

    long getMatchesWonCount();

    long getMatchesDrawCount();

    UUID getPlayerId();

    void addMatchResult(Match m);

    int getMatchesDone();

    long getMatchesLostCount();

    int getGoalsShot();

    int getGoalsConceded();

}
