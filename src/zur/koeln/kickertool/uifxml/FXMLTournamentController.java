package zur.koeln.kickertool.uifxml;

import javax.swing.text.NumberFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.uifxml.tools.SimpleTimer;
import zur.koeln.kickertool.uifxml.tools.TimerStringConverter;
import javafx.scene.control.ToggleButton;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentController implements UpdateableUIComponent {
	
	@Autowired
    private BackendController backendController;
	
	@FXML
	private TableView tblStatistics;
	@FXML
	private StackPane stackGames;
	@FXML
	private Label lblClock;
	@FXML
	private GridPane gridButtons;
	@FXML
	private HBox hboxBtnsTable;
	@FXML
	private Button btnAddPlayer;
	@FXML
	private Button btnPausePlayer;
	@FXML
	private Button btnUnpausePlayer;
	@FXML
	private Button btnCreateRound;
	
	private SimpleTimer timer = new SimpleTimer();

	@FXML 
	private ToggleButton tglPauseStopwatch;
	@FXML 
	private Button btnStartStopwatch;
	@FXML 
	private Button btnResetStopwatch;

	@FXML
	public void initialize() {
		
		getLblClock().textProperty().bindBidirectional(getTimer().getTimeSeconds(), new TimerStringConverter());
		getTimer().init(getBackendController().getCurrentTournament().getConfig().getMinutesPerMatch());
		
		getBtnStartStopwatch().disableProperty().bind(getTimer().getRunningProperty());
		getBtnResetStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		getTglPauseStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		
	}

	@Override
	public void update() {
		//
	}

	@FXML public void onBtnAddPlayerClicked() {
		
	}

	@FXML public void onBtnPausePlayerClicked() {
		
	}

	@FXML public void onBtnResumePlayerClicked() {
		
	}

	@FXML public void onBtnCreateRoundClicked() {
		
	}

	@FXML public void onBtnResetStopwatchClicked() {
		getTimer().reset();
		getTglPauseStopwatch().setSelected(false);
	}

	@FXML public void onBtnStartStopwatchClicked() {
		getTimer().start();
	}
	
	@FXML public void onTglPauseStopwatchClicked() {
		
		if (getTglPauseStopwatch().isSelected()) {
			getTimer().pause();
		} else {
			getTimer().resume();
		}
	}
	
}
