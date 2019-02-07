package zur.koeln.kickertool.application.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.commands.player.PlayerDTO;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.model.Player;

@Service
public class PlayerCommandHandler
    implements IPlayerCommandHandler {

    @Autowired
    private IPlayerService playerService;

    @Override
    public PlayerDTO createNewPlayer(PlayerDTO dto) {
        Player newPlayer = playerService.createNewPlayer(dto.getFirstName(), dto.getLastName());
        return new PlayerDTO(newPlayer.getFirstName(), newPlayer.getSurname(), newPlayer.getUid(), newPlayer.getStatus(), newPlayer.isDummy());
    }

    @Override
    public boolean deletePlayer(UUID id) {
        playerService.deletePlayer(id);
        return true;
    }

    @Override
    public PlayerDTO updatePlayer(PlayerDTO id) {
        Player updatePlayer = playerService.updatePlayer(new Player(id.getUid(), id.getFirstName(), id.getLastName(), false));
        return new PlayerDTO(updatePlayer.getFirstName(), updatePlayer.getSurname(), updatePlayer.getUid(), updatePlayer.getStatus(), updatePlayer.isDummy());
    }

    @Override
    public List<PlayerDTO> getAllPlayer() {
        List<Player> allPlayer = playerService.getAllPlayer();
        List<PlayerDTO> allPlayerDTO = new ArrayList<>();
        allPlayer.forEach(player -> allPlayerDTO.add(new PlayerDTO(player.getFirstName(), player.getSurname(), player.getUid(), player.getStatus(), player.isDummy())));
        return allPlayerDTO;
    }

}
