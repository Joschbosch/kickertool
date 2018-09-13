package zur.koeln.kickertool.uifxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.content.PlayerTournamentStatistics;
import zur.koeln.kickertool.uifxml.tools.SimpleTimer;
import zur.koeln.kickertool.uifxml.tools.TimerStringConverter;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentController implements UpdateableUIComponent {
	
	@Autowired
    private BackendController backendController;
	
	@FXML
	private TableView tblStatistics;

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
	
	private final SimpleTimer timer = new SimpleTimer();

	@FXML 
	private ToggleButton tglPauseStopwatch;
	@FXML 
	private Button btnStartStopwatch;
	@FXML 
	private Button btnResetStopwatch;

	@FXML TableColumn tblColNumber;

	@FXML TableColumn tblColPlayerName;

	@FXML TableColumn tblColPlayedMatched;

	@FXML TableColumn tblColMatchesWon;

	@FXML TableColumn tblColMatchesLost;

	@FXML TableColumn tblColMatchesRemis;

	@FXML TableColumn tblColGoals;

	@FXML TableColumn tblColGoalsEnemies;

	@FXML TableColumn tblColGoalsDifference;

	@FXML TableColumn tblColPoints;

	@FXML ScrollPane scrPaneMatches;

	@FXML VBox stackGames;
	
	private final List<FXMLMatchEntryController> matchEntryController = new ArrayList<>();

	@FXML
	public void initialize() {
		
		getLblClock().textProperty().bindBidirectional(getTimer().getTimeSeconds(), new TimerStringConverter());
		getTimer().init(getBackendController().getCurrentTournament().getConfig().getMinutesPerMatch());
		
		getBtnStartStopwatch().disableProperty().bind(getTimer().getRunningProperty());
		getBtnResetStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		getTglPauseStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		
		setupColumns();
		
		getTblStatistics().setItems(FXCollections.observableList(getBackendController().getCurrentTable().stream().sorted().collect(Collectors.toList())));
	
	}

	private void setupColumns() {
		
        getTblColPlayerName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(param.getValue().getPlayer().getName());
			}
		});
		
        getTblColPlayedMatched().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesDone()));
			}
		});
		
        getTblColMatchesWon().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesWonCount()));
			}
		});
		
        getTblColMatchesLost().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesLostCount()));
			}
		});
		
        getTblColMatchesRemis().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesDrawCount()));
			}
		});
		
        getTblColGoals().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getGoalsShot()));
			}
		});
		
        getTblColGoalsEnemies().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getGoalsConceded()));
			}
		});
		
        getTblColGoalsDifference().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getGoalDiff()));
			}
		});
		
        getTblColPoints().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getPointsForConfiguration(getBackendController().getCurrentTournament().getConfig())));
			}
		});
		
	}

	@Override
	public void update() {
		getMatchEntryController().forEach(FXMLMatchEntryController::update);
		getTblStatistics().refresh();
		getTblStatistics().getSortOrder().add(getTblColPoints());
		getTblStatistics().sort();
	}

	@FXML public void onBtnAddPlayerClicked() {
		
	}

	@FXML public void onBtnPausePlayerClicked() {
		
	}

	@FXML public void onBtnResumePlayerClicked() {
		
	}

	@FXML public void onBtnCreateRoundClicked() {
		getBackendController().nextRound();
		
		getBackendController().getCurrentTournament().getCurrentRound().getMatches().forEach(eMatch -> {
			try {
				FXMLLoader matchEntryLoader = getMatchEntryFXMLLoader();
				Parent pane = matchEntryLoader.load();
				FXMLMatchEntryController matchEntryController = matchEntryLoader.getController();
				matchEntryController.setMatch(eMatch);
				matchEntryController.setBackendController(backendController);
				getStackGames().getChildren().add(pane);
				getMatchEntryController().add(matchEntryController);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
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
	
	private FXMLLoader getMatchEntryFXMLLoader() {
		return new FXMLLoader(getClass().getResource("MatchEntry.fxml"));
	}
}
