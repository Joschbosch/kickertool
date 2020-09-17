package zur.koeln.kickertool.core.bl.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.bl.model.tournament.Tournament;

public interface ITournamentParticipantService {

    void addParticipantsToTournament(List<Player> participants, Tournament newTournament);

    void updateParticipantsStatus(Tournament tournament, Map<UUID, Player> playersInTournament);

    List<Player> checkDummyPlayer(Tournament tournament, List<Player> dummyPlayer, List<Player> freeDummyPlayer);

    void removeFromTournament(Tournament tournament, UUID participantId, List<Player> participants);

    boolean addParticipantToTournament(Tournament tournament, Player participant, List<Player> participants);
}
