package zur.koeln.kickertool.ui.rest;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import zur.koeln.kickertool.ui.common.ListResponseDTO;
import zur.koeln.kickertool.ui.common.StatusDTO;
import zur.koeln.kickertool.ui.common.StatusOnlyDTO;
import zur.koeln.kickertool.ui.rest.adapter.PlayerRestAdapter;
import zur.koeln.kickertool.ui.rest.model.PlayerDTO;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/players")
public class PlayerRestController {

	private PlayerRestAdapter restAdapter;

	@Inject
	public PlayerRestController(PlayerRestAdapter restAdapter) {
		this.restAdapter = restAdapter;

	}

	@GetMapping("/getall")
	public ListResponseDTO<PlayerDTO> getAllPlayer() {

		List<PlayerDTO> allPlayerDTO = restAdapter.getAllPlayer();
		ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
		response.setDtoStatus(StatusDTO.SUCCESS);
		response.setValidation(null);
		response.setDtoValueList(allPlayerDTO);

		return response;
	}

	@GetMapping("/getall-not-in-tournament/{tournamendId}")
	public ListResponseDTO<PlayerDTO> getAllPlayerNotInTournament(@PathVariable UUID tournamendId) {

		List<PlayerDTO> allPlayerNotInTournament = restAdapter.getAllPlayerNotInTournament(tournamendId);

		ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
		response.setDtoStatus(StatusDTO.SUCCESS);
		response.setValidation(null);
		response.setDtoValueList(allPlayerNotInTournament);

		return response;
	}

	@PostMapping()
	public StatusOnlyDTO insertNewPlayer(@RequestBody PlayerDTO playerDTO) {
		restAdapter.createNewPlayer(playerDTO.getFirstName(), playerDTO.getLastName());

		StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
		statusOnlyDTO.setDtoStatus(StatusDTO.SUCCESS);
		return statusOnlyDTO;
	}

	@DeleteMapping("/{uuid}")
	public @ResponseBody StatusOnlyDTO deletePlayer(@PathVariable UUID uuid) {
		restAdapter.deletePlayer(uuid);
		StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
		statusOnlyDTO.setDtoStatus(StatusDTO.SUCCESS);
		return statusOnlyDTO;
	}
}
