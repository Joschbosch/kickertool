package zur.koeln.kickertool.application.handler.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.application.handler.commands.player.PlayerDTO;

public interface IPlayerCommandHandler {

    PlayerDTO createNewPlayer(PlayerDTO dto);
    boolean deletePlayer(UUID id);
    PlayerDTO updatePlayer(PlayerDTO id);
    List<PlayerDTO> getAllPlayer();

}
