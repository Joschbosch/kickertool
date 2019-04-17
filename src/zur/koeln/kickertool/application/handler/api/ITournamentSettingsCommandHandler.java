package zur.koeln.kickertool.application.handler.api;

import java.util.UUID;

import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;

public interface ITournamentSettingsCommandHandler {

    SingleResponseDTO<SettingsDTO> getDefaultSettings();

    SingleResponseDTO<SettingsDTO> getSettings(UUID tournamentUid);

}
