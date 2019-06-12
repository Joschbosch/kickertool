package zur.koeln.kickertool.ui.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusOnlyDTO;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/players")
public class PlayerRestController {

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	private IPlayerCommandHandler commandHandler;

	@GetMapping("/getall")
	public ListResponseDTO<PlayerDTO> getAllPlayer() {
		return getCommandHandler().getAllPlayer();
	}

	@PostMapping()
	public SingleResponseDTO<PlayerDTO> insertNewPlayer(@RequestBody PlayerDTO playerDTO) {
		return getCommandHandler().createNewPlayer(playerDTO.getFirstName(), playerDTO.getLastName());
	}

	@DeleteMapping("/{uuid}")
	public @ResponseBody StatusOnlyDTO deletePlayer(@PathVariable UUID uuid) {
		return getCommandHandler().deletePlayer(uuid);
	}
}
