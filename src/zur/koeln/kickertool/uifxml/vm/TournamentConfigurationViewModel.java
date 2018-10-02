package zur.koeln.kickertool.uifxml.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.config.TournamentSettingsKeys;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.uifxml.vm.wrapper.ModelWrapper;
import zur.koeln.kickertool.uifxml.vm.wrapper.TournamentConfigFields;

@Component
public class TournamentConfigurationViewModel {

	@Autowired
	private BackendController backendController;
	@Autowired
	private ModelWrapper<TournamentConfigFields> tournamentConfigMapper;
	
	private TournamentSettings tournamentSettings;
	
	private final StringProperty lblTournamentNameProperty = new SimpleStringProperty();
	
	public void loadTournamentSettings() {
		setTournamentSettings(getBackendController().getCurrentTournament().getSettings());
		getLblTournamentNameProperty().set(getBackendController().getCurrentTournament().getName());
		setPromptTexts();
	}
	
	private void setPromptTexts() {
		getStringProperty(TournamentConfigFields.GOALS_TO_WIN_DEFAULT).set(String.valueOf(getTournamentSettings().getGoalsToWin()));
		getStringProperty(TournamentConfigFields.MATCHES_TO_WIN_DEFAULT).set(String.valueOf(getTournamentSettings().getMatchesToWin()));
		getStringProperty(TournamentConfigFields.MINUTES_FOR_MATCH_DEFAULT).set(String.valueOf(getTournamentSettings().getMinutesPerMatch()));
		getStringProperty(TournamentConfigFields.NUMBER_OF_TABLES_DEFAULT).set(String.valueOf(getTournamentSettings().getTableCount()));
		getStringProperty(TournamentConfigFields.POINTS_FOR_DRAW_DEFAULT).set(String.valueOf(getTournamentSettings().getPointsForDraw()));
		getStringProperty(TournamentConfigFields.POINTS_TO_WIN_DEFAULT).set(String.valueOf(getTournamentSettings().getPointsForWinner()));
		getStringProperty(TournamentConfigFields.RANDOM_ROUNDS_START_DEFAULT).set(String.valueOf(getTournamentSettings().getRandomRounds()));
	}
	
	public void updateTournamentSettings() {
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.TABLES, getValue(TournamentConfigFields.NUMBER_OF_TABLES, TournamentConfigFields.NUMBER_OF_TABLES_DEFAULT));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.GOALS_FOR_WIN, getValue(TournamentConfigFields.GOALS_TO_WIN, TournamentConfigFields.GOALS_TO_WIN_DEFAULT));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.MATCHES_TO_WIN, getValue(TournamentConfigFields.MATCHES_TO_WIN, TournamentConfigFields.MATCHES_TO_WIN_DEFAULT));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.MINUTES_PER_MATCH, getValue(TournamentConfigFields.MINUTES_FOR_MATCH, TournamentConfigFields.MINUTES_FOR_MATCH_DEFAULT));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.POINTS_FOR_DRAW, getValue(TournamentConfigFields.POINTS_FOR_DRAW, TournamentConfigFields.POINTS_FOR_DRAW_DEFAULT));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.POINTS_FOR_WINNER, getValue(TournamentConfigFields.POINTS_TO_WIN, TournamentConfigFields.POINTS_TO_WIN_DEFAULT));
		getBackendController().changedTournamentConfig(TournamentSettingsKeys.RANDOM_ROUNDS, getValue(TournamentConfigFields.RANDOM_ROUNDS_START, TournamentConfigFields.RANDOM_ROUNDS_START_DEFAULT));
	}

	private Integer getValue(TournamentConfigFields propertyField, TournamentConfigFields defaultField) {
		StringProperty txtProperty = getStringProperty(propertyField); 
		StringProperty txtPromptProperty = getStringProperty(defaultField);
		
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
	private ModelWrapper<TournamentConfigFields> getTournamentConfigMapper() {
		return tournamentConfigMapper;
	}
	
	public StringProperty getStringProperty(TournamentConfigFields field) {
		return getTournamentConfigMapper().getStringProperty(field);
	}
}
