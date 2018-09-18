package zur.koeln.kickertool.uifxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.uifxml.cells.RoundConverter;
import zur.koeln.kickertool.uifxml.dialog.AddPlayerDialog;
import zur.koeln.kickertool.uifxml.tools.SimpleTimer;
import zur.koeln.kickertool.uifxml.tools.TimerStringConverter;
import javafx.scene.control.ChoiceBox;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentController implements UpdateableUIComponent {
	
	@Autowired
    private BackendController backendController;
    @Autowired
    private ConfigurableApplicationContext ctx;
	
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
	private boolean selectionModeOn = true;
	private final ObservableList<Round> rounds = FXCollections.observableArrayList();
	@FXML ChoiceBox cmbRounds;
	
	
	@FXML
	public void initialize() {
		
		getLblClock().textProperty().bindBidirectional(getTimer().getTimeSeconds(), new TimerStringConverter());
		getTimer().init(getBackendController().getCurrentTournament().getSettings().getMinutesPerMatch());
		
		getBtnStartStopwatch().disableProperty().bind(getTimer().getRunningProperty());
		getBtnResetStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		getTglPauseStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		
		setupColumns();
		
		getTblStatistics().setItems(FXCollections.observableArrayList(loadTableStatistics()));
		getCmbRounds().setItems(getRounds());
		getCmbRounds().setConverter(new RoundConverter());
		getCmbRounds().disableProperty().bind(Bindings.size(getRounds()).isEqualTo(0));
		
		loadPlayerRounds();
		
		setupListener();
	}
	
	private void setupListener() {
		getTblStatistics().getSelectionModel().selectedItemProperty().addListener(event -> {
			
			if (!isSelectionModeOn()) {
				return;
			}
			
			Player selectedPlayer = getSelectedPlayerInStatisticsTable();
			
			getBtnPausePlayer().setDisable(selectedPlayer.isDummy() || getTblStatistics().getSelectionModel().getSelectedItems().size() == 0 || selectedPlayer.isPausingTournament());
			getBtnUnpausePlayer().setDisable(getTblStatistics().getSelectionModel().getSelectedItems().size() == 0 || !selectedPlayer.isPausingTournament());
			
		});
		
		getCmbRounds().getSelectionModel().selectedItemProperty().addListener(event -> {
			
			if (getCmbRounds().getSelectionModel().getSelectedItem() == null) {
				return;
			}
			
			Round selectedRound = (Round) getCmbRounds().getSelectionModel().getSelectedItem();
			fillMatchesForRound(selectedRound.getRoundNo());
			
		});
	}

	private Player getSelectedPlayerInStatisticsTable() {
		
		if (!isSelectionModeOn()) {
			return null;
		}
		
		return ((PlayerTournamentStatistics) getTblStatistics().getSelectionModel().getSelectedItem()).getPlayer();
	}
	
	private SortedSet<PlayerTournamentStatistics> loadTableStatistics() {
		return getBackendController().getCurrentTable();
	}

	private void setupColumns() {
		
		getTblColNumber().setCellFactory(param -> new TableCell<PlayerTournamentStatistics, String>() {
			
			 @Override
			    protected void updateItem(String item, boolean empty) {
				 	
				 	if (empty) {
				 		setText(null);
				 		return;
				 	}
				 
			      	setText(String.valueOf(getIndex() + 1));
			    }
			 
		});
		
        getTblColPlayerName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
				
				String playerName = param.getValue().getPlayer().getName();
				if (param.getValue().getPlayer().isPausingTournament()) {
					playerName += " (Pausing)";
				}
				
				return new SimpleStringProperty(playerName);
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
				
				return new SimpleStringProperty(String.valueOf(param.getValue().getPointsForConfiguration(getBackendController().getCurrentTournament().getSettings())));
			}
		});
		
	}

	@Override
	public void update() {
		setSelectionModeOn(false);
		getMatchEntryController().forEach(FXMLMatchEntryController::update);
		getTblStatistics().setItems(FXCollections.observableArrayList(loadTableStatistics()));
		getTblStatistics().refresh();
		getTblStatistics().getSortOrder().add(getTblColPoints());
		getTblStatistics().sort();
		setSelectionModeOn(true);
		loadPlayerRounds();
		
		if (getBackendController().getCurrentTournament().getCurrentRound() != null && getBackendController().getCurrentTournament().getCurrentRound().isComplete()) {
			getBtnCreateRound().setDisable(false);
		}
	}
	
	private void loadPlayerRounds() {
		
		if (getBackendController().getCurrentTournament().getCurrentRound() == null) {
			return;
		}
		
		getRounds().clear();
		getRounds().addAll(getBackendController().getCurrentTournament().getCompleteRounds());
		getRounds().add(getBackendController().getCurrentTournament().getCurrentRound());
		getCmbRounds().getSelectionModel().select(getRounds().size() - 1);
	}

	@FXML public void onBtnAddPlayerClicked() {
		
		AddPlayerDialog<List<Player>> addPlayerDialog = new AddPlayerDialog<>(getCtx());
		Optional<List<Player>> selectedPlayer = addPlayerDialog.showAndWait();
		
		if (selectedPlayer.isPresent()) {
			selectedPlayer.get().forEach(ePlayer -> {
				getBackendController().addParticipantToTournament(ePlayer);
			});
		}
		
		update();
	}

	@FXML public void onBtnPausePlayerClicked() {
		Player selectedPlayer = getSelectedPlayerInStatisticsTable();
		
		if (selectedPlayer != null) {
			getBackendController().pausePlayer(selectedPlayer.getUid());
			update();
		}
		
	}

	@FXML public void onBtnResumePlayerClicked() {
		Player selectedPlayer = getSelectedPlayerInStatisticsTable();
		
		if (selectedPlayer != null) {
			getBackendController().unpausePlayer(selectedPlayer.getUid());
			update();
		}
		
		
	}

	@FXML public void onBtnCreateRoundClicked() {
		getBtnCreateRound().setDisable(true);
		
		getBackendController().nextRound();
		loadPlayerRounds();
	}
	
	private void fillMatchesForRound(int round) {
		
		getStackGames().getChildren().clear();
		
		getBackendController().getMatchesForRound(round).forEach(eMatch -> {
			try {
				FXMLLoader matchEntryLoader = getFXMLLoader(FXMLGUI.MATCH_ENTRY);
				Parent pane = matchEntryLoader.load();
				FXMLMatchEntryController matchEntryController = matchEntryLoader.getController();
				matchEntryController.setMatch(eMatch);
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
	
	private FXMLLoader getFXMLLoader(FXMLGUI gui) {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(gui.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;
	}

	private void setSelectionModeOn(boolean selectionModeOn) {
		this.selectionModeOn = selectionModeOn;
	}
	
	
}
