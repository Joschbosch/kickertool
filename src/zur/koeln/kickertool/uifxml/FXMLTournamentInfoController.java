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
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.core.entities.Match;
import zur.koeln.kickertool.core.entities.PlayerStatistics;
import zur.koeln.kickertool.core.logic.StatisticsService;
import zur.koeln.kickertool.uifxml.tools.SimpleTimer;
import zur.koeln.kickertool.uifxml.tools.TimerStringConverter;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentInfoController {
	
	@Autowired
    private BasicBackendController backendController;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private ConfigurableApplicationContext ctx;
	
	@FXML
	private VBox stackGames;
	@FXML RowConstraints gridInfo;
	@FXML Label lblClock;
	
	private SimpleTimer timer;
    private ObservableList<PlayerStatistics> statistics;
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
	
    public void init(SimpleTimer refTimer, ObservableList<PlayerStatistics> refStatistics) {
		
		timer = refTimer;
		statistics = refStatistics;
		
		getTblStatistics().setItems(getStatistics());
		getLblClock().textProperty().bindBidirectional(getTimer().getTimeSeconds(), new TimerStringConverter());
		setupColumns();
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
	
	public void updateMatches(List<Match> matches) {

		getStackGames().getChildren().clear();
		
		matches.forEach(eMatch -> {
			try {
				FXMLLoader matchEntryLoader = getFXMLLoader(FXMLGUI.MATCH_ENTRY);
				Parent pane = matchEntryLoader.load();
				FXMLMatchEntryController matchEntryController = matchEntryLoader.getController();
				matchEntryController.setMatch(eMatch);
				matchEntryController.hideBtnFinish();
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
