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
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.core.entities.Match;
import zur.koeln.kickertool.uifxml.dialog.ScoreDialog;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FXMLMatchEntryController implements UpdateableUIComponent{

	@FXML
	private Label lblScore;
	@FXML
	private Label lblTable;
	@FXML 
	Button btnFinish;
	
	@Autowired
    private BasicBackendController backendController;
	@Autowired
	private FXMLTournamentController tournamentController;
	
    private Match match;
	
	@FXML Label lblPlayer1TeamHome;
	@FXML Label lblPlayer1TeamVisit;
	@FXML Label lblPlayer2TeamHome;
	@FXML Label lblPlayer2TeamVisit;
	
    public void setMatch(Match currentMatch) {
		match = currentMatch;
		init();
	}

	private void init() {

		setPlayerTeamTexts();
		lblTable.setText(match.getTableNo() == -1 ? "TBA" : String.valueOf(match.getTableNo()));

		if (match.getResult() != null) {
			
			lblScore.setText(match.getScoreHome() + ":" + match.getScoreVisiting());
		} else {
            lblScore.setText("-:-");
		}
		
        btnFinish.setDisable(match.getTableNo() == -1 || match.getRoundNumber().intValue() != backendController.getCurrentTournament().getCurrentRound().getRoundNo());
	}
	
	public void hideBtnFinish() {
		btnFinish.setVisible(false);
	}
	
	private void setPlayerTeamTexts() { 
		lblPlayer1TeamHome.setText(match.getHomeTeam().getPlayer1().getName());
		lblPlayer2TeamHome.setText(match.getHomeTeam().getPlayer2().getName());
		
		lblPlayer1TeamVisit.setText(match.getVisitingTeam().getPlayer1().getName());
		lblPlayer2TeamVisit.setText(match.getVisitingTeam().getPlayer2().getName());
	}

	@FXML public void onBtnFinishClicked() {
		
        ScoreDialog<Pair<Integer, Integer>> dialog = new ScoreDialog(match.getHomeTeam(), match.getVisitingTeam(), backendController.getCurrentTournament().getSettings().getGoalsToWin());
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
