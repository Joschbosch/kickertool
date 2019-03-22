package zur.koeln.kickertool.ui.controller.dialogs.vms;

import java.util.UUID;

import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentMode;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;

@Component
@Getter
@Setter
public class TournamentSettingsViewModel extends FXViewModel{

	private UUID uid;
	
	private final StringProperty tableCountProperty = new SimpleStringProperty();
	private final StringProperty randomRoundsProperty = new SimpleStringProperty();
	private final StringProperty matchesToWinProperty = new SimpleStringProperty();
	private final StringProperty goalsToWinProperty = new SimpleStringProperty();
	private final StringProperty minutesPerMatchProperty = new SimpleStringProperty();
	private final StringProperty pointsForWinnerProperty = new SimpleStringProperty();
	private final StringProperty pointsForDrawProperty = new SimpleStringProperty();
	
	private final BooleanProperty fixedTeamsProperty = new SimpleBooleanProperty();
	
	private final ObjectProperty<TournamentMode> modeProperty = new SimpleObjectProperty<>();
	
	@Override
	public ModelValidationResult validate() {
		return ModelValidationResult.empty();
	}

	public void setTableCount(int value) {
		getTableCountProperty().set(Integer.toString(value));
	}
	
	public void setRandomRounds(int value) {
		getRandomRoundsProperty().set(Integer.toString(value));
	}
	
	public void setMatchesToWin(int value) {
		getMatchesToWinProperty().set(Integer.toString(value));
	}
	
	public void setGoalsToWin(int value) {
		getGoalsToWinProperty().set(Integer.toString(value));
	}
	
	public void setMinutesPerMatch(int value) {
		getMinutesPerMatchProperty().set(Integer.toString(value));
	}
	
	public void setPointsForWinner(int value) {
		getPointsForWinnerProperty().set(Integer.toString(value));
	}
	
	public void setPointsForDraw(int value) {
		getPointsForDrawProperty().set(Integer.toString(value));
	}
	
	public void setFixedTeams(boolean fixedTeams) {
		getFixedTeamsProperty().set(fixedTeams);
	}
	
	public void setMode(TournamentMode mode) {
		getModeProperty().set(mode);
	}
	
	public int getTableCount() {
		return Integer.valueOf(getTableCountProperty().get()).intValue();
	}
	
	public int getRandomRounds() {
		return Integer.valueOf(getRandomRoundsProperty().get()).intValue();
	}
	
	public int getMatchesToWin() {
		return Integer.valueOf(getMatchesToWinProperty().get()).intValue();
	}
	
	public int getGoalsToWin() {
		return Integer.valueOf(getGoalsToWinProperty().get()).intValue();
	}
	
	public int getMinutesPerMatch() {
		return Integer.valueOf(getMinutesPerMatchProperty().get()).intValue();
	}
	
	public int getPointsForWinner() {
		return Integer.valueOf(getPointsForWinnerProperty().get()).intValue();
	}
	
	public int getPointsForDraw() {
		return Integer.valueOf(getPointsForDrawProperty().get()).intValue();
	}
	
	public boolean getFixedTeams() {
		return getFixedTeamsProperty().get();
	}
	
	public TournamentMode getMode() {
		return getModeProperty().get();
	}
	
}
