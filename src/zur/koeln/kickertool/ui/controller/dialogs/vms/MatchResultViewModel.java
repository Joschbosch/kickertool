package zur.koeln.kickertool.ui.controller.dialogs.vms;

import lombok.Getter;

@Getter
public class MatchResultViewModel {

	private int scoreHomeTeam;
	private int scoreVisitingTeam;

	public MatchResultViewModel(int scoreHomeTeam, int scoreVisitingTeam) {
		super();
		this.scoreHomeTeam = scoreHomeTeam;
		this.scoreVisitingTeam = scoreVisitingTeam;
	}

}