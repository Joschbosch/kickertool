package zur.koeln.kickertool.uifxml;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.uifxml.converter.TimerStringConverter;
import zur.koeln.kickertool.uifxml.vm.TournamentViewModel;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLTournamentInfoController {
	
	@FXML
	private VBox stackGames;
	@FXML 
	RowConstraints gridInfo;
	@FXML 
	Label lblClock;
	@FXML 
	GridPane gridPane;
	@FXML 
	TableView tblStatistics;
	@FXML 
	TableColumn tblColNumber;
	@FXML 
	TableColumn tblColPlayerName;
	@FXML 
	TableColumn tblColPlayedMatched;
	@FXML 
	TableColumn tblColMatchesWon;
	@FXML 
	TableColumn tblColMatchesLost;
	@FXML 
	TableColumn tblColMatchesRemis;
	@FXML 
	TableColumn tblColGoals;
	@FXML 
	TableColumn tblColGoalsEnemies;
	@FXML 
	TableColumn tblColGoalsDifference;
	@FXML 
	TableColumn tblColPoints;
	
	private TournamentViewModel vm;
	
	public void init(TournamentViewModel newVm) {

		vm = newVm;
		
		getLblClock().textProperty().bindBidirectional(getVm().getTimePropertyFromStopWatch(), new TimerStringConverter());
		getTblStatistics().setItems(getVm().getStatistics());

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

	public void fillMatchesForRound(Round selectedRound) {
		List<Pane> matchesForRound = getVm().loadInfoMatchesForRound(selectedRound);
		
		getStackGames().getChildren().clear();
		getStackGames().getChildren().addAll(matchesForRound);
	}
	
}
