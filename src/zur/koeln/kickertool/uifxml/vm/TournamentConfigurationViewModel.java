package zur.koeln.kickertool.uifxml.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.config.TournamentSettingsKeys;
import zur.koeln.kickertool.api.tournament.TournamentSettings;

@Component
public class TournamentConfigurationViewModel {

	@Autowired
	private BackendController backendController;
	
	private TournamentSettings tournamentSettings;
	
	private final StringProperty lblTournamentNameProperty = new SimpleStringProperty();
	
	private final StringProperty txtNumberOfTablesProperty = new SimpleStringProperty();
	private final StringProperty txtMatchesToWinProperty = new SimpleStringProperty();
	private final StringProperty txtGoalsToWinProperty = new SimpleStringProperty();
	private final StringProperty txtPointsToWinProperty = new SimpleStringProperty();
	private final StringProperty txtPointsForDrawProperty = new SimpleStringProperty();
	private final StringProperty txtMinutesPerMatchProperty = new SimpleStringProperty();
	private final StringProperty txtRandomRoundsAtStartProperty = new SimpleStringProperty();
	
	private final StringProperty txtNumberOfTablesPromptProperty = new SimpleStringProperty();
	private final StringProperty txtMatchesToWinPromptProperty = new SimpleStringProperty();
	private final StringProperty txtGoalsToWinPromptProperty = new SimpleStringProperty();
	private final StringProperty txtPointsToWinPromptProperty = new SimpleStringProperty();
	private final StringProperty txtPointsForDrawPromptProperty = new SimpleStringProperty();
	private final StringProperty txtMinutesPerMatchPromptProperty = new SimpleStringProperty();
	private final StringProperty txtRandomRoundsAtStartPromptProperty = new SimpleStringProperty();
	
	public void loadTournamentSettings() {
		setTournamentSettings(getBackendController().getCurrentTournament().getSettings());
		getLblTournamentNameProperty().set(getBackendController().getCurrentTournament().getName());
		setPromptTexts();
	}
	
	private void setPromptTexts() {
		getTxtGoalsToWinPromptProperty().set(String.valueOf(getTournamentSettings().getGoalsToWin()));
		getTxtMatchesToWinPromptProperty().set(String.valueOf(getTournamentSettings().getMatchesToWin()));
		getTxtMinutesPerMatchPromptProperty().set(String.valueOf(getTournamentSettings().getMinutesPerMatch()));
		getTxtNumberOfTablesPromptProperty().set(String.valueOf(getTournamentSettings().getTableCount()));
		getTxtPointsForDrawPromptProperty().set(String.valueOf(getTournamentSettings().getPointsForDraw()));
		getTxtPointsToWinPromptProperty().set(String.valueOf(getTournamentSettings().getPointsForWinner()));
		getTxtRandomRoundsAtStartPromptProperty().set(String.valueOf(getTournamentSettings().getRandomRounds()));
	}
	
	public void updateTournamentSettings() {
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.TABLES, getValue(getTxtNumberOfTablesProperty(), getTxtNumberOfTablesPromptProperty()));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.GOALS_FOR_WIN, getValue(getTxtGoalsToWinProperty(), getTxtGoalsToWinPromptProperty()));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.MATCHES_TO_WIN, getValue(getTxtMatchesToWinProperty(), getTxtMatchesToWinPromptProperty()));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.MINUTES_PER_MATCH, getValue(getTxtMinutesPerMatchProperty(), getTxtMinutesPerMatchPromptProperty()));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.POINTS_FOR_DRAW, getValue(getTxtPointsForDrawProperty(), getTxtPointsForDrawPromptProperty()));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.POINTS_FOR_WINNER, getValue(getTxtPointsToWinProperty(), getTxtPointsToWinPromptProperty()));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.RANDOM_ROUNDS, getValue(getTxtRandomRoundsAtStartProperty(), getTxtRandomRoundsAtStartPromptProperty()));
	}

	private Integer getValue(StringProperty txtProperty, StringProperty txtPromptProperty) {
		
		if (!txtProperty.isEmpty().get()) {
			return Integer.valueOf(txtProperty.get());
		}
		
		return Integer.valueOf(txtPromptProperty.get());
		
	}
	
	private BackendController getBackendController() {
		return backendController;
	}
	private TournamentSettings getTournamentSettings() {
		return tournamentSettings;
	}
	private void setTournamentSettings(TournamentSettings tournamentSettings) {
		this.tournamentSettings = tournamentSettings;
	}
	public StringProperty getLblTournamentNameProperty() {
		return lblTournamentNameProperty;
	}
	public StringProperty getTxtNumberOfTablesProperty() {
		return txtNumberOfTablesProperty;
	}
	public StringProperty getTxtMatchesToWinProperty() {
		return txtMatchesToWinProperty;
	}
	public StringProperty getTxtGoalsToWinProperty() {
		return txtGoalsToWinProperty;
	}
	public StringProperty getTxtPointsToWinProperty() {
		return txtPointsToWinProperty;
	}
	public StringProperty getTxtPointsForDrawProperty() {
		return txtPointsForDrawProperty;
	}
	public StringProperty getTxtMinutesPerMatchProperty() {
		return txtMinutesPerMatchProperty;
	}
	public StringProperty getTxtRandomRoundsAtStartProperty() {
		return txtRandomRoundsAtStartProperty;
	}
	public StringProperty getTxtNumberOfTablesPromptProperty() {
		return txtNumberOfTablesPromptProperty;
	}
	public StringProperty getTxtMatchesToWinPromptProperty() {
		return txtMatchesToWinPromptProperty;
	}
	public StringProperty getTxtGoalsToWinPromptProperty() {
		return txtGoalsToWinPromptProperty;
	}
	public StringProperty getTxtPointsToWinPromptProperty() {
		return txtPointsToWinPromptProperty;
	}
	public StringProperty getTxtPointsForDrawPromptProperty() {
		return txtPointsForDrawPromptProperty;
	}
	public StringProperty getTxtMinutesPerMatchPromptProperty() {
		return txtMinutesPerMatchPromptProperty;
	}
	public StringProperty getTxtRandomRoundsAtStartPromptProperty() {
		return txtRandomRoundsAtStartPromptProperty;
	}
	
}
