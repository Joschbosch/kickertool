package zur.koeln.kickertool.application.handler;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.commands.player.PlayerDTO;
import zur.koeln.kickertool.application.handler.commands.player.PlayerStatisticsDTO;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.model.Player;

@Service
public class PlayerCommandHandler
    implements IPlayerCommandHandler {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public PlayerDTO createNewPlayer(PlayerDTO dto) {
        Player newPlayer = playerService.createNewPlayer(dto.getFirstName(), dto.getLastName());
        return new PlayerDTO(newPlayer.getFirstName(), newPlayer.getLastName(), newPlayer.getUid(), newPlayer.getStatus(), newPlayer.isDummy());
    }

    @Override
    public boolean deletePlayer(UUID id) {
        playerService.deletePlayer(id);
        return true;
    }

    @Override
    public PlayerDTO updatePlayerName(UUID id, String newFirstName, String newLastName) {
        Player updatePlayer = playerService.updatePlayerName(id, newFirstName, newLastName);
        return new PlayerDTO(updatePlayer.getFirstName(), updatePlayer.getLastName(), updatePlayer.getUid(), updatePlayer.getStatus(), updatePlayer.isDummy());
    }

    @Override
    public List<PlayerDTO> getAllPlayer() {
        List<Player> allPlayer = playerService.getAllPlayer();
        List<PlayerDTO> allPlayerDTO = new ArrayList<>();
        allPlayer.forEach(player -> allPlayerDTO.add(new PlayerDTO(player.getFirstName(), player.getLastName(), player.getUid(), player.getStatus(), player.isDummy())));
        return allPlayerDTO;
    }
    @Override
    public Map<UUID, PlayerStatisticsDTO> getAllPlayerStatistics() {
        Map<UUID, PlayerStatisticsDTO> result = new HashMap<>();

        playerService.getAllPlayerStatistics().entrySet().forEach(entry ->
        result.put(entry.getKey(), mapper.map(entry.getValue(), PlayerStatisticsDTO.class))
        );

        return result;
    }
}
