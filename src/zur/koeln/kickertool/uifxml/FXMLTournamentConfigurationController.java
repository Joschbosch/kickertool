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
import zur.koeln.kickertool.uifxml.vm.wrapper.TournamentConfigFields;

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
		
		getTxtNumberOfTables().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.NUMBER_OF_TABLES_DEFAULT));
		getTxtGoalsToWin().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.GOALS_TO_WIN_DEFAULT));
		getTxtMatchesToWin().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.MATCHES_TO_WIN_DEFAULT));
		getTxtPointsToWin().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.POINTS_TO_WIN_DEFAULT));
		getTxtPointsForDraw().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.POINTS_FOR_DRAW_DEFAULT));
		getTxtMinutesPerMatch().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.MINUTES_FOR_MATCH_DEFAULT));
		getTxtRandomRoundsAtStart().promptTextProperty().bind(getVm().getStringProperty(TournamentConfigFields.RANDOM_ROUNDS_START_DEFAULT));
		
		getTxtNumberOfTables().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.NUMBER_OF_TABLES));
		getTxtGoalsToWin().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.GOALS_TO_WIN));
		getTxtMatchesToWin().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.MATCHES_TO_WIN));
		getTxtPointsToWin().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.POINTS_TO_WIN));
		getTxtPointsForDraw().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.POINTS_FOR_DRAW));
		getTxtMinutesPerMatch().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.MINUTES_FOR_MATCH));
		getTxtRandomRoundsAtStart().textProperty().bindBidirectional(getVm().getStringProperty(TournamentConfigFields.RANDOM_ROUNDS_START));
		
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
