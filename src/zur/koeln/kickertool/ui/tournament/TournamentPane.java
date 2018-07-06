/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import zur.koeln.kickertool.TournamentController;

public class TournamentPane extends GridPane {

    private MatchesTable matchesTable;
    private ScoreTable scoreTable;

    public TournamentPane(TournamentController controller) {
        GridPane innerPane = new GridPane();
        ScrollPane sp = new ScrollPane(innerPane);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        this.add(sp, 0, 0);

        innerPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setHgrow(sp, Priority.ALWAYS);
        GridPane.setVgrow(sp, Priority.ALWAYS);
        sp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        innerPane.setPadding(new Insets(10));
        innerPane.setHgap(10);
        innerPane.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        innerPane.getColumnConstraints().addAll(col1, col2);

        matchesTable = new MatchesTable(controller);
        GridPane.setValignment(matchesTable, VPos.TOP);
        innerPane.add(matchesTable, 0, 0);

        scoreTable = new ScoreTable(controller);
        GridPane.setValignment(scoreTable, VPos.BOTTOM);

        innerPane.add(scoreTable, 1, 0, 2, 1);
        innerPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Button nextRoundButton = new Button("New Round");
        nextRoundButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.nextRound();
            }
        });
        GridPane.setHalignment(nextRoundButton, HPos.CENTER);
        innerPane.add(nextRoundButton, 0, 1);

        Button addPlayerButton = new Button("Add late Player");
        Button removePlayer = new Button("Remove lazy Player");
        Button pausePlayer = new Button("Pause Player");
        Button unpausePlayer = new Button("Unpause Player");

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(addPlayerButton, removePlayer, pausePlayer, unpausePlayer);

        GridPane.setHalignment(buttonBox, HPos.CENTER);
        innerPane.add(buttonBox, 1, 1);

    }

    public void update() {
        matchesTable.update();
        scoreTable.update();
    }
}
