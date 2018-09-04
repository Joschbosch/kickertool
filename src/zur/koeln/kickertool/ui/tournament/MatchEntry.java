/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.Match;

public class MatchEntry extends GridPane {

    /**
     * @param match
     */
    public MatchEntry(Match match, TournamentController controller) {

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(40);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(10);
        this.getColumnConstraints().addAll(col1, col2, col3, col4);

        Label team1 = new Label(match.createHomeTeamString());
        Label team2 = new Label(match.createVisitingTeamString());
        Button scoreButton = new Button(match.getScoreHome() + ":" + match.getScoreVisiting());
        scoreButton.setStyle("-fx-background-color: transparent;");
        //        scoreButton.setDisable(match.getResult() != null);
        scoreButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ScoreDialog<Pair<Integer, Integer>> diag = new ScoreDialog<>(match, controller);
                Optional<Pair<Integer, Integer>> result = diag.showAndWait();
                if (result.isPresent()) {
                    controller.updateMatchResult(match, result.get().getKey(), result.get().getValue());
                }
            }
        });

        String tableString = "TBA";
        if (match.getTableNo() != -1) {
            tableString = String.valueOf(match.getTableNo());
            if (match.getResult() != null) {
                tableString = "Finished";
            }
        }

        Label tableLabel = new Label(tableString);

        GridPane.setHalignment(team1, HPos.CENTER);
        GridPane.setHalignment(team2, HPos.CENTER);
        GridPane.setHalignment(scoreButton, HPos.CENTER);
        GridPane.setHalignment(tableLabel, HPos.RIGHT);

        this.add(team1, 0, 0);
        this.add(scoreButton, 1, 0);
        this.add(team2, 2, 0);
        this.add(tableLabel, 3, 0);

    }

}
