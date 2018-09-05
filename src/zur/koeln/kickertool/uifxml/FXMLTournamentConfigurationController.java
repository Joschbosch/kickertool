package zur.koeln.kickertool.uifxml;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.Tournament;
import zur.koeln.kickertool.tournament.TournamentConfiguration;

@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentConfigurationController {
	@FXML
	private Label lblTournament;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnNext;
	@FXML
	private TextField txtNumberOfTables;
	@FXML
	private TextField txtMatchesToWin;
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
	
	private TournamentConfiguration config;
	
	@FXML 
	public void initialize() {
		
		Tournament currentTournament = TournamentController.getInstance().getCurrentTournament();
		config = currentTournament.getConfig();
		
		getLblTournament().setText(TournamentController.getInstance().getCurrentTournament().getName());
		
		getTxtGoalsToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtNumberOfTables().setTextFormatter(createIntegerTextFormatter());
		getTxtMatchesToWin().setTextFormatter(createIntegerTextFormatter());
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
		getTxtMatchesToWin().setPromptText(String.valueOf(getConfig().getMatchesToWin()));
		getTxtPointsToWin().setPromptText(String.valueOf(getConfig().getPointsForWinner()));
		getTxtPointsForDraw().setPromptText(String.valueOf(getConfig().getPointsForDraw()));
		getTxtMinutesPerMatch().setPromptText(String.valueOf(getConfig().getMinutesPerMatch()));
		getTxtRandomRoundsAtStart().setPromptText(String.valueOf(getConfig().getRandomRounds()));
		
	}
	
	@FXML 
	public void onBtnBackClicked() {
		TournamentController.getInstance().backToMainMenu();
	}
	
	@FXML 
	public void onBtnNextClicked() {
		updateTournamentConfig();
		TournamentController.getInstance().selectPlayer();
	}
	
	private void updateTournamentConfig() {
		
		getConfig().setGoalsToWin(getValue(getTxtGoalsToWin()));
		getConfig().setTableCount(getValue(getTxtNumberOfTables()));
		getConfig().setMatchesToWin(getValue(getTxtMatchesToWin()));
		getConfig().setPointsForWinner(getValue(getTxtPointsToWin()));
		getConfig().setPointsForDraw(getValue(getTxtPointsForDraw()));
		getConfig().setMinutesPerMatch(getValue(getTxtMinutesPerMatch()));
		getConfig().setRandomRounds(getValue(getTxtRandomRoundsAtStart()));
		
	}
	
	private int getValue(TextField textfield) {
		
		if (!textfield.getText().isEmpty()) {
			return Integer.parseInt(textfield.getText());
		}
		
		return Integer.parseInt(textfield.getPromptText());
		
	}

}
