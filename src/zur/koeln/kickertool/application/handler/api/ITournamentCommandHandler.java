package zur.koeln.kickertool.application.handler.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.PlayerRankingRowDTO;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusOnlyDTO;

public interface ITournamentCommandHandler {

    SingleResponseDTO<TournamentDTO> createAndStartNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings);

    SingleResponseDTO<TournamentDTO> getTournamentById(UUID tournamentId);

    ListResponseDTO<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    ListResponseDTO<PlayerDTO> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participant);

    SingleResponseDTO<TournamentDTO> startNextTournamentRound(UUID tournamentUUID);

    ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentUUID, int round);

    StatusOnlyDTO enterOrChangeMatchResult(UUID tournamentUUID, UUID matchId, int scoreHome, int scoreVisiting);

    SingleResponseDTO<TournamentDTO> pauseOrUnpausePlayer(UUID tournamentUUID, UUID playerId, boolean pausing);

}
