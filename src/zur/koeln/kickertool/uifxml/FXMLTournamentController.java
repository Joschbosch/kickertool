package zur.koeln.kickertool.uifxml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.tournament.PlayerTournamentStatisticsImpl;
import zur.koeln.kickertool.uifxml.converter.RoundConverter;
import zur.koeln.kickertool.uifxml.converter.TimerStringConverter;
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
	
	@FXML
	public void initialize() {
		
		getLblClock().textProperty().bindBidirectional(getVm().getTimePropertyFromStopWatch(), new TimerStringConverter());
		
		getBtnCreateRound().disableProperty().bind(getVm().getBtnCreateRoundsDisableProperty());
		getBtnStartStopwatch().disableProperty().bind(getVm().getIsRunningPropertyFromStopWatch());
		getBtnResetStopwatch().disableProperty().bind(getVm().getIsRunningPropertyFromStopWatch().not());
		getTglPauseStopwatch().disableProperty().bind(getVm().getIsRunningPropertyFromStopWatch().not());
		getTglPauseStopwatch().selectedProperty().bindBidirectional(getVm().getStopwatchToggleButtonSelectedProperty());
		getTblStatistics().setItems(getVm().getStatistics());
		getCmbRounds().setItems(getVm().getRounds());
		getCmbRounds().setConverter(new RoundConverter());
		getCmbRounds().disableProperty().bind(Bindings.size(getVm().getRounds()).isEqualTo(0));
		
		getBtnPausePlayer().disableProperty().bind(getVm().getBtnPausePlayerDisableProperty());
		getBtnUnpausePlayer().disableProperty().bind(getVm().getBtnResumePlayerDisableProperty());
		
		setupColumns();
		registerListener();
		
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

	private void registerListener() {
		getTblStatistics().getSelectionModel().selectedItemProperty().addListener(event -> {
			
			getVm().enableDisablePauseResumePlayer((PlayerTournamentStatisticsImpl) getTblStatistics().getSelectionModel().getSelectedItem());
			
		});

		getCmbRounds().getSelectionModel().selectedItemProperty().addListener(event -> {
			
			if (getCmbRounds().getSelectionModel().getSelectedItem() == null) {
				return;
			}
			
			Round selectedRound = (Round) getCmbRounds().getSelectionModel().getSelectedItem();
			fillMatchesForRound(selectedRound);
			
		});
	}
	
	private void fillMatchesForRound(final Round selectedRound) {
			
		List<Pane> matchesForRound = getVm().loadMatchesForRound(selectedRound);
		
		getStackGames().getChildren().clear();
		getStackGames().getChildren().addAll(matchesForRound);
		
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
				
				return new SimpleStringProperty(String.valueOf("0"));
			}
		});
		
	}

	@FXML public void onBtnAddPlayerClicked() {
		
		getVm().openAddLatePlayerDialog();
		
	}

	@FXML public void onBtnPausePlayerClicked() {
		
		Player selectedPlayer = getSelectedPlayerInStatisticsTable();
		
		if (selectedPlayer != null) {
			getVm().pausePlayer(selectedPlayer);
		}
		
	}

	@FXML public void onBtnResumePlayerClicked() {
		
		Player selectedPlayer = getSelectedPlayerInStatisticsTable();
		
		if (selectedPlayer != null) {
			getVm().resumePlayer(selectedPlayer);
		}
	}
	
	private Player getSelectedPlayerInStatisticsTable() {
		return ((PlayerTournamentStatistics) getTblStatistics().getSelectionModel().getSelectedItem()).getPlayer();
	}
	
	@FXML public void onBtnCreateRoundClicked() {
		
		getVm().createNewRound();
		getCmbRounds().getSelectionModel().select(getVm().getLastRoundIndex());
		
	}
	
	@FXML public void onBtnResetStopwatchClicked() {
		getVm().resetStopwatch();
	}

	@FXML public void onBtnStartStopwatchClicked() {
		getVm().startStopwatch();
	}
	
	@FXML public void onTglPauseStopwatchClicked() {
		getVm().pauseResumeStopwatch();
	}
	
}
