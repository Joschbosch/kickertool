package zur.koeln.kickertool.uifxml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.base.GUIUpdate;
import zur.koeln.kickertool.player.Player;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLPlayerPoolManagementController implements GUIUpdate {

    @Autowired
    private BackendController backendController;
	@FXML
	private TableView<Player> tblPlayers;
	@FXML
	private TableColumn<Player,String> tblColName;
	@FXML
	private TextField txtPlayerName;
	@FXML
	private Button btnAddPlayer;
	@FXML
	private Button btnBack;
	
	private final ObservableList<Player> playerData = FXCollections.observableArrayList();

	@FXML
	public void initialize() {

		getTblColName().setCellValueFactory(new PropertyValueFactory<>("name"));
		getTblColName().setCellFactory(TextFieldTableCell.<Player>forTableColumn());
		
		getPlayerData().addAll(loadPlayerData());
		
		getTblPlayers().setItems(getPlayerData());
		getTblPlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getBtnAddPlayer().disableProperty().bind(Bindings.greaterThan(1, getTxtPlayerName().textProperty().length()));
	
	}
	
	private List<Player> loadPlayerData() {
		
        return backendController.getPlayerpool().getPlayers();
	}
	
	// Event Listener on Button[#btnAddPlayer].onAction
	@FXML
	public void onBtnAddPlayerClicked(ActionEvent event) {
		
		Player newPlayer = new Player(getTxtPlayerName().getText());
		getPlayerData().add(newPlayer);
		getTxtPlayerName().clear();
        backendController.addPlayer(newPlayer);
		
	}
	// Event Listener on Button[#btnBack].onAction
	@FXML
	public void onBackClicked(ActionEvent event) {
        backendController.showMainMenu();
	}
	
	@FXML
	public void onPlayerDeleteClicked(ActionEvent event) {
		
		List<Player> selectedPlayers = getTblPlayers().getSelectionModel().getSelectedItems();
        selectedPlayers.forEach(ePlayer -> backendController.removePlayer(ePlayer));
		getPlayerData().removeAll(selectedPlayers);
		
	}

	@FXML 
	public void onPlayerNameChanged(CellEditEvent<Player, String> event) {
			
		String newName = event.getNewValue();
		
        if (!newName.isEmpty()) {
        	Player selectedPlayer = getTblPlayers().getSelectionModel().getSelectedItem();
            selectedPlayer.setName(newName);
            backendController.savePlayerPool();
        }
		
	}
	
	@Override
	public void update() {
		//
	}

}
