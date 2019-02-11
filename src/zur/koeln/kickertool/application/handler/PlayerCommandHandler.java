package zur.koeln.kickertool.application.handler;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.PlayerStatisticsDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
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
    public PlayerDTO createNewPlayer(String firstName, String lastName) {
        Player newPlayer = playerService.createNewPlayer(firstName, lastName);
        return mapper.map(newPlayer, PlayerDTO.class);
    }

    @Override
    public boolean deletePlayer(UUID id) {
        playerService.deletePlayer(id);
        return true;
    }

    @Override
    public PlayerDTO updatePlayerName(UUID id, String newFirstName, String newLastName) {
        Player updatePlayer = playerService.updatePlayerName(id, newFirstName, newLastName);
        return mapper.map(updatePlayer, PlayerDTO.class);
    }

    @Override
    public List<PlayerDTO> getAllPlayer() {
        List<Player> allPlayer = playerService.getAllPlayer();
        List<PlayerDTO> allPlayerDTO = new ArrayList<>();
        allPlayer.forEach(player -> allPlayerDTO.add(mapper.map(player, PlayerDTO.class)));
        return allPlayerDTO;
    }
    @Override
    public Map<UUID, PlayerStatisticsDTO> getAllPlayerStatistics() {
        Map<UUID, PlayerStatisticsDTO> result = new HashMap<>();

        playerService.getAllPlayerStatistics().entrySet().forEach(entry ->
        result.put(entry.getKey(), mapper.map(entry.getValue(), PlayerStatisticsDTO.class)));

        return result;
    }
}
