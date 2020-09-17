package zur.koeln.kickertool.ui.rest.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.ui.common.DTO;

@NoArgsConstructor
@Getter
@Setter
public class TournamentConfigurationDTO extends DTO{

	private String name;

	private SettingsDTO settings;

	private List<PlayerDTO> selectedPlayer;

}
