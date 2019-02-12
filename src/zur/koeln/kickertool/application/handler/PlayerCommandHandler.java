package zur.koeln.kickertool.application.handler;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.PlayerStatisticsDTO;
import zur.koeln.kickertool.application.api.dtos.base.*;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.core.model.Player;

@Service
public class PlayerCommandHandler
    implements IPlayerCommandHandler {

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private CustomModelMapper mapper;

    @Override
    public SingleResponseDTO<PlayerDTO> createNewPlayer(String firstName, String lastName) {
        Player newPlayer = playerService.createNewPlayer(firstName, lastName);

        SingleResponseDTO<PlayerDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValue(mapper.map(newPlayer, PlayerDTO.class));

        return response;
    }

    @Override
    public StatusOnlyDTO deletePlayer(UUID id) {
        playerService.deletePlayer(id);
        StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
        statusOnlyDTO.setDtoStatus(StatusDTO.SUCCESS);
        return statusOnlyDTO;
    }

    @Override
    public SingleResponseDTO<PlayerDTO> updatePlayerName(UUID id, String newFirstName, String newLastName) {
        Player updatePlayer = playerService.updatePlayerName(id, newFirstName, newLastName);
        SingleResponseDTO<PlayerDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValue(mapper.map(updatePlayer, PlayerDTO.class));

        return response;
    }

    @Override
    public ListResponseDTO<PlayerDTO> getAllPlayer() {
        List<Player> allPlayer = playerService.getAllPlayer();
        List<PlayerDTO> allPlayerDTO = new ArrayList<>();
        allPlayer.forEach(player -> allPlayerDTO.add(mapper.map(player, PlayerDTO.class)));

        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValueList(mapper.map(allPlayerDTO, PlayerDTO.class));

        return response;
    }
    @Override
    public MapResponseDTO<PlayerStatisticsDTO> getAllPlayerStatistics() {
        Map<UUID, PlayerStatisticsDTO> result = new HashMap<>();

        playerService.getAllPlayerStatistics().entrySet().forEach(entry ->
        result.put(entry.getKey(), mapper.map(entry.getValue(), PlayerStatisticsDTO.class)));

        MapResponseDTO<PlayerStatisticsDTO> response = new MapResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setMapDTOValue(result);

        return response;
    }
}
