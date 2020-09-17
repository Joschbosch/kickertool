package zur.koeln.kickertool.ui.rest.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import lombok.Getter;
import zur.koeln.kickertool.core.bl.model.tournament.TournamentMode;
import zur.koeln.kickertool.core.ports.ui.ITournamentService;
import zur.koeln.kickertool.ui.rest.adapter.tools.CustomModelMapper;
import zur.koeln.kickertool.ui.rest.model.SettingsDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentModeDTO;

@Getter
@Component
public class TournamentConfigurationRestAdapter {

	private final ITournamentService tournamentService;

	private final CustomModelMapper mapper;

	@Inject
	public TournamentConfigurationRestAdapter(ITournamentService tournamentService, CustomModelMapper mapper) {
		this.tournamentService = tournamentService;
		this.mapper = mapper;
	}

	public SettingsDTO getDefaultSettings() {
		return mapper.map(tournamentService.getDefaultSettings(), SettingsDTO.class);
	}

	public List<TournamentModeDTO> getTournamentModes() {
		List<TournamentModeDTO> modeDTO = new ArrayList<>();
		for (TournamentMode mode : TournamentMode.values()) {
			modeDTO.add(new TournamentModeDTO(mode, mode.getDisplayName()));
		}
		return modeDTO;

	}
}
