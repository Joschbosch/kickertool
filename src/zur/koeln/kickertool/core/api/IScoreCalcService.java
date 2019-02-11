package zur.koeln.kickertool.core.api;

import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Tournament;

public interface IScoreCalcService {

    int getScoreForPlayerAndTournamentUntilRound(Player player1, Tournament tournament, int roundForScoring);

    int getGoalDiffForPlayerUntilRound(Player player, Tournament tournament, int roundForScoring);

    int getMatchesWonCountForPlayerUntilRound(Player player, Tournament tournament, int roundForScoring);

    int getMatchesDrawCountForPlayerUntilRound(Player player1, Tournament tournament, int roundForScoring);

}
