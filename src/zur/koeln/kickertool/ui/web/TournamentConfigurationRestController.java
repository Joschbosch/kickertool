package zur.koeln.kickertool.ui.web;

import java.util.ArrayList;
import java.util.List;

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
import zur.koeln.kickertool.application.handler.dtos.TournamentModeDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
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
    public ListResponseDTO<TournamentModeDTO> getTournamentModes() {
        ListResponseDTO<TournamentModeDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        List<TournamentModeDTO> modeDTO = new ArrayList<>();
        for (TournamentMode mode : TournamentMode.values()) {
            modeDTO.add(new TournamentModeDTO(mode, mode.getDisplayName()));
        }
        response.setDtoValueList(modeDTO);
        return response;
    }

}
