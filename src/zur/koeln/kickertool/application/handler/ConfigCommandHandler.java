package zur.koeln.kickertool.application.handler;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.application.handler.api.ITournamentConfigCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusDTO;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;

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
