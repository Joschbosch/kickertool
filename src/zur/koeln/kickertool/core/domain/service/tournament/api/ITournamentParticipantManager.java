package zur.koeln.kickertool.core.domain.service.tournament.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public interface ITournamentParticipantManager {

    void addParticipantsToTournament(List<Player> participants, Tournament newTournament);

    void updateParticipantsStatus(Tournament tournament, Map<UUID, Player> playersInTournament);

    List<Player> checkDummyPlayer(Tournament tournament, List<Player> dummyPlayer, List<Player> freeDummyPlayer);

    void removeFromTournament(Tournament tournament, UUID participantId, List<Player> participants);

    boolean addParticipantToTournament(Tournament tournament, Player participant, List<Player> participants);
}
