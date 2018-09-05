/**
 * 
 */
package zur.koeln.kickertool.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.player.Player;

public class PlayerAssignmentPane extends GridPane {

    public PlayerAssignmentPane(
        BackendController controller) {
        this.setAlignment(Pos.CENTER);

        this.setHgap(5);
        this.setVgap(15);

        TableView<Player> playerPoolTable = addPlayerTable(controller, "Player Pool", 0, 0);
        ObservableList<Player> playerPoolData = FXCollections.observableArrayList();
        playerPoolData.addAll(controller.getPlayerpool().getPlayers());
        playerPoolTable.setItems(playerPoolData);

        Button right = new Button("->");
        Button left = new Button("<-");

        VBox box = new VBox(right, left);
        box.setSpacing(4);
        box.setAlignment(Pos.CENTER);
        GridPane.setValignment(box, VPos.BOTTOM);
        GridPane.setHalignment(box, HPos.CENTER);
        this.add(box, 1, 1);

        TableView<Player> participantTable = addPlayerTable(controller, "Participant List", 2, 0);
        ObservableList<Player> dataParticipantTable = FXCollections.observableArrayList();
        participantTable.setItems(dataParticipantTable);
        final Button startButton = addControlButtons(controller);
        startButton.setDisable(true);
        right.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!playerPoolTable.getSelectionModel().getSelectedItems().isEmpty()) {
                    for (Player p : playerPoolTable.getSelectionModel().getSelectedItems()) {
                        controller.addParticipantToTournament(p);

                    }
                    dataParticipantTable.clear();
                    dataParticipantTable.addAll(controller.getParticipantList());
                }
                startButton.setDisable(dataParticipantTable.size() < 2);
            }
        });

        left.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!participantTable.getSelectionModel().getSelectedItems().isEmpty()) {
                    for (Player p : participantTable.getSelectionModel().getSelectedItems()) {
                        controller.removeParticipantFromTournament(p);

                    }
                    dataParticipantTable.clear();
                    dataParticipantTable.addAll(controller.getParticipantList());
                }
                startButton.setDisable(dataParticipantTable.size() < 2);

            }
        });

    }

    /**
     * @param controller
     */
    private TableView<Player> addPlayerTable(BackendController controller, String labelText, int gridCol,
            int gridRow) {
        final Label label = new Label(labelText);
        label.setFont(new Font("Arial", 20));

        TableView<Player> table = new TableView<>();

        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.<Player>forTableColumn());
        nameCol.setMinWidth(150);
        nameCol.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<Player, String> nicknameCol = new TableColumn<>("Nickname");
        nicknameCol.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        nicknameCol.setCellFactory(TextFieldTableCell.<Player>forTableColumn());
        nicknameCol.setMinWidth(150);
        nicknameCol.setSortable(false);

        table.getColumns().add(nameCol);
        table.getColumns().add(nicknameCol);

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.add(label, gridCol, gridRow);
        this.add(table, gridCol, gridRow + 1);
        return table;
    }

    /**
     * @param controller
     */
    private Button addControlButtons(BackendController controller) {
        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.showTournamentConfig();
            }
        });
        Button next = new Button("Start Tournament");
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.startTournament();
            }
        });

        this.add(back, 0, 2);
        this.add(next, 2, 2);
        GridPane.setHalignment(next, HPos.RIGHT);

        return next;
    }
}
