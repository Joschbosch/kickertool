/**
 * 
 */
package zur.koeln.kickertool.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.player.Player;

public class PlayerPoolManagementPane extends StackPane {

    private TournamentController controller;

    public PlayerPoolManagementPane(TournamentController controller) {
        this.controller = controller;

        final Label label = new Label("Player Pool");
        label.setFont(new Font("Arial", 20));

        TableView<Player> table = new TableView<>();
        table.setEditable(true);

        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.<Player>forTableColumn());
        nameCol.setMinWidth(200);
        nameCol.setSortType(TableColumn.SortType.DESCENDING);
        nameCol.setOnEditCommit((CellEditEvent<Player, String> event) -> {
            TablePosition<Player, String> pos = event.getTablePosition();

            String newName = event.getNewValue();
            if (!newName.isEmpty()) {
                int row = pos.getRow();
                Player player = event.getTableView().getItems().get(row);
                player.setName(newName);
                controller.playerEdited();
            }
        });

        TableColumn<Player, String> nicknameCol = new TableColumn<>("Nickname");
        nicknameCol.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        nicknameCol.setCellFactory(TextFieldTableCell.<Player>forTableColumn());
        nicknameCol.setMinWidth(200);
        nicknameCol.setSortable(false);
        nicknameCol.setOnEditCommit((CellEditEvent<Player, String> event) -> {
            TablePosition<Player, String> pos = event.getTablePosition();
            String newNickName = event.getNewValue();

            int row = pos.getRow();
            Player player = event.getTableView().getItems().get(row);

            player.setNickname(newNickName);
            controller.playerEdited();

        });

        table.getColumns().add(nameCol);
        table.getColumns().add(nicknameCol);

        ObservableList<Player> data = FXCollections.observableArrayList();
        data.addAll(controller.getPlayerpool().getPlayers());

        table.setItems(data);

        final TextField nameText = new TextField();
        nameText.setPromptText("Name");
        nameText.setMaxWidth(nameCol.getPrefWidth());
        final TextField nicknameText = new TextField();
        nicknameText.setPromptText("Nickname");
        nicknameText.setMaxWidth(nicknameCol.getPrefWidth());

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!nameText.getText().isEmpty()) {
                    Player newPlayer = new Player(nameText.getText(), nicknameText.getText());
                    data.add(newPlayer);
                    nameText.clear();
                    nicknameText.clear();
                    controller.addPlayer(newPlayer);

                }
            }
        });

        Button removeButton = new Button("Remove Selected Player");
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                table.getSelectionModel().getSelectedItems().forEach(player -> {
                    data.remove(player);
                    controller.removePlayer(player);
                });
            }
        });

        Button back = new Button();
        back.setText("Back to Main Menu");
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                controller.backToMainMenu();
            }

        });

        final HBox hBox = new HBox();
        hBox.getChildren().addAll(nameText, nicknameText, addButton);
        hBox.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hBox, removeButton, back);

        getChildren().add(vbox);
    }
}
