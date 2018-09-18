package zur.koeln.kickertool.uifxml;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Pair;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.uifxml.dialog.ScoreDialog;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FXMLMatchEntryController implements UpdateableUIComponent{
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
	
	@Autowired
    private BackendController backendController;
	@Autowired
	private FXMLTournamentController tournamentController;
	
    private Match match;
	
	private String teamHomeName;
	private String teamVisitName;
	
    public void setMatch(Match currentMatch) {
		match = currentMatch;
		init();
	}

	private void init() {
		teamHomeName = match.getHomeTeam().getPlayer1().getName() + " / " + match.getHomeTeam().getPlayer2().getName();
		teamVisitName = match.getVisitingTeam().getPlayer1().getName() + " / " + match.getVisitingTeam().getPlayer2().getName();
		
		lblTeamHome.setText(teamHomeName);
		lblTeamVisit.setText(teamVisitName);
		lblTable.setText(match.getTableNo() == -1 ? "TBA" : String.valueOf(match.getTableNo()));

		if (match.getResult() != null) {
			lblScore.setText("?:?");
		} else {
			lblScore.setText("0:0");
		}
		
		btnFinish.setDisable(match.getTableNo() == -1);
	}

	@FXML public void onBtnFinishClicked() {
		
		ScoreDialog<Pair<Integer, Integer>> dialog = new ScoreDialog<>(teamHomeName, teamVisitName);
		Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        if (result.isPresent()) {
            backendController.updateMatchResult(match, result.get().getKey(), result.get().getValue());
    		lblScore.setText(result.get().getKey() + ":" + result.get().getValue());
    		tournamentController.update();
        }
	}

	@Override
	public void update() {
		lblTable.setText(match.getTableNo() == -1 ? "TBA" : String.valueOf(match.getTableNo()));
	}
}
