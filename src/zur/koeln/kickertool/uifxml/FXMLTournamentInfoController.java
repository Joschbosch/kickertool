package zur.koeln.kickertool.uifxml;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.tools.TournamentStopWatch;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentInfoController {
	
	@Autowired
    private BackendController backendController;
    @Autowired
    private ConfigurableApplicationContext ctx;
	
	@FXML
	private VBox stackGames;
	@FXML RowConstraints gridInfo;
	@FXML Label lblClock;
	
	private TournamentStopWatch timer;
	private ObservableList<PlayerTournamentStatistics> statistics;
	@FXML GridPane gridPane;
	@FXML TableView tblStatistics;
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
	
	public void init(TournamentStopWatch refTimer, ObservableList<PlayerTournamentStatistics> refStatistics) {
		
		timer = refTimer;
		statistics = refStatistics;
		
		getTblStatistics().setItems(getStatistics());
		//getLblClock().textProperty().bindBidirectional(getTimer().getTimeSeconds(), new TimerStringConverter());
		setupColumns();
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
				if (backendController.isPlayerPausing(param.getValue().getPlayer())) {
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
	
	public void updateMatches(List<Match> matches) {

		getStackGames().getChildren().clear();
		
		matches.forEach(eMatch -> {
			try {
				FXMLLoader matchEntryLoader = getFXMLLoader(FXMLGUI.MATCH_ENTRY);
				Parent pane = matchEntryLoader.load();
				FXMLMatchEntryController matchEntryController = matchEntryLoader.getController();
//				matchEntryController.setMatch(eMatch);
//				matchEntryController.hideBtnFinish();
				getStackGames().getChildren().add(pane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
	}

	private FXMLLoader getFXMLLoader(FXMLGUI gui) {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(gui.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;
	}
	
}
