/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import java.util.UUID;

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
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.TournamentStatistics;

public class ScoreTable extends GridPane {

    private final TournamentController controller;

    private ObservableList<TournamentStatistics> tableData;

    private TableView<TournamentStatistics> table;

    public ScoreTable(TournamentController controller) {

        this.controller = controller;
        createTable();
    }

    /**
     * @param controller
     */
    private void createTable() {

        tableData = FXCollections.observableArrayList(controller.getCurrentTournament().getTableCopySortedByPoints());

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
                Player player = controller.getPlayer(p.getValue().getPlayerId());
                String playerString = player.getName();
                if (player.isPausingTournament()) {
                    playerString += " (pausing)";
                }
                return new ReadOnlyObjectWrapper(playerString);

            }
        });
        TableColumn matchesHeader = new TableColumn("Matches");

        TableColumn matchesCol = new TableColumn("Played");
        matchesCol.setCellValueFactory(new PropertyValueFactory<>("matchesDone"));

        TableColumn matchesWonCol = new TableColumn("Won");
        matchesWonCol.setCellValueFactory(new PropertyValueFactory<>("matchesWonCount"));

        TableColumn matchesLostCol = new TableColumn("Lost");
        matchesLostCol.setCellValueFactory(new PropertyValueFactory<>("matchesLostCount"));

        TableColumn matchesDrawCol = new TableColumn("Draw");
        matchesDrawCol.setCellValueFactory(new PropertyValueFactory<>("matchesDrawCount"));

        TableColumn goalsHeader = new TableColumn("Goal Stats");
        TableColumn goalsCol = new TableColumn("Goals");
        goalsCol.setCellValueFactory(new PropertyValueFactory<>("goals"));

        TableColumn conGoalsCol = new TableColumn("Con. Goals");
        conGoalsCol.setCellValueFactory(new PropertyValueFactory<>("goalsConceded"));

        TableColumn diffGoalsrankCol = new TableColumn("Goals Diff");
        diffGoalsrankCol
            .setCellValueFactory(p -> new ReadOnlyObjectWrapper(Integer
                .valueOf(((CellDataFeatures<TournamentStatistics, String>) p).getValue().calcGoalsShot()
                    - ((CellDataFeatures<TournamentStatistics, String>) p).getValue().calcGoalsConceded())));

        TableColumn pointsCol = new TableColumn("Points");
        pointsCol.setCellValueFactory(
            p -> new ReadOnlyObjectWrapper(Long.valueOf(((CellDataFeatures<TournamentStatistics, String>) p).getValue()
                .calcPointsForConfiguration(controller.getCurrentTournament().getConfig()))));

        matchesHeader.getColumns().addAll(matchesCol, matchesWonCol, matchesLostCol, matchesDrawCol);
        goalsHeader.getColumns().addAll(goalsCol, conGoalsCol, diffGoalsrankCol);
        table.getColumns().addAll(rankCol, playerCol, matchesHeader, goalsHeader, pointsCol);

        this.add(table, 0, 0);

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

        tableData = FXCollections.observableArrayList(controller.getCurrentTournament().getTableCopySortedByPoints());
        table.setItems(tableData);
        table.refresh();
        table.sort();
    }

    public UUID getSelectedPlayer() {
        if (table != null && table.getSelectionModel() != null && table.getSelectionModel().getSelectedItem() != null) {
            return table.getSelectionModel().getSelectedItem().getPlayerId();
        }
        return null;
    }
}
