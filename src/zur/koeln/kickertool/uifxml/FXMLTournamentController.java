package zur.koeln.kickertool.uifxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.core.entities.Player;
import zur.koeln.kickertool.core.entities.PlayerStatistics;
import zur.koeln.kickertool.core.entities.Round;
import zur.koeln.kickertool.core.logic.StatisticsService;
import zur.koeln.kickertool.uifxml.cells.RoundConverter;
import zur.koeln.kickertool.uifxml.dialog.AddPlayerDialog;
import zur.koeln.kickertool.uifxml.tools.SimpleTimer;
import zur.koeln.kickertool.uifxml.tools.TimerStringConverter;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentController implements UpdateableUIComponent {
	
	@Autowired
    private BasicBackendController backendController;

    @Autowired
    private StatisticsService statisticsService;

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
    private final ObservableList<PlayerStatistics> statistics = FXCollections.observableArrayList();
	@FXML ChoiceBox cmbRounds;
	
	private FXMLTournamentInfoController tournamentInfoController;
	
	
	@FXML
	public void initialize() {
		
		getLblClock().textProperty().bindBidirectional(getTimer().getTimeSeconds(), new TimerStringConverter());
		getTimer().init(getBackendController().getCurrentTournament().getSettings().getMinutesPerMatch());
		
		getBtnStartStopwatch().disableProperty().bind(getTimer().getRunningProperty());
		getBtnResetStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		getTglPauseStopwatch().disableProperty().bind(getTimer().getRunningProperty().not());
		
		setupColumns();
		
		getStatistics().addAll(loadTableStatistics());
		
		getTblStatistics().setItems(getStatistics());
		getCmbRounds().setItems(getRounds());
		getCmbRounds().setConverter(new RoundConverter());
		getCmbRounds().disableProperty().bind(Bindings.size(getRounds()).isEqualTo(0));
		
		loadPlayerRounds();
		
		setupListener();
		
		showTournamentInfoGUI();
	}
	
	private void showTournamentInfoGUI() {
		
		Stage secondaryStage = new Stage();
		secondaryStage.setTitle("parcIT Kickerturnier Helferlein");
		secondaryStage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));

		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLGUI.TOURMANENT_INFO.getFxmlFile()));
		try {
			loader.setControllerFactory(getCtx()::getBean);
			
			Pane rootPane = (Pane) loader.load();
			tournamentInfoController = loader.getController();
			tournamentInfoController.init(getTimer(), getStatistics());
			Scene newScene = new Scene(rootPane);
			secondaryStage.setScene(newScene);
			secondaryStage.centerOnScreen();
	        secondaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setupListener() {
		getTblStatistics().getSelectionModel().selectedItemProperty().addListener(event -> {
			
			Player selectedPlayer = getSelectedPlayerInStatisticsTable();
			
			if (selectedPlayer != null) {
				getBtnPausePlayer().setDisable(selectedPlayer.isDummy() || getTblStatistics().getSelectionModel().getSelectedItems().size() == 0 || backendController.isPlayerPausing(selectedPlayer));
				getBtnUnpausePlayer().setDisable(getTblStatistics().getSelectionModel().getSelectedItems().size() == 0 || !backendController.isPlayerPausing(selectedPlayer));
				
			}
			
			
		});
		
		getCmbRounds().getSelectionModel().selectedItemProperty().addListener(event -> {
			
			if (getCmbRounds().getSelectionModel().getSelectedItem() == null) {
				return;
			}
			
			Round selectedRound = (Round) getCmbRounds().getSelectionModel().getSelectedItem();
			fillMatchesForRound(selectedRound, getCmbRounds().getSelectionModel().getSelectedIndex());
			
		});
	}

	private Player getSelectedPlayerInStatisticsTable() {
		
		if (!isSelectionModeOn()) {
			return null;
		}
		
        return ((PlayerStatistics) getTblStatistics().getSelectionModel().getSelectedItem()).getPlayer();
	}
	
    private List<PlayerStatistics> loadTableStatistics() {
		return getBackendController().getCurrentTable();
	}

	private void setupColumns() {
		
        getTblColNumber().setCellFactory(param -> new TableCell<PlayerStatistics, String>() {
			
			 @Override
			    protected void updateItem(String item, boolean empty) {
				 	
				 	if (empty) {
				 		setText(null);
				 		return;
				 	}
				 
			      	setText(String.valueOf(getIndex() + 1));
			    }
			 
		});
		
        getTblColPlayerName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
				String playerName = param.getValue().getPlayer().getName();
				if (backendController.isPlayerPausing(param.getValue().getPlayer())) {
					playerName += " (Pausing)";
				}
				
				return new SimpleStringProperty(playerName);
			}
		});
		
        getTblColPlayedMatched().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getMatchesDone(param.getValue())));
			}
		});
		
        getTblColMatchesWon().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getMatchesWonCount(param.getValue())));
			}
		});
		
        getTblColMatchesLost().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getMatchesLostCount(param.getValue())));
			}
		});
		
        getTblColMatchesRemis().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getMatchesDrawCount(param.getValue())));
			}
		});
		
        getTblColGoals().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getGoalsShot(param.getValue())));
			}
		});
		
        getTblColGoalsEnemies().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getGoalsConceded(param.getValue())));
			}
		});
		
        getTblColGoalsDifference().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getGoalDiff(param.getValue())));
			}
		});
		
        getTblColPoints().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerStatistics, String>, ObservableValue<String>>() {

			@Override
            public ObservableValue<String> call(CellDataFeatures<PlayerStatistics, String> param) {
				
                return new SimpleStringProperty(String.valueOf(statisticsService.getPointsForConfiguration(param.getValue(), getBackendController().getCurrentTournament().getSettings())));
			}
		});
		
	}

	@Override
	public void update() {
		setSelectionModeOn(false);
		getMatchEntryController().forEach(FXMLMatchEntryController::update);
		getStatistics().clear();
		getStatistics().addAll(loadTableStatistics());
		setSelectionModeOn(true);
		loadPlayerRounds();
		
		if (getBackendController().getCurrentTournament().getCurrentRound() != null && getBackendController().getCurrentTournament().getCurrentRound().isComplete()) {
			getBtnCreateRound().setDisable(false);
		}
	}
	
	private void loadPlayerRounds() {
		Round selectedRound = null;
		
		if (getCmbRounds().getSelectionModel().getSelectedItem() != null) {
			selectedRound = (Round) getCmbRounds().getSelectionModel().getSelectedItem();
		}
		
		if (getBackendController().getCurrentTournament().getCurrentRound() == null) {
			return;
		}
		
		getRounds().clear();
		getRounds().addAll(getBackendController().getCurrentTournament().getCompleteRounds());
		getRounds().add(getBackendController().getCurrentTournament().getCurrentRound());
		
		if (selectedRound != null) {
			getCmbRounds().getSelectionModel().select(selectedRound);
		} else {
			getCmbRounds().getSelectionModel().select(getRounds().size() - 1);
		}
		
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
		getCmbRounds().getSelectionModel().select(getRounds().size() - 1);
	}
	
	private void fillMatchesForRound(final Round selectedRound, int selectedIndex) {
		
		getStackGames().getChildren().clear();
		
		if (!selectedRound.isComplete() || selectedIndex == (getRounds().size() - 1)) {
			getTournamentInfoController().updateMatches(getBackendController().getMatchesForRound(selectedRound.getRoundNo()));
		}
		
		getBackendController().getMatchesForRound(selectedRound.getRoundNo()).forEach(eMatch -> {
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
