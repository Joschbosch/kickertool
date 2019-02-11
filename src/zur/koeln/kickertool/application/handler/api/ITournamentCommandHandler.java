package zur.koeln.kickertool.application.handler.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;

public interface ITournamentCommandHandler {

    TournamentDTO createNewTournament();

    TournamentDTO startTournament(UUID tournamentIDToStart);

    TournamentDTO renameTournament(UUID tournamentIDToRename, String name);

    List<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    List<PlayerDTO> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant);
}
