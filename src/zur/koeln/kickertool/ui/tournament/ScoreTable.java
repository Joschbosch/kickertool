/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.TournamentStatistics;

public class ScoreTable extends GridPane {

    private final TournamentController controller;
    private ObservableList<TournamentStatistics> tableData;
    private TableView table;

    public ScoreTable(TournamentController controller) {

        this.controller = controller;
        createTable();
    }

    /**
     * @param controller
     */
    private void createTable() {

        tableData = FXCollections.observableArrayList(controller.getCurrentTournament().getTable());

        table = new TableView();
        table.setEditable(false);
        table.setItems(tableData);

        TableColumn rankCol = new TableColumn("#");
        rankCol.setCellValueFactory(new Callback<CellDataFeatures<TournamentStatistics, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<TournamentStatistics, String> p) {
                return new ReadOnlyObjectWrapper(table.getItems().indexOf(p.getValue()) + 1 + "");
            }
        });
        TableColumn playerCol = new TableColumn("Player");
        playerCol.setCellValueFactory(new Callback<CellDataFeatures<TournamentStatistics, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<TournamentStatistics, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getPlayer().getName());
            }
        });
        TableColumn matchesHeader = new TableColumn("Matches");

        TableColumn matchesCol = new TableColumn("Played");
        matchesCol.setCellValueFactory(new PropertyValueFactory<>("matchesDone"));

        TableColumn matchesWonCol = new TableColumn("Won");
        matchesWonCol.setCellValueFactory(new PropertyValueFactory<>("matchesWon"));

        TableColumn matchesLostCol = new TableColumn("Lost");
        matchesLostCol.setCellValueFactory(new PropertyValueFactory<>("matchesLost"));

        TableColumn matchesDrawCol = new TableColumn("Draw");
        matchesDrawCol.setCellValueFactory(new PropertyValueFactory<>("matchesDraw"));

        TableColumn goalsHeader = new TableColumn("Goal Stats");
        TableColumn goalsCol = new TableColumn("Goals");
        goalsCol.setCellValueFactory(new PropertyValueFactory<>("goals"));

        TableColumn conGoalsCol = new TableColumn("Con. Goals");
        conGoalsCol.setCellValueFactory(new PropertyValueFactory<>("goalsConceded"));

        TableColumn diffGoalsrankCol = new TableColumn("Goals Diff");
        diffGoalsrankCol.setCellValueFactory(new Callback<CellDataFeatures<TournamentStatistics, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<TournamentStatistics, String> p) {
                return new ReadOnlyObjectWrapper(Integer.valueOf(p.getValue().getGoals() - p.getValue().getGoalsConceded()));
            }
        });

        TableColumn pointsCol = new TableColumn("Points");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        matchesHeader.getColumns().addAll(matchesCol, matchesWonCol, matchesLostCol, matchesDrawCol);
        goalsHeader.getColumns().addAll(goalsCol, conGoalsCol, diffGoalsrankCol);
        table.getColumns().addAll(rankCol, playerCol, matchesHeader, goalsHeader, pointsCol);

        
        this.add(table, 0, 0);

        //

        //        for (int i = 0; i < table.size(); i++) {
        //            TournamentStatistics stat = table.get(i);
        //            boolean darkRow = i % 2 == 0;
        //            boolean inactive = stat.getPlayer().isPausingTournement();
        //            int row = i + 1;
        //            addLabel(String.valueOf(i + 1) + (i < 9 ? " " : ""), 0, row, darkRow, inactive);
        //            addLabel(stat.getPlayer().getName(), 1, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getMatchesDone()), 2, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getMatchesWon()), 3, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getMatchesLost()), 4, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getMatchesDraw()), 5, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getGoals()), 6, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getGoalsConceded()), 7, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getGoalDiff()), 8, row, darkRow, inactive);
        //            addLabel(String.valueOf(stat.getPoints()), 9, row, darkRow, inactive);
        //
        //        }
    }

    private void addLabel(String text, int col, int row, boolean dark, boolean inactive) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if (inactive) {
            if (dark) {
                label.setStyle("-fx-text-fill: red ;-fx-background-color:lightgrey;");
            } else {
                label.setStyle("-fx-text-fill: red ;");
            }
        } else {
            if (dark) {
                label.setStyle("-fx-background-color:lightgrey;");
            }
        }
        this.add(label, col, row);
    }

    public void update() {
        table.refresh();
        table.sort();
    }
}
