package zur.koeln.kickertool.application.handler.api;

import java.util.UUID;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.PlayerStatisticsDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.MapResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;

public interface IPlayerCommandHandler {

    SingleResponseDTO<PlayerDTO> createNewPlayer(String firstName, String lastName);
    boolean deletePlayer(UUID id);
    SingleResponseDTO<PlayerDTO> updatePlayerName(UUID id, String newFirstName, String newLastName);
    ListResponseDTO<PlayerDTO> getAllPlayer();
    MapResponseDTO<PlayerStatisticsDTO> getAllPlayerStatistics();

}
