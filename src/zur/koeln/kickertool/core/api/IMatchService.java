package zur.koeln.kickertool.core.api;

import java.util.UUID;

import zur.koeln.kickertool.core.model.Match;
import zur.koeln.kickertool.core.model.Player;

public interface IMatchService {

    void createNextMatches(int newRoundNumber, UUID tournamentUID);

    boolean hasPlayerWon(Match m, Player player);

    int getScoreForPlayer(Match m, Player player);

}
