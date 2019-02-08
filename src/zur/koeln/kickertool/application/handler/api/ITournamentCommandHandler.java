package zur.koeln.kickertool.application.handler.api;

import java.util.UUID;

import zur.koeln.kickertool.application.api.dtos.TournamentDTO;

public interface ITournamentCommandHandler {

    TournamentDTO createNewTournament();

    TournamentDTO startTournament(UUID tournamentIDToStart);

    TournamentDTO renameTournament(UUID tournamentIDToRename, String name);

    boolean addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    boolean removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant);
}
