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
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;
import zur.koeln.kickertool.uifxml.vm.TournamentConfigurationViewModel;

@Component
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
	
	@Autowired
	private TournamentConfigurationViewModel vm;
	@Autowired
	private FXMLGUIservice guiService;
	
	@FXML 
	public void initialize() {
		
		getVm().loadTournamentSettings();
		
		getLblTournament().textProperty().bind(getVm().getLblTournamentNameProperty());
		
		getTxtNumberOfTables().promptTextProperty().bind(getVm().getTxtNumberOfTablesPromptProperty());
		getTxtGoalsToWin().promptTextProperty().bind(getVm().getTxtGoalsToWinPromptProperty());
		getTxtMatchesToWin().promptTextProperty().bind(getVm().getTxtMatchesToWinPromptProperty());
		getTxtPointsToWin().promptTextProperty().bind(getVm().getTxtPointsToWinPromptProperty());
		getTxtPointsForDraw().promptTextProperty().bind(getVm().getTxtPointsForDrawPromptProperty());
		getTxtMinutesPerMatch().promptTextProperty().bind(getVm().getTxtMinutesPerMatchPromptProperty());
		getTxtRandomRoundsAtStart().promptTextProperty().bind(getVm().getTxtRandomRoundsAtStartPromptProperty());
		
		getTxtNumberOfTables().textProperty().bindBidirectional(getVm().getTxtNumberOfTablesProperty());
		getTxtGoalsToWin().textProperty().bindBidirectional(getVm().getTxtGoalsToWinProperty());
		getTxtMatchesToWin().textProperty().bindBidirectional(getVm().getTxtMatchesToWinProperty());
		getTxtPointsToWin().textProperty().bindBidirectional(getVm().getTxtPointsToWinProperty());
		getTxtPointsForDraw().textProperty().bindBidirectional(getVm().getTxtPointsForDrawProperty());
		getTxtMinutesPerMatch().textProperty().bindBidirectional(getVm().getTxtMinutesPerMatchProperty());
		getTxtRandomRoundsAtStart().textProperty().bindBidirectional(getVm().getTxtRandomRoundsAtStartProperty());
		
		setTextFormatter();
	}
	
	private void setTextFormatter() {
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
		getGuiService().switchToScene(FXMLGUI.MAIN_MENU);
	}
	
	@FXML 
	public void onBtnNextClicked() {
		getVm().updateTournamentSettings();
		getGuiService().switchToScene(FXMLGUI.PLAYER_SELECTION);
	}
}
