/**
 * 
 */
package zur.koeln.kickertool.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import zur.koeln.kickertool.TournamentController;

public class MainMenuPane extends GridPane {

    private final TournamentController controller;

    public MainMenuPane(TournamentController controller) {
        this.controller = controller;
        // ColumnConstraints cc = new ColumnConstraints(100, 100, Double.MAX_VALUE,
        // Priority.ALWAYS, HPos.CENTER, true);
        // this.getColumnConstraints().addAll(cc, cc);
        //
        // RowConstraints rc = new RowConstraints(20, 20, Double.MAX_VALUE,
        // Priority.ALWAYS, VPos.CENTER, true);
        // this.getRowConstraints().addAll(rc, rc);

        this.setVgap(20);

        TextField newTournamentNameText = new TextField();
        newTournamentNameText.setPromptText("Tournament name");
        Button newTournamentButton = new Button("Create New Tournament");
        newTournamentButton.setDisable(true);
        newTournamentNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            newTournamentButton.setDisable(newValue.isEmpty());
        });
        newTournamentButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.createNewTournament(newTournamentNameText.getText());
            }
        });

        Button btn = new Button();
        btn.setText("Manage Players");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.showPlayerPoolManagement();
            }
        });

        HBox tournamentBox = new HBox(newTournamentNameText, newTournamentButton);
        this.setAlignment(Pos.CENTER);
        this.add(tournamentBox, 0, 0);
        GridPane.setHalignment(btn, HPos.CENTER);
        this.add(btn, 0, 1);

    }

}
