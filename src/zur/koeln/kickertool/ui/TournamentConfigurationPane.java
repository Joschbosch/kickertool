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
import zur.koeln.kickertool.base.TournamentControllerService;
import zur.koeln.kickertool.tournament.TournamentConfiguration;
import zur.koeln.kickertool.tournament.TournamentConfigurationKeys;

public class TournamentConfigurationPane extends GridPane {

    private final TournamentControllerService controller;

    public TournamentConfigurationPane(
        TournamentControllerService controller) {
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

        createSpinnerAndLabel(1, "Number of tables", 1, 100, config.getTableCount(), TournamentConfigurationKeys.TABLES);
        createSpinnerAndLabel(2, "Matches to win", 1, 100, config.getMatchesToWin(), TournamentConfigurationKeys.MATCHES_TO_WIN);
        createSpinnerAndLabel(3, "Goals for win", 1, 100, config.getGoalsToWin(), TournamentConfigurationKeys.GOALS_FOR_WIN);
        createSpinnerAndLabel(4, "Points for winner", 0, 100, config.getPointsForWinner(), TournamentConfigurationKeys.POINTS_FOR_WINNER);
        createSpinnerAndLabel(5, "Points for draw", 0, 100, config.getPointsForDraw(), TournamentConfigurationKeys.POINTS_FOR_DRAW);
        createSpinnerAndLabel(6, "Minutes per match", 1, 100, config.getMinutesPerMatch(), TournamentConfigurationKeys.MINUTES_PER_MATCH);
        createSpinnerAndLabel(7, "Random rounds at start", 0, 100, config.getRandomRounds(), TournamentConfigurationKeys.RANDOM_ROUNDS);

        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.showMainMenu();
            }
        });
        Button next = new Button("Select Player");
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.showPlayerSelection();
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
        TournamentConfigurationKeys configKey) {
        Label label = new Label(labelText);
        final Spinner<Integer> spinner = new Spinner<>();
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
