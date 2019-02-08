package zur.koeln.kickertool.application.handler.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.application.handler.commands.player.PlayerDTO;

public interface IPlayerCommandHandler {

    PlayerDTO createNewPlayer(PlayerDTO dto);
    boolean deletePlayer(UUID id);
    PlayerDTO updatePlayerName(UUID id, String newFirstName, String newLastName);
    List<PlayerDTO> getAllPlayer();

}
