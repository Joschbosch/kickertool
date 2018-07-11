/**
 * 
 */
package zur.koeln.kickertool.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.TournamentConfiguration;

public class TournamentConfigurationPane extends GridPane {

    private final TournamentController controller;

    public TournamentConfigurationPane(TournamentController controller) {
        this.controller = controller;
        this.setAlignment(Pos.CENTER);

        this.setHgap(5);
        this.setVgap(15);

        final Label label = new Label(controller.getCurrentTournament().getName());
        label.setFont(new Font("Arial", 20));
        this.add(label, 0, 0);
        GridPane.setColumnSpan(label, Integer.valueOf(2));
        GridPane.setHalignment(label, HPos.CENTER);

        TournamentConfiguration config = controller.getCurrentTournament().getConfig();

        createSpinnerAndLabel(1, "Number of tables", 1, 100, config.getTableCount(), "tables");
        createSpinnerAndLabel(2, "Matches to win", 1, 100, config.getMatchesToWin(), "matchesToWin");
        createSpinnerAndLabel(3, "Goals for win", 1, 100, config.getGoalsToWin(), "goalsForWin");
        createSpinnerAndLabel(4, "Points for winner", 0, 100, config.getPointsForWinner(), "pointForWinner");
        createSpinnerAndLabel(5, "Points for draw", 0, 100, config.getPointsForDraw(), "pointsForDraw");
        createSpinnerAndLabel(6, "Minutes per match", 1, 100, config.getMinutesPerMatch(), "minutesPerMatch");
        createSpinnerAndLabel(7, "Random rounds at start", 0, 100, config.getRandomRounds(), "randomRounds");

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.backToMainMenu();
            }
        });
        Button next = new Button("Select Player");
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.selectPlayer();
            }
        });

        this.add(back, 0, 8);
        this.add(next, 1, 8);
        GridPane.setHalignment(next, HPos.RIGHT);
    }

    /**
     * 
     */
    private void createSpinnerAndLabel(int gridRow, String labelText, int minValue, int maxValue, int initialValue,
            String configKey) {
        Label label = new Label(labelText);
        final Spinner<Integer> spinner = new Spinner<Integer>();
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue);
        spinner.setValueFactory(valueFactory);

        spinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                controller.changedTournamentConfig(configKey, newValue);
            }
        });

        this.add(label, 0, gridRow);
        this.add(spinner, 1, gridRow);
    }

}
