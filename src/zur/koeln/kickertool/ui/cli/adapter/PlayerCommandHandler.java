package zur.koeln.kickertool.ui.cli.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.ports.ui.IPlayerManagementService;
import zur.koeln.kickertool.core.ports.ui.ITournamentService;
import zur.koeln.kickertool.ui.common.ListResponseDTO;
import zur.koeln.kickertool.ui.common.SingleResponseDTO;
import zur.koeln.kickertool.ui.common.StatusDTO;
import zur.koeln.kickertool.ui.common.StatusOnlyDTO;
import zur.koeln.kickertool.ui.rest.adapter.tools.CustomModelMapper;
import zur.koeln.kickertool.ui.rest.model.PlayerDTO;

@Named
public class PlayerCommandHandler {

    private final IPlayerManagementService playerService;

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    @Inject
    public PlayerCommandHandler(
        IPlayerManagementService playerService,
        ITournamentService tournamentService,
        CustomModelMapper mapper) {
        this.playerService = playerService;
        this.tournamentService = tournamentService;
        this.mapper = mapper;
    }

    public SingleResponseDTO<PlayerDTO> createNewPlayer(String firstName, String lastName) {
        Player newPlayer = playerService.createNewPlayer(firstName, lastName);

        SingleResponseDTO<PlayerDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValue(mapper.map(newPlayer, PlayerDTO.class));

        return response;
    }

    public StatusOnlyDTO deletePlayer(UUID id) {
        playerService.deletePlayer(id);
        StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
        statusOnlyDTO.setDtoStatus(StatusDTO.SUCCESS);
        return statusOnlyDTO;
    }

    public SingleResponseDTO<PlayerDTO> updatePlayerName(UUID id, String newFirstName, String newLastName) {
        Player updatePlayer = playerService.updatePlayerName(id, newFirstName, newLastName);
        SingleResponseDTO<PlayerDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValue(mapper.map(updatePlayer, PlayerDTO.class));

        return response;
    }

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

	public ListResponseDTO<PlayerDTO> getAllPlayerNotInTournament(UUID tournamentId) {

		List<Player> allPlayer = playerService.getAllPlayer();
		List<Player> tournamentParticipants = tournamentService.getTournamentParticipants(tournamentId);
		allPlayer.removeAll(tournamentParticipants);

		List<PlayerDTO> allPlayerDTO = new ArrayList<>();
        allPlayer.forEach(player -> allPlayerDTO.add(mapper.map(player, PlayerDTO.class)));

        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValueList(mapper.map(allPlayerDTO, PlayerDTO.class));

        return response;
	}

}
