package zur.koeln.kickertool.uifxml;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import zur.koeln.kickertool.tournament.content.Match;
import javafx.scene.control.Button;

public class FXMLMatchEntryController {
	@FXML
	private Label lblTeamHome;
	@FXML
	private Label lblScore;
	@FXML
	private Label lblTeamVisit;
	@FXML
	private Label lblTable;
	@FXML 
	Button btnFinish;
	
	private Match match;
	
	public void setMatch(Match currentMatch) {
		match = currentMatch;
		init();
	}

	private void init() {
		lblTeamHome.setText(match.getHomeTeam().getPlayer1().getName() + " / " + match.getHomeTeam().getPlayer2().getName());
		lblTeamVisit.setText(match.getVisitingTeam().getPlayer1().getName() + " / " + match.getVisitingTeam().getPlayer2().getName());
		lblScore.setText("0:0");
	}

	@FXML public void onBtnFinishClicked() {
		match.setResult(4, 2);
		lblScore.setText("4:2");
	}
}
