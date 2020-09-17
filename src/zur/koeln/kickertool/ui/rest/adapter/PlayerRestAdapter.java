package zur.koeln.kickertool.ui.rest.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import lombok.Getter;
import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.ports.ui.IPlayerManagementService;
import zur.koeln.kickertool.core.ports.ui.ITournamentService;
import zur.koeln.kickertool.ui.rest.adapter.tools.CustomModelMapper;
import zur.koeln.kickertool.ui.rest.model.PlayerDTO;

@Getter
@Component
public class PlayerRestAdapter {
	private final IPlayerManagementService playerService;

	private final ITournamentService tournamentService;

	private final CustomModelMapper mapper;

	@Inject
	public PlayerRestAdapter(IPlayerManagementService playerService, ITournamentService tournamentService,
			CustomModelMapper mapper) {
		this.playerService = playerService;
		this.tournamentService = tournamentService;
		this.mapper = mapper;

	}

	public List<PlayerDTO> getAllPlayer() {
		List<Player> allPlayer = playerService.getAllPlayer();
		List<PlayerDTO> allPlayerDTO = new ArrayList<>();
		allPlayer.forEach(player -> allPlayerDTO.add(mapper.map(player, PlayerDTO.class)));
		return allPlayerDTO;
	}

	public List<PlayerDTO> getAllPlayerNotInTournament(UUID tournamendId) {
		List<Player> allPlayer = playerService.getAllPlayer();
		List<Player> tournamentParticipants = tournamentService.getTournamentParticipants(tournamendId);
		allPlayer.removeAll(tournamentParticipants);

		List<PlayerDTO> allPlayerDTO = new ArrayList<>();
		allPlayer.forEach(player -> allPlayerDTO.add(mapper.map(player, PlayerDTO.class)));
		return allPlayerDTO;
	}

	public void createNewPlayer(String firstName, String lastName) {
		playerService.createNewPlayer(firstName, lastName);
	}

	public void deletePlayer(UUID uuid) {

		playerService.deletePlayer(uuid);

	}

}
