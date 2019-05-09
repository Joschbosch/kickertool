package zur.koeln.kickertool.application.handler;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.application.handler.api.ITournamentSettingsCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusDTO;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;

@Named
public class SettingsCommandHandler
    implements ITournamentSettingsCommandHandler {

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    @Inject
    public SettingsCommandHandler(
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
    public SingleResponseDTO<SettingsDTO> getSettings(UUID tournamentUid) {
        SingleResponseDTO<SettingsDTO> response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(tournamentService.getSettings(tournamentUid), SettingsDTO.class));
        return response;
    }

}
