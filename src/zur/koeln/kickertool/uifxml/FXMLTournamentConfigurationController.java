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
import zur.koeln.kickertool.api.config.TournamentSettingsKeys;
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
		
		getTxtNumberOfTables().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.TABLES));
		getTxtGoalsToWin().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.GOALS_FOR_WIN));
		getTxtMatchesToWin().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.MATCHES_TO_WIN));
		getTxtPointsToWin().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.POINTS_FOR_WINNER));
		getTxtPointsForDraw().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.POINTS_FOR_DRAW));
		getTxtMinutesPerMatch().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.MINUTES_PER_MATCH));
		getTxtRandomRoundsAtStart().promptTextProperty().bind(getVm().getStringPromptProperty(TournamentSettingsKeys.RANDOM_ROUNDS));
		
		getTxtNumberOfTables().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.TABLES));
		getTxtGoalsToWin().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.GOALS_FOR_WIN));
		getTxtMatchesToWin().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.MATCHES_TO_WIN));
		getTxtPointsToWin().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.POINTS_FOR_WINNER));
		getTxtPointsForDraw().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.POINTS_FOR_DRAW));
		getTxtMinutesPerMatch().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.MINUTES_PER_MATCH));
		getTxtRandomRoundsAtStart().textProperty().bindBidirectional(getVm().getStringProperty(TournamentSettingsKeys.RANDOM_ROUNDS));
		
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
