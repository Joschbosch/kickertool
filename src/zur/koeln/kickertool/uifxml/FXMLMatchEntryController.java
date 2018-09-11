package zur.koeln.kickertool.uifxml;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.util.Pair;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.tournament.content.Match;
import zur.koeln.kickertool.uifxml.dialog.ScoreDialog;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;

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
	
	@Autowired
    private BackendController backendController;
	
	private Match match;
	
	private String teamHomeName;
	private String teamVisitName;
	
	private BooleanProperty matchFinished = new SimpleBooleanProperty(false);
	
	public void setMatch(Match currentMatch) {
		match = currentMatch;
		init();
	}

	private void init() {
		teamHomeName = match.getHomeTeam().getP1Obj().getName() + " / " + match.getHomeTeam().getP2Obj().getName();
		teamVisitName = match.getVisitingTeam().getP1Obj().getName() + " / " + match.getVisitingTeam().getP2Obj().getName();
		
		lblTeamHome.setText(teamHomeName);
		lblTeamVisit.setText(teamVisitName);
		lblTable.setText(match.getTableNo() == -1 ? "TBA" : String.valueOf(match.getTableNo()));
		lblScore.setText("0:0");
		
		btnFinish.disableProperty().bind(matchFinished);
	}

	@FXML public void onBtnFinishClicked() {
		
		ScoreDialog<Pair<Integer, Integer>> dialog = new ScoreDialog<>(teamHomeName, teamVisitName);
		Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        if (result.isPresent()) {
            backendController.updateMatchResult(match, result.get().getKey(), result.get().getValue());
    		lblScore.setText(result.get().getKey() + ":" + result.get().getValue());
    		matchFinished.set(true);
        }
	}
	
	public void setBackendController(BackendController newBackendController) {
		backendController = newBackendController;
	}
}
