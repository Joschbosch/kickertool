package zur.koeln.kickertool.uifxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BackendController;
import javafx.scene.control.ToggleButton;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentController implements GUIUpdate {
	
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
	@FXML
	private Button btnStartRound;
	@FXML 
	private ToggleButton tglPauseStopwatch;

	@FXML
	public void initialize() {
		
	}

	@Override
	public void update() {
		//
	}
	
	public void hideButtons() {
		getGridButtons().setVisible(false);
	}

	@FXML 
	public void onBtnAddPlayerClicked() {
		
	}

	@FXML 
	public void onBtnPausePlayerClicked() {
		
	}

	@FXML 
	public void onBtnResumePlayerClicked() {
		
	}

	@FXML 
	public void onBtnCreateRoundClicked() {
		backendController.nextRound();
	}

	@FXML 
	public void onBtnStartClicked() {
		
	}

	@FXML 
	public void onTglPauseStopwatchClicked() 
	{
		
	}
	
}
