package zur.koeln.kickertool.core.domain.service.tournament.api;

import java.util.List;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public interface ITournamentRoundService {

    boolean startNewRound(Tournament tournament, List<Player> tournamentParticipants);

    List<PlayerRankingRow> getRankingByRound(Tournament tournament, List<Player> allParticipants, int round);

}
