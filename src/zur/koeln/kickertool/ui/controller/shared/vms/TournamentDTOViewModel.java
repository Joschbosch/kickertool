package zur.koeln.kickertool.ui.controller.shared.vms;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private List<MatchDTOViewModel> matches;

    private List<GameTableDTOViewModel> playtables;

    private final ObservableList<PlayerRankingRowViewModel> playerRankings = FXCollections.observableArrayList();

    private int currentRoundIndex;

    public List<MatchDTOViewModel> getMatchesForCurrentRound() {
    	return getMatches().stream().filter(match -> match.getRoundNumber() == getCurrentRoundIndex()).collect(Collectors.toList());
    }

	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

}
