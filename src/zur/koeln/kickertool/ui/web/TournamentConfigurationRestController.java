package zur.koeln.kickertool.ui.web;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.ITournamentConfigCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentConfigurationDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusDTO;
import zur.koeln.kickertool.core.kernl.TournamentMode;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournamentconfig")
public class TournamentConfigurationRestController {

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	private ITournamentConfigCommandHandler tournamentSettingsCommandHandler;



	@GetMapping("/defaultConfig")
	public SingleResponseDTO<TournamentConfigurationDTO> getDefaultConfig() {
		SingleResponseDTO<SettingsDTO> defaultSettings = getTournamentSettingsCommandHandler().getDefaultSettings();

		TournamentConfigurationDTO configurationDTO = new TournamentConfigurationDTO();
		configurationDTO.setSettings(defaultSettings.getDtoValue());

		SingleResponseDTO<TournamentConfigurationDTO> response = new SingleResponseDTO<>();
		response.setDtoValue(configurationDTO);
		return response;
	}

    @GetMapping("/tournamentmodes")
    public SingleResponseDTO<Map<String, String>> getTournamentModes() {
        SingleResponseDTO<Map<String, String>> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(Arrays.stream(TournamentMode.values()).collect(Collectors.toMap(TournamentMode::name, TournamentMode::getDisplayName)));
        return response;
    }

}
