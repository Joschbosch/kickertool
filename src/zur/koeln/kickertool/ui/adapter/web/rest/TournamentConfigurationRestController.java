package zur.koeln.kickertool.ui.adapter.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zur.koeln.kickertool.core.application.api.ITournamentService;
import zur.koeln.kickertool.core.domain.model.entities.tournament.TournamentMode;
import zur.koeln.kickertool.ui.adapter.common.SettingsDTO;
import zur.koeln.kickertool.ui.adapter.common.TournamentConfigurationDTO;
import zur.koeln.kickertool.ui.adapter.common.TournamentModeDTO;
import zur.koeln.kickertool.ui.adapter.common.base.ListResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.SingleResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.StatusDTO;
import zur.koeln.kickertool.ui.configuration.CustomModelMapper;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournamentconfig")
public class TournamentConfigurationRestController {

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    @Inject
    public TournamentConfigurationRestController(
        ITournamentService tournamentService,
        CustomModelMapper mapper) {
        this.tournamentService = tournamentService;
        this.mapper = mapper;

    }

    @GetMapping("/defaultConfig")
    public SingleResponseDTO<TournamentConfigurationDTO> getDefaultConfig() {
        SingleResponseDTO<SettingsDTO> defaultSettings = new SingleResponseDTO<>();
        defaultSettings.setDtoStatus(StatusDTO.SUCCESS);
        defaultSettings.setDtoValue(mapper.map(tournamentService.getDefaultSettings(), SettingsDTO.class));

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
