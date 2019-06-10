package zur.koeln.kickertool.ui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.ITournamentSettingsCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentConfigurationDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournamentconfig")
public class TournamentConfigurationRestController {

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	private ITournamentSettingsCommandHandler tournamentSettingsCommandHandler;

	@GetMapping("/defaultConfig")
	public SingleResponseDTO<TournamentConfigurationDTO> getDefaultConfig() {
		SingleResponseDTO<SettingsDTO> defaultSettings = getTournamentSettingsCommandHandler().getDefaultSettings();

		TournamentConfigurationDTO configurationDTO = new TournamentConfigurationDTO();
		configurationDTO.setSettings(defaultSettings.getDtoValue());

		SingleResponseDTO<TournamentConfigurationDTO> response = new SingleResponseDTO<>();
		response.setDtoValue(configurationDTO);
		return response;
	}

}
