package zur.koeln.kickertool.ui.controller.vms;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.ui.controller.vms.base.FXViewModel;
import zur.koeln.kickertool.ui.controller.vms.base.ModelValidationResult;

@Getter
@Setter
public class TournamentViewModel extends FXViewModel{

    private UUID uid;

    private String name;

    private TournamentStatus status;

    private TournamentSettingsViewModel settings;

    private List<PlayerViewModel> players;

    private List<PlayerViewModel> dummyPlayers;

    private List<MatchViewModel> matches;

    private List<GameTableViewModel> playtables;

    private int currentRoundIndex;
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

}
