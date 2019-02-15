package zur.koeln.kickertool.application.handler;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import zur.koeln.kickertool.application.api.dtos.SettingsDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.StatusDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentSettingsCommandHandler;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;

public class SettingsCommandHandler
    implements ITournamentSettingsCommandHandler {

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private CustomModelMapper mapper;

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
