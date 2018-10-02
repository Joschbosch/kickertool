package zur.koeln.kickertool.uifxml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.uifxml.converter.RoundConverter;
import zur.koeln.kickertool.uifxml.converter.TimerStringConverter;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.vm.TournamentViewModel;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentController {

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
	@FXML 
	private ToggleButton tglPauseStopwatch;
	@FXML 
	private Button btnStartStopwatch;
	@FXML 
	private Button btnResetStopwatch;
	@FXML
	private TableColumn tblColNumber;
	@FXML 
	private TableColumn tblColPlayerName;
	@FXML 
	private TableColumn tblColPlayedMatched;
	@FXML 
	private TableColumn tblColMatchesWon;
	@FXML 
	private TableColumn tblColMatchesLost;
	@FXML 
	private TableColumn tblColMatchesRemis;
	@FXML 
	private TableColumn tblColGoals;
	@FXML 
	private TableColumn tblColGoalsEnemies;
	@FXML 
	private TableColumn tblColGoalsDifference;
	@FXML 
	private TableColumn tblColPoints;
	@FXML 
	private ScrollPane scrPaneMatches;
	@FXML 
	private VBox stackGames;
	@FXML 
	private ChoiceBox cmbRounds;
	
	@Autowired
	private TournamentViewModel vm;
	
	private final List<FXMLMatchEntryController> matchEntryController = new ArrayList<>();
	private boolean selectionModeOn = true;
	
	private FXMLTournamentInfoController tournamentInfoController;
	
	@FXML
	public void initialize() {
		
		getLblClock().textProperty().bindBidirectional(getVm().getTimePropertyFromStopWatch(), new TimerStringConverter());
		
		getBtnStartStopwatch().disableProperty().bind(getVm().getIsRunningPropertyFromStopWatch());
		getBtnResetStopwatch().disableProperty().bind(getVm().getIsRunningPropertyFromStopWatch().not());
		getTglPauseStopwatch().disableProperty().bind(getVm().getIsRunningPropertyFromStopWatch().not());
		getTblStatistics().setItems(getVm().getStatistics());
		getCmbRounds().setItems(getVm().getRounds());
		getCmbRounds().setConverter(new RoundConverter());
		getCmbRounds().disableProperty().bind(Bindings.size(getVm().getRounds()).isEqualTo(0));
		
		setupColumns();
		setupListener();
		
		getVm().init();
		
		// showTournamentInfoGUI();
	}
	
	private void showTournamentInfoGUI() {
		
//		Stage secondaryStage = new Stage();
//		secondaryStage.setTitle("parcIT Kickerturnier Helferlein");
//		secondaryStage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));
//
//		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLGUI.TOURMANENT_INFO.getFxmlFile()));
//		try {
//			loader.setControllerFactory(getCtx()::getBean);
//			
//			Pane rootPane = (Pane) loader.load();
//			tournamentInfoController = loader.getController();
//			tournamentInfoController.init(getTimer(), getStatistics());
//			Scene newScene = new Scene(rootPane);
//			secondaryStage.setScene(newScene);
//			secondaryStage.centerOnScreen();
//	        secondaryStage.show();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	private void setupListener() {
//		getTblStatistics().getSelectionModel().selectedItemProperty().addListener(event -> {
//			
//			Player selectedPlayer = getSelectedPlayerInStatisticsTable();
//			
//			if (selectedPlayer != null) {
//				getBtnPausePlayer().setDisable(selectedPlayer.isDummy() || getTblStatistics().getSelectionModel().getSelectedItems().size() == 0 || backendController.isPlayerPausing(selectedPlayer));
//				getBtnUnpausePlayer().setDisable(getTblStatistics().getSelectionModel().getSelectedItems().size() == 0 || !backendController.isPlayerPausing(selectedPlayer));
//				
//			}
//			
//			
//		});
//		
//		getCmbRounds().getSelectionModel().selectedItemProperty().addListener(event -> {
//			
//			if (getCmbRounds().getSelectionModel().getSelectedItem() == null) {
//				return;
//			}
//			
//			Round selectedRound = (Round) getCmbRounds().getSelectionModel().getSelectedItem();
//			fillMatchesForRound(selectedRound, getCmbRounds().getSelectionModel().getSelectedIndex());
//			
//		});
	}

	private Player getSelectedPlayerInStatisticsTable() {
		
		if (!isSelectionModeOn()) {
			return null;
		}
		
		return ((PlayerTournamentStatistics) getTblStatistics().getSelectionModel().getSelectedItem()).getPlayer();
	}
	
	private void setupColumns() {
		
//		getTblColNumber().setCellFactory(param -> new TableCell<PlayerTournamentStatistics, String>() {
//			
//			 @Override
//			    protected void updateItem(String item, boolean empty) {
//				 	
//				 	if (empty) {
//				 		setText(null);
//				 		return;
//				 	}
//				 
//			      	setText(String.valueOf(getIndex() + 1));
//			    }
//			 
//		});
//		
//        getTblColPlayerName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				String playerName = param.getValue().getPlayer().getName();
//				if (backendController.isPlayerPausing(param.getValue().getPlayer())) {
//					playerName += " (Pausing)";
//				}
//				
//				return new SimpleStringProperty(playerName);
//			}
//		});
//		
//        getTblColPlayedMatched().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesDone()));
//			}
//		});
//		
//        getTblColMatchesWon().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesWonCount()));
//			}
//		});
//		
//        getTblColMatchesLost().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesLostCount()));
//			}
//		});
//		
//        getTblColMatchesRemis().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getMatchesDrawCount()));
//			}
//		});
//		
//        getTblColGoals().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getGoalsShot()));
//			}
//		});
//		
//        getTblColGoalsEnemies().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getGoalsConceded()));
//			}
//		});
//		
//        getTblColGoalsDifference().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getGoalDiff()));
//			}
//		});
//		
//        getTblColPoints().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerTournamentStatistics, String>, ObservableValue<String>>() {
//
//			@Override
//            public ObservableValue<String> call(CellDataFeatures<PlayerTournamentStatistics, String> param) {
//				
//				return new SimpleStringProperty(String.valueOf(param.getValue().getPointsForConfiguration(getBackendController().getCurrentTournament().getSettings())));
//			}
//		});
		
	}

	public void update() {
//		setSelectionModeOn(false);
//		getMatchEntryController().forEach(FXMLMatchEntryController::update);
//		getStatistics().clear();
//		getStatistics().addAll(loadTableStatistics());
//		setSelectionModeOn(true);
//		loadPlayerRounds();
//		
//		if (getBackendController().getCurrentTournament().getCurrentRound() != null && getBackendController().getCurrentTournament().getCurrentRound().isComplete()) {
//			getBtnCreateRound().setDisable(false);
//		}
	}
	
	private void loadPlayerRounds() {
//		Round selectedRound = null;
//		
//		if (getCmbRounds().getSelectionModel().getSelectedItem() != null) {
//			selectedRound = (Round) getCmbRounds().getSelectionModel().getSelectedItem();
//		}
//		
//		if (getBackendController().getCurrentTournament().getCurrentRound() == null) {
//			return;
//		}
//		
//		getRounds().clear();
//		getRounds().addAll(getBackendController().getCurrentTournament().getCompleteRounds());
//		getRounds().add(getBackendController().getCurrentTournament().getCurrentRound());
//		
//		if (selectedRound != null) {
//			getCmbRounds().getSelectionModel().select(selectedRound);
//		} else {
//			getCmbRounds().getSelectionModel().select(getRounds().size() - 1);
//		}
//		
	}

	@FXML public void onBtnAddPlayerClicked() {
		
//		AddPlayerDialog<List<Player>> addPlayerDialog = new AddPlayerDialog<>(getCtx());
//		Optional<List<Player>> selectedPlayer = addPlayerDialog.showAndWait();
//		
//		if (selectedPlayer.isPresent()) {
//			selectedPlayer.get().forEach(ePlayer -> {
//				getBackendController().addParticipantToTournament(ePlayer);
//			});
//		}
//		
//		update();
	}

	@FXML public void onBtnPausePlayerClicked() {
//		Player selectedPlayer = getSelectedPlayerInStatisticsTable();
//		
//		if (selectedPlayer != null) {
//			getBackendController().pausePlayer(selectedPlayer.getUid());
//			update();
//		}
		
	}

	@FXML public void onBtnResumePlayerClicked() {
//		Player selectedPlayer = getSelectedPlayerInStatisticsTable();
//		
//		if (selectedPlayer != null) {
//			getBackendController().unpausePlayer(selectedPlayer.getUid());
//			update();
//		}
		
		
	}

	@FXML public void onBtnCreateRoundClicked() {
//		getBtnCreateRound().setDisable(true);
//		
//		getBackendController().nextRound();
//		loadPlayerRounds();
//		getCmbRounds().getSelectionModel().select(getRounds().size() - 1);
	}
	
	private void fillMatchesForRound(final Round selectedRound, int selectedIndex) {
		
//		getStackGames().getChildren().clear();
//		
//		if (!selectedRound.isComplete() || selectedIndex == (getRounds().size() - 1)) {
//			getTournamentInfoController().updateMatches(getBackendController().getMatchesForRound(selectedRound.getRoundNo()));
//		}
//		
//		getBackendController().getMatchesForRound(selectedRound.getRoundNo()).forEach(eMatch -> {
//			try {
//				FXMLLoader matchEntryLoader = getFXMLLoader(FXMLGUI.MATCH_ENTRY);
//				Parent pane = matchEntryLoader.load();
//				FXMLMatchEntryController matchEntryController = matchEntryLoader.getController();
//				matchEntryController.setMatch(eMatch);
//				getStackGames().getChildren().add(pane);
//				getMatchEntryController().add(matchEntryController);
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
	}

	@FXML public void onBtnResetStopwatchClicked() {
//		getTimer().reset();
//		getTglPauseStopwatch().setSelected(false);
	}

	@FXML public void onBtnStartStopwatchClicked() {
//		getTimer().start();
	}
	
	@FXML public void onTglPauseStopwatchClicked() {
		
//		if (getTglPauseStopwatch().isSelected()) {
//			getTimer().pause();
//		} else {
//			getTimer().resume();
//		}
	}
	
	private FXMLLoader getFXMLLoader(FXMLGUI gui) {
		return null;
//		FXMLLoader loader = new FXMLLoader(getClass().getResource(gui.getFxmlFile()));
//		loader.setControllerFactory(getCtx()::getBean);
//		return loader;
	}

	private void setSelectionModeOn(boolean selectionModeOn) {
		this.selectionModeOn = selectionModeOn;
	}
	
	
}
