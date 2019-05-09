package zur.koeln.kickertool.application.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusOnlyDTO;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.core.model.aggregates.Player;

@Named
public class PlayerCommandHandler
    implements IPlayerCommandHandler {

    private final IPlayerService playerService;

    private final CustomModelMapper mapper;

    @Inject
    public PlayerCommandHandler(
        IPlayerService playerService,
        CustomModelMapper mapper) {
        this.playerService = playerService;
        this.mapper = mapper;
    }

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
    public SingleResponseDTO<PlayerDTO> pauseOrUnpausePlayer(UUID playerId, boolean pausing) {
        Player player = playerService.pauseOrUnpausePlayer(playerId, pausing);
        SingleResponseDTO response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(player, PlayerDTO.class));
        return response;
    }
}
