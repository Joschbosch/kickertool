package zur.koeln.kickertool.ui.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;

@RestController
public class PlayerRestController {

	@Autowired
	private IPlayerCommandHandler commandHandler;
	
	@RequestMapping("/getAll")
	public List<PlayerDTO> getAllPlayer() {
		
		ListResponseDTO<PlayerDTO> allPlayer = commandHandler.getAllPlayer();
		return allPlayer.getDtoValueList();
	}
	
}
