package zur.koeln.kickertool.uifxml;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.util.Pair;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.tournament.content.Match;
import javafx.scene.control.ChoiceBox;

public class FXMLMatchResultDialogController {
	
	@FXML
	private Label lblTeamHome;
	@FXML
	private Label lblTeamVisitor;
	@FXML
	private ChoiceBox<Integer> choiceGoalsTeamHome;
	@FXML
	private ChoiceBox<Integer> choiceGoalsTeamVisit;
	
	public void init(String teamHomeName, String teamVisitString) {
		lblTeamHome.setText(teamHomeName);
		lblTeamVisitor.setText(teamVisitString);
		choiceGoalsTeamHome.setItems(FXCollections.observableList(getGoalValues()));
		choiceGoalsTeamVisit.setItems(FXCollections.observableList(getGoalValues()));
	}
	
	private List<Integer> getGoalValues() {
		return Arrays.asList(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6) ,Integer.valueOf(7),Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10));
	}

	public Pair<Integer, Integer> getEnteredResult() {

		return new Pair<Integer, Integer>(choiceGoalsTeamHome.getValue(), choiceGoalsTeamVisit.getValue());
	}

}
