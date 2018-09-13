package zur.koeln.kickertool.api.tournament;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Round {

    List<Match> getMatches();

    int getRoundNo();

    Map<UUID, PlayerTournamentStatistics> getScoreTableAtEndOfRound();

    List<Match> getAllMatches();

    boolean isComplete();

}
