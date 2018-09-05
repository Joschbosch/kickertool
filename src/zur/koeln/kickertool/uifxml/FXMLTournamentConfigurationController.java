package zur.koeln.kickertool.uifxml;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.TournamentControllerService;

@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentConfigurationController {

    @Autowired
    private TournamentControllerService controller;

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
	
	@FXML 
	public void initialize() {
        getLblTournament().setText(controller.getCurrentTournament().getName());
		
		getTxtGoalsToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtNumberOfTables().setTextFormatter(createIntegerTextFormatter());
		getTxtMatchesToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtPointsToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtPointsForDraw().setTextFormatter(createIntegerTextFormatter());
		getTxtMinutesPerMatch().setTextFormatter(createIntegerTextFormatter());
		getTxtRandomRoundsAtStart().setTextFormatter(createIntegerTextFormatter());
	}
	
	private TextFormatter<Integer> createIntegerTextFormatter() {
		return new TextFormatter<>(new IntegerStringConverter());
	}
	
	@FXML 
	public void onBtnBackClicked() {
        controller.showMainMenu();
	}
	
	@FXML 
	public void onBtnNextClicked() {
		
	}

}
