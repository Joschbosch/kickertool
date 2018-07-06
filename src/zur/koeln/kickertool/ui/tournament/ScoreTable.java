/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.TournamentStatistics;

public class ScoreTable extends GridPane {

    private TournamentController controller;

    public ScoreTable(TournamentController controller) {

        this.controller = controller;
        createTable();
    }

    /**
     * @param controller
     */
    private void createTable() {
        addLabel("#", 0, 0, false, false);
        addLabel("Player", 1, 0, false, false);
        addLabel(" Matches ", 2, 0, false, false);
        addLabel(" Won ", 3, 0, false, false);
        addLabel(" Lost ", 4, 0, false, false);
        addLabel(" Draw ", 5, 0, false, false);
        addLabel(" Goals ", 6, 0, false, false);
        addLabel(" Con. Goals ", 7, 0, false, false);
        addLabel(" Goals Diff ", 8, 0, false, false);
        addLabel(" Points ", 9, 0, false, false);

        List<TournamentStatistics> table = controller.getCurrentTournament().getTable();

        for (int i = 0; i < table.size(); i++) {
            TournamentStatistics stat = table.get(i);
            boolean darkRow = i % 2 == 0;
            boolean inactive = stat.getPlayer().isPausingTournement();
            int row = i + 1;
            addLabel(String.valueOf(i + 1) + (i < 9 ? " " : ""), 0, row, darkRow, inactive);
            addLabel(stat.getPlayer().getName(), 1, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getMatchesDone()), 2, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getMatchesWon()), 3, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getMatchesLost()), 4, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getMatchesDraw()), 5, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getGoals()), 6, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getGoalsConceded()), 7, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getGoalDiff()), 8, row, darkRow, inactive);
            addLabel(String.valueOf(stat.getPoints()), 9, row, darkRow, inactive);

        }
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
        this.getChildren().clear();
        createTable();
    }
}
