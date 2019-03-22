package zur.koeln.kickertool.ui.controller.shared.vms;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.dialogs.vms.TournamentSettingsViewModel;

@Getter
@Setter
public class TournamentDTOViewModel extends FXViewModel{

    private UUID uid;

    private String name;

    private TournamentStatus status;

    private TournamentSettingsViewModel settings;

    private List<PlayerDTOViewModel> players;

    private List<PlayerDTOViewModel> dummyPlayers;

    private List<MatchDTOViewModel> matches;

    private List<GameTableDTOViewModel> playtables;

    private int currentRoundIndex;
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

}
