package zur.koeln.kickertool.core.bl.api;

import java.util.List;

import zur.koeln.kickertool.core.bl.model.misc.PlayerRankingRow;
import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.bl.model.tournament.Tournament;

public interface ITournamentRoundService {

    boolean startNewRound(Tournament tournament, List<Player> tournamentParticipants);

    List<PlayerRankingRow> getRankingByRound(Tournament tournament, List<Player> allParticipants, int round);

}
