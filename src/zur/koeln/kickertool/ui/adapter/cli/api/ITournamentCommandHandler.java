package zur.koeln.kickertool.ui.adapter.cli.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.ui.adapter.common.PlayerDTO;
import zur.koeln.kickertool.ui.adapter.common.PlayerRankingRowDTO;
import zur.koeln.kickertool.ui.adapter.common.SettingsDTO;
import zur.koeln.kickertool.ui.adapter.common.TournamentDTO;
import zur.koeln.kickertool.ui.adapter.common.base.ListResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.SingleResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.StatusOnlyDTO;

public interface ITournamentCommandHandler {

    SingleResponseDTO<TournamentDTO> createAndStartNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings);

    SingleResponseDTO<TournamentDTO> getTournamentById(UUID tournamentId);

    ListResponseDTO<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    ListResponseDTO<PlayerDTO> addParticipantsToTournament(UUID tournamentId, List<UUID> playerIds);

    ListResponseDTO<PlayerDTO> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participant);

    SingleResponseDTO<TournamentDTO> startNextTournamentRound(UUID tournamentUUID);

    ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentUUID, int round);

    StatusOnlyDTO enterOrChangeMatchResult(UUID tournamentUUID, UUID matchId, int scoreHome, int scoreVisiting);

    SingleResponseDTO<TournamentDTO> pauseOrUnpausePlayer(UUID tournamentUUID, UUID playerId, boolean pausing);


}
