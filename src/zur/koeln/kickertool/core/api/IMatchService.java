package zur.koeln.kickertool.core.api;

import zur.koeln.kickertool.core.model.Match;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Tournament;

public interface IMatchService {

    void createNextMatches(int newRoundNumber, Tournament tournamentUID);

    boolean hasPlayerWon(Match m, Player player);

    int getScoreForPlayer(Match m, Player player);

}
