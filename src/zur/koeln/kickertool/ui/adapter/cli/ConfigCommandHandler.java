package zur.koeln.kickertool.ui.adapter.cli;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.application.api.ITournamentService;
import zur.koeln.kickertool.ui.adapter.cli.api.ITournamentConfigCommandHandler;
import zur.koeln.kickertool.ui.adapter.common.SettingsDTO;
import zur.koeln.kickertool.ui.adapter.common.base.SingleResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.StatusDTO;
import zur.koeln.kickertool.ui.configuration.CustomModelMapper;

@Named
public class ConfigCommandHandler
    implements ITournamentConfigCommandHandler {

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    @Inject
    public ConfigCommandHandler(
        ITournamentService tournamentService,
        CustomModelMapper mapper) {
        this.tournamentService = tournamentService;
        this.mapper = mapper;
    }

    @Override
    public SingleResponseDTO<SettingsDTO> getDefaultSettings() {
        SingleResponseDTO<SettingsDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(tournamentService.getDefaultSettings(), SettingsDTO.class));
        return response;
    }

    @Override
    public SingleResponseDTO<SettingsDTO> getTournamentModes() {
        SingleResponseDTO<SettingsDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(tournamentService.getDefaultSettings(), SettingsDTO.class));
        return response;
    }

}
