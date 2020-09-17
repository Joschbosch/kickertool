package zur.koeln.kickertool.ui.rest;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zur.koeln.kickertool.ui.common.ListResponseDTO;
import zur.koeln.kickertool.ui.common.SingleResponseDTO;
import zur.koeln.kickertool.ui.common.StatusDTO;
import zur.koeln.kickertool.ui.rest.adapter.TournamentConfigurationRestAdapter;
import zur.koeln.kickertool.ui.rest.model.SettingsDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentConfigurationDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentModeDTO;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournamentconfig")
public class TournamentConfigurationRestController {

	private TournamentConfigurationRestAdapter restAdapter;

	@Inject
	public TournamentConfigurationRestController(TournamentConfigurationRestAdapter restAdapter) {
		this.restAdapter = restAdapter;

	}

	@GetMapping("/defaultConfig")
	public SingleResponseDTO<TournamentConfigurationDTO> getDefaultConfig() {
		SingleResponseDTO<SettingsDTO> defaultSettings = new SingleResponseDTO<>();
		defaultSettings.setDtoStatus(StatusDTO.SUCCESS);
		defaultSettings.setDtoValue(restAdapter.getDefaultSettings());

		TournamentConfigurationDTO configurationDTO = new TournamentConfigurationDTO();
		configurationDTO.setSettings(defaultSettings.getDtoValue());

		SingleResponseDTO<TournamentConfigurationDTO> response = new SingleResponseDTO<>();
		response.setDtoValue(configurationDTO);
		return response;
	}

	@GetMapping("/tournamentmodes")
	public ListResponseDTO<TournamentModeDTO> getTournamentModes() {
		ListResponseDTO<TournamentModeDTO> response = new ListResponseDTO<>();
		response.setDtoStatus(StatusDTO.SUCCESS);
		response.setDtoValueList(restAdapter.getTournamentModes());
		return response;
	}

}
