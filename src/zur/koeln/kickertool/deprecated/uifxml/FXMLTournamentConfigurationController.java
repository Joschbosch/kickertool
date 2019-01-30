package zur.koeln.kickertool.deprecated.uifxml;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.core.entities.Settings;
import zur.koeln.kickertool.core.entities.Tournament;
import zur.koeln.kickertool.core.kernl.TournamentSetingsKeys;

@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentConfigurationController implements UpdateableUIComponent {

    @Autowired
    private BasicBackendController backendController;
    
	@FXML
	private Label lblTournament;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
	@FXML
	private TextField txtNumberOfTables;
    //	@FXML
    //	private TextField txtMatchesToWin;
	@FXML
	private TextField txtGoalsToWin;
	@FXML
	private TextField txtPointsToWin;
	@FXML
	private TextField txtPointsForDraw;
	@FXML
	private TextField txtMinutesPerMatch;
	@FXML
	private TextField txtRandomRoundsAtStart;
	
    private Settings config;
	
	@FXML 
	public void initialize() {
		
        Tournament currentTournament = backendController.getCurrentTournament();
		config = currentTournament.getSettings();
		
		
        getLblTournament().setText(backendController.getCurrentTournament().getName());
		
		getTxtGoalsToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtNumberOfTables().setTextFormatter(createIntegerTextFormatter());
        //		getTxtMatchesToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtPointsToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtPointsForDraw().setTextFormatter(createIntegerTextFormatter());
		getTxtMinutesPerMatch().setTextFormatter(createIntegerTextFormatter());
		getTxtRandomRoundsAtStart().setTextFormatter(createIntegerTextFormatter());
		
		
		setDefaultValues();
	}
	
	private TextFormatter<Integer> createIntegerTextFormatter() {
		return new TextFormatter<>(new IntegerStringConverter());
	}
	
	private void setDefaultValues() {
		
		getTxtGoalsToWin().setPromptText(String.valueOf(getConfig().getGoalsToWin()));
		getTxtNumberOfTables().setPromptText(String.valueOf(getConfig().getTableCount()));
        //		getTxtMatchesToWin().setPromptText(String.valueOf(getConfig().getMatchesToWin()));
		getTxtPointsToWin().setPromptText(String.valueOf(getConfig().getPointsForWinner()));
		getTxtPointsForDraw().setPromptText(String.valueOf(getConfig().getPointsForDraw()));
		getTxtMinutesPerMatch().setPromptText(String.valueOf(getConfig().getMinutesPerMatch()));
		getTxtRandomRoundsAtStart().setPromptText(String.valueOf(getConfig().getRandomRounds()));
		
	}
	
	@FXML 
	public void onBtnBackClicked() {
        backendController.showMainMenu();
	}
	
	@FXML 
	public void onBtnNextClicked() {
		updateTournamentSettings();
        backendController.showPlayerSelection();
	}
	
	private void updateTournamentSettings() {
		
		getBackendController().changedTournamentConfig(TournamentSetingsKeys.GOALS_FOR_WIN, getValue(getTxtGoalsToWin()));
		getBackendController().changedTournamentConfig(TournamentSetingsKeys.TABLES, getValue(getTxtNumberOfTables()));
        //		getBackendController().changedTournamentConfig(TournamentSetingsKeys.MATCHES_TO_WIN, getValue(getTxtMatchesToWin()));
		getBackendController().changedTournamentConfig(TournamentSetingsKeys.POINTS_FOR_WINNER, getValue(getTxtPointsToWin()));
		getBackendController().changedTournamentConfig(TournamentSetingsKeys.POINTS_FOR_DRAW, getValue(getTxtPointsForDraw()));
		getBackendController().changedTournamentConfig(TournamentSetingsKeys.MINUTES_PER_MATCH, getValue(getTxtMinutesPerMatch()));
		getBackendController().changedTournamentConfig(TournamentSetingsKeys.RANDOM_ROUNDS, getValue(getTxtRandomRoundsAtStart()));

	}
	
	private Integer getValue(TextField textfield) {
		
		if (!textfield.getText().isEmpty()) {
			return Integer.valueOf(textfield.getText());
		}
		
		return Integer.valueOf(textfield.getPromptText());
		
	}
	
	@Override
	public void update() {
		//
	}
	

}
