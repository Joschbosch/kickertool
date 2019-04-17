package zur.koeln.kickertool.application.handler.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.PlayerRankingRowDTO;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;

public interface ITournamentCommandHandler {

    SingleResponseDTO<TournamentDTO> startTournament(UUID tournamentIDToStart);

    SingleResponseDTO<TournamentDTO> renameTournament(UUID tournamentIDToRename, String name);

    ListResponseDTO<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    ListResponseDTO<PlayerDTO> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant);

    SingleResponseDTO<TournamentDTO> createNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings);

    SingleResponseDTO<TournamentDTO> startNextTournamentRound(UUID tournamentUUID);

    ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentUUID, int round);

}
