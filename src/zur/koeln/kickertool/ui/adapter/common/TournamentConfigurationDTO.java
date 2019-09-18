package zur.koeln.kickertool.ui.adapter.common;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.ui.adapter.common.base.DTO;

@NoArgsConstructor
@Getter
@Setter
public class TournamentConfigurationDTO extends DTO{

	private String name;

	private SettingsDTO settings;

	private List<PlayerDTO> selectedPlayer;

}
