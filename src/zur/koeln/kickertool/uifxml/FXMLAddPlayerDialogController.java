package zur.koeln.kickertool.uifxml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.player.Player;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

@Component
public class FXMLAddPlayerDialogController {
	
    @Autowired
    private BackendController backendController;
	@FXML
	private TableView tblPlayers;
	@FXML
	private TableColumn tblColPlayerName;
	@FXML
	private TextField txtPlayerName;
	@FXML
	private Button btnAddPlayer;

	private final ObservableList<Player> playerData = FXCollections.observableArrayList();
	
	@FXML
	public void initialize() {
		getTblColPlayerName().setCellValueFactory(new PropertyValueFactory<>("name")); //$NON-NLS-1$
		getTblColPlayerName().setCellFactory(TextFieldTableCell.<Player>forTableColumn());
		
		refreshTable();
		
		getTblPlayers().setItems(getPlayerData());
		getTblPlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getBtnAddPlayer().disableProperty().bind(Bindings.greaterThan(1, getTxtPlayerName().textProperty().length()));
	}
	
	private void refreshTable() {
		getPlayerData().clear();
		getPlayerData().addAll(loadPlayerData());
	}

	private List<Player> loadPlayerData() {

		return getBackendController().getPlayerListNotInTournament();
	}

	private BackendController getBackendController() {
		return backendController;
	}

	private TableView getTblPlayers() {
		return tblPlayers;
	}

	private TableColumn getTblColPlayerName() {
		return tblColPlayerName;
	}

	private TextField getTxtPlayerName() {
		return txtPlayerName;
	}

	private Button getBtnAddPlayer() {
		return btnAddPlayer;
	}

	private ObservableList<Player> getPlayerData() {
		return playerData;
	}

	public List<Player> getEnteredResult() {
		
		return getTblPlayers().getSelectionModel().getSelectedItems();
	}

	@FXML public void onBtnAddPlayerClicked() {

		Player newPlayer = new Player(getTxtPlayerName().getText());
		getTxtPlayerName().clear();
        getBackendController().addPlayerToPool(newPlayer);
        refreshTable();
	}

	
}
