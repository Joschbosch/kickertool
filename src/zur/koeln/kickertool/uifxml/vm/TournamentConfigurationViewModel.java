package zur.koeln.kickertool.uifxml.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.config.TournamentSettingsKeys;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.uifxml.vm.wrapper.ModelWrapper;

@Component
public class TournamentConfigurationViewModel {

	@Autowired
	private BackendController backendController;
	@Autowired
	private ModelWrapper<TournamentSettingsKeys> tournamentConfigMapper;
	
	private TournamentSettings tournamentSettings;
	
	private final StringProperty lblTournamentNameProperty = new SimpleStringProperty();
	
	public void loadTournamentSettings() {
		setTournamentSettings(getBackendController().getCurrentTournament().getSettings());
		getLblTournamentNameProperty().set(getBackendController().getCurrentTournament().getName());
		setPromptTexts();
	}
	
	private void setPromptTexts() {
		getStringPromptProperty(TournamentSettingsKeys.GOALS_FOR_WIN).set(String.valueOf(getTournamentSettings().getGoalsToWin()));
		getStringPromptProperty(TournamentSettingsKeys.MATCHES_TO_WIN).set(String.valueOf(getTournamentSettings().getMatchesToWin()));
		getStringPromptProperty(TournamentSettingsKeys.MINUTES_PER_MATCH).set(String.valueOf(getTournamentSettings().getMinutesPerMatch()));
		getStringPromptProperty(TournamentSettingsKeys.TABLES).set(String.valueOf(getTournamentSettings().getTableCount()));
		getStringPromptProperty(TournamentSettingsKeys.POINTS_FOR_DRAW).set(String.valueOf(getTournamentSettings().getPointsForDraw()));
		getStringPromptProperty(TournamentSettingsKeys.POINTS_FOR_WINNER).set(String.valueOf(getTournamentSettings().getPointsForWinner()));
		getStringPromptProperty(TournamentSettingsKeys.RANDOM_ROUNDS).set(String.valueOf(getTournamentSettings().getRandomRounds()));
	}
	
	public void updateTournamentSettings() {
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.TABLES, getValue(TournamentSettingsKeys.TABLES));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.GOALS_FOR_WIN, getValue(TournamentSettingsKeys.GOALS_FOR_WIN));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.MATCHES_TO_WIN, getValue(TournamentSettingsKeys.MATCHES_TO_WIN));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.MINUTES_PER_MATCH, getValue(TournamentSettingsKeys.MINUTES_PER_MATCH));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.POINTS_FOR_DRAW, getValue(TournamentSettingsKeys.POINTS_FOR_DRAW));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.POINTS_FOR_WINNER, getValue(TournamentSettingsKeys.POINTS_FOR_WINNER));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.RANDOM_ROUNDS, getValue(TournamentSettingsKeys.RANDOM_ROUNDS));
	}

	private Integer getValue(TournamentSettingsKeys setting) {
		StringProperty txtProperty = getStringProperty(setting); 
		StringProperty txtPromptProperty = getStringPromptProperty(setting);
		
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
	private ModelWrapper<TournamentSettingsKeys> getTournamentConfigMapper() {
		return tournamentConfigMapper;
	}
	
	public StringProperty getStringProperty(TournamentSettingsKeys field) {
		return getTournamentConfigMapper().getStringProperty(field);
	}
	
	public StringProperty getStringPromptProperty(TournamentSettingsKeys field) {
		return getTournamentConfigMapper().getStringPromptProperty(field);
	}
}
