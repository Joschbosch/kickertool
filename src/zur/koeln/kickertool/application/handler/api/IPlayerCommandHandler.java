package zur.koeln.kickertool.application.handler.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.PlayerStatisticsDTO;

public interface IPlayerCommandHandler {

    PlayerDTO createNewPlayer(String firstName, String lastName);
    boolean deletePlayer(UUID id);
    PlayerDTO updatePlayerName(UUID id, String newFirstName, String newLastName);
    List<PlayerDTO> getAllPlayer();
    Map<UUID, PlayerStatisticsDTO> getAllPlayerStatistics();

}
