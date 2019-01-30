package zur.koeln.kickertool.deprecated.uifxml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.core.entities.Player;

public class FXMLAddPlayerDialogController {
	
    @Autowired
    private BasicBackendController backendController;
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

    private BasicBackendController getBackendController() {
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
		
        getBackendController().addPlayerToPool(getTxtPlayerName().getText());
        getTxtPlayerName().clear();
        getTxtPlayerName().requestFocus();
        refreshTable();
	}

	
}
