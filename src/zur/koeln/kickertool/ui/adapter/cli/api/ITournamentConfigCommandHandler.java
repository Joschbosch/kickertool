package zur.koeln.kickertool.ui.adapter.cli.api;

import zur.koeln.kickertool.ui.adapter.common.SettingsDTO;
import zur.koeln.kickertool.ui.adapter.common.base.SingleResponseDTO;

public interface ITournamentConfigCommandHandler {

    SingleResponseDTO<SettingsDTO> getDefaultSettings();

    SingleResponseDTO<SettingsDTO> getTournamentModes();

}
