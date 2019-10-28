package zur.koeln.kickertool.ui.adapter.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.*;

import zur.koeln.kickertool.core.application.api.IPlayerManagementService;
import zur.koeln.kickertool.core.application.api.ITournamentService;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.ui.adapter.common.PlayerDTO;
import zur.koeln.kickertool.ui.adapter.common.base.ListResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.SingleResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.StatusDTO;
import zur.koeln.kickertool.ui.adapter.common.base.StatusOnlyDTO;
import zur.koeln.kickertool.ui.configuration.CustomModelMapper;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/players")
public class PlayerRestController {

    private final IPlayerManagementService playerService;

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    @Inject
    public PlayerRestController(
        IPlayerManagementService playerService ,
        ITournamentService tournamentService,
        CustomModelMapper mapper) {
        this.playerService = playerService;
        this.tournamentService = tournamentService;
        this.mapper = mapper;

    }

	@GetMapping("/getall")
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

	@GetMapping("/getall-not-in-tournament/{tournamendId}")
	public ListResponseDTO<PlayerDTO> getAllPlayerNotInTournament(@PathVariable UUID tournamendId) {
        List<Player> allPlayer = playerService.getAllPlayer();
        List<Player> tournamentParticipants = tournamentService.getTournamentParticipants(tournamendId);
        allPlayer.removeAll(tournamentParticipants);

        List<PlayerDTO> allPlayerDTO = new ArrayList<>();
        allPlayer.forEach(player -> allPlayerDTO.add(mapper.map(player, PlayerDTO.class)));

        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValueList(mapper.map(allPlayerDTO, PlayerDTO.class));

        return response;
	}

	@PostMapping()
	public SingleResponseDTO<PlayerDTO> insertNewPlayer(@RequestBody PlayerDTO playerDTO) {
        Player newPlayer = playerService.createNewPlayer(playerDTO.getFirstName(), playerDTO.getLastName());

        SingleResponseDTO<PlayerDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setValidation(null);
        response.setDtoValue(mapper.map(newPlayer, PlayerDTO.class));

        return response;
	}

	@DeleteMapping("/{uuid}")
	public @ResponseBody StatusOnlyDTO deletePlayer(@PathVariable UUID uuid) {
        playerService.deletePlayer(uuid);
        StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
        statusOnlyDTO.setDtoStatus(StatusDTO.SUCCESS);
        return statusOnlyDTO;
	}
}
