package zur.koeln.kickertool.application.handler.api;

import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;

public interface ITournamentConfigCommandHandler {

    SingleResponseDTO<SettingsDTO> getDefaultSettings();

    SingleResponseDTO<SettingsDTO> getTournamentModes();

}
