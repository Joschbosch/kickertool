package zur.koeln.kickertool.uifxml;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.util.Pair;
import zur.koeln.kickertool.api.tournament.Team;

public class FXMLMatchResultDialogController {
	
	@FXML
	private Label lblTeamHome;
	@FXML
	private Label lblTeamVisitor;
	@FXML
	private ChoiceBox<Integer> choiceGoalsTeamHome;
	@FXML
	private ChoiceBox<Integer> choiceGoalsTeamVisit;
	
    public void init(Team teamHome, Team teamVisit, int goalsToWin) {
		lblTeamHome.setText(teamHome.getPlayer1().getName() + "/" + teamHome.getPlayer2().getName());
		lblTeamVisitor.setText(teamVisit.getPlayer1().getName() + "/" + teamVisit.getPlayer2().getName());
        choiceGoalsTeamHome.setItems(FXCollections.observableList(getGoalValues(goalsToWin)));
        choiceGoalsTeamVisit.setItems(FXCollections.observableList(getGoalValues(goalsToWin)));
        choiceGoalsTeamHome.setValue(Integer.valueOf(0));
        choiceGoalsTeamVisit.setValue(Integer.valueOf(0));
	}
	
    private List<Integer> getGoalValues(int goalsToWin) {
        return IntStream.range(0, goalsToWin + 1).boxed().collect(Collectors.toList());
	}

	public Pair<Integer, Integer> getEnteredResult() {

		return new Pair<Integer, Integer>(choiceGoalsTeamHome.getValue(), choiceGoalsTeamVisit.getValue());
	}

}
