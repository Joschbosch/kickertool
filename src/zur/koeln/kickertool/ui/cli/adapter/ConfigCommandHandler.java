package zur.koeln.kickertool.ui.cli.adapter;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.ports.ui.ITournamentService;
import zur.koeln.kickertool.ui.common.SingleResponseDTO;
import zur.koeln.kickertool.ui.common.StatusDTO;
import zur.koeln.kickertool.ui.rest.adapter.tools.CustomModelMapper;
import zur.koeln.kickertool.ui.rest.model.SettingsDTO;

@Named
public class ConfigCommandHandler {

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    @Inject
    public ConfigCommandHandler(
        ITournamentService tournamentService,
        CustomModelMapper mapper) {
        this.tournamentService = tournamentService;
        this.mapper = mapper;
    }

    public SingleResponseDTO<SettingsDTO> getDefaultSettings() {
        SingleResponseDTO<SettingsDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(tournamentService.getDefaultSettings(), SettingsDTO.class));
        return response;
    }

    public SingleResponseDTO<SettingsDTO> getTournamentModes() {
        SingleResponseDTO<SettingsDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(tournamentService.getDefaultSettings(), SettingsDTO.class));
        return response;
    }

}
