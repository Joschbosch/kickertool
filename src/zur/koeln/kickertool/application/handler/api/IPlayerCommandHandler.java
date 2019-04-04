package zur.koeln.kickertool.application.handler.api;

import java.util.UUID;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.StatusOnlyDTO;

public interface IPlayerCommandHandler {

    SingleResponseDTO<PlayerDTO> createNewPlayer(String firstName, String lastName);
    StatusOnlyDTO deletePlayer(UUID id);
    SingleResponseDTO<PlayerDTO> updatePlayerName(UUID id, String newFirstName, String newLastName);
    ListResponseDTO<PlayerDTO> getAllPlayer();

    SingleResponseDTO<PlayerDTO> pauseOrUnpausePlayer(UUID playerId, boolean pausing);
}
