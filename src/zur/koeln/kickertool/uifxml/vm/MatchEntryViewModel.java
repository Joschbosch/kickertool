package zur.koeln.kickertool.uifxml.vm;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.uifxml.dialog.ScoreDialog;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SuppressWarnings("nls")
public class MatchEntryViewModel {

	@Autowired
	private BackendController backendController;
	
	private Match currentMatch;
	
	private final StringProperty player1TeamHomeNameProperty = new SimpleStringProperty();
	private final StringProperty player2TeamHomeNameProperty = new SimpleStringProperty();
	private final StringProperty player1TeamVisitNameProperty = new SimpleStringProperty();
	private final StringProperty player2TeamVisitNameProperty = new SimpleStringProperty();
	private final StringProperty tableNameProperty = new SimpleStringProperty();
	private final StringProperty scoreProperty = new SimpleStringProperty();
	
	private final BooleanProperty btnFinishMatchDisableProperty = new SimpleBooleanProperty();
	private final BooleanProperty btnFinishMatchVisibleProperty = new SimpleBooleanProperty();
	
	public void init(Match match) {
		currentMatch = match;
		
		getPlayer1TeamHomeNameProperty().set(getCurrentMatch().getHomeTeam().getPlayer1().getName());
		getPlayer2TeamHomeNameProperty().set(getCurrentMatch().getHomeTeam().getPlayer2().getName());
		getPlayer1TeamVisitNameProperty().set(getCurrentMatch().getVisitingTeam().getPlayer1().getName());
		getPlayer2TeamVisitNameProperty().set(getCurrentMatch().getVisitingTeam().getPlayer2().getName());
		
		updateTableAndScoreTexts();
		updateDisableState();
	}
	
	private void updateTableAndScoreTexts() {
		getTableNameProperty().set(getTableNoString());
		getScoreProperty().set(getMatchResultString());
	}
	
	private String getTableNoString() {
		return getCurrentMatch().getTableNo() == -1 ? "TBA" : String.valueOf(getCurrentMatch().getTableNo());
	}
	
	private String getMatchResultString() {
		return getCurrentMatch().getResult() == null ? "-:-" : getCurrentMatch().getScoreHome() + ":" + getCurrentMatch().getScoreVisiting(); 
	}
	
	private void updateDisableState() {
		getBtnFinishMatchDisableProperty().set(getCurrentMatch().getTableNo() == -1 || getCurrentMatch().getRoundNumber().intValue() != getBackendController().getCurrentTournament().getCurrentRound().getRoundNo());
	}
	
	private Match getCurrentMatch() {
		return currentMatch;
	}
	
	private BackendController getBackendController() {
		return backendController;
	}

	public StringProperty getPlayer1TeamHomeNameProperty() {
		return player1TeamHomeNameProperty;
	}

	public StringProperty getPlayer2TeamHomeNameProperty() {
		return player2TeamHomeNameProperty;
	}

	public StringProperty getPlayer1TeamVisitNameProperty() {
		return player1TeamVisitNameProperty;
	}

	public StringProperty getPlayer2TeamVisitNameProperty() {
		return player2TeamVisitNameProperty;
	}

	public StringProperty getTableNameProperty() {
		return tableNameProperty;
	}

	public StringProperty getScoreProperty() {
		return scoreProperty;
	}

	public BooleanProperty getBtnFinishMatchDisableProperty() {
		return btnFinishMatchDisableProperty;
	}

	public BooleanProperty getBtnFinishMatchVisibleProperty() {
		return btnFinishMatchVisibleProperty;
	}

	public void openScoreEntryDialog() {
		ScoreDialog<Pair<Integer, Integer>> dialog = new ScoreDialog(getCurrentMatch().getHomeTeam(), getCurrentMatch().getVisitingTeam(), getBackendController().getCurrentTournament().getSettings().getGoalsToWin());
		Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        if (result.isPresent()) {
            getBackendController().updateMatchResult(getCurrentMatch(), result.get().getKey(), result.get().getValue());
    		updateTableAndScoreTexts();
    		updateDisableState();
        }
	}

}
