package zur.koeln.kickertool.uifxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.tournament.TournamentConfig;
import zur.koeln.kickertool.tournament.content.Tournament;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentConfigurationController {

    @Autowired
    private BackendController controller;

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
	
	private TournamentConfig config;
	
	@FXML 
	public void initialize() {
		
        Tournament currentTournament = controller.getCurrentTournament();
		config = currentTournament.getConfig();
		
		
        getLblTournament().setText(controller.getCurrentTournament().getName());
		
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
        controller.showMainMenu();
	}
	
	@FXML 
	public void onBtnNextClicked() {
		updateTournamentConfig();
        controller.showPlayerSelection();
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
