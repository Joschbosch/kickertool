package zur.koeln.kickertool.uifxml;

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.player.Player;

@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerPoolManagementController{
	
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
	
	private ObservableList<Player> playerData = FXCollections.observableArrayList();

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
		
		return TournamentController.getInstance().getPlayerpool().getPlayers();
	}
	
	// Event Listener on Button[#btnAddPlayer].onAction
	@FXML
	public void OnBtnAddPlayerClicked(ActionEvent event) {
		
		Player newPlayer = new Player(getTxtPlayerName().getText());
		getPlayerData().add(newPlayer);
		getTxtPlayerName().clear();
        TournamentController.getInstance().addPlayer(newPlayer);
		
	}
	// Event Listener on Button[#btnBack].onAction
	@FXML
	public void OnBackClicked(ActionEvent event) {
		TournamentController.getInstance().backToMainMenu();
	}
	
	@FXML
	public void OnPlayerDeleteClicked(ActionEvent event) {
		
		List<Player> selectedPlayers = getTblPlayers().getSelectionModel().getSelectedItems();
		getPlayerData().removeAll(selectedPlayers);
		
	}
}
