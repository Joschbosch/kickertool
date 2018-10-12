package zur.koeln.kickertool.uifxml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;
import zur.koeln.kickertool.uifxml.vm.PlayerPoolManagementViewModel;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLPlayerPoolManagementController {

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
	
	@Autowired
	private PlayerPoolManagementViewModel vm;
	@Autowired
	private FXMLGUIservice guiService;
	
	@FXML
	public void initialize() {
		
		getTblColName().setCellValueFactory(new PropertyValueFactory<>("name")); //$NON-NLS-1$
		getTblColName().setCellFactory(TextFieldTableCell.<Player>forTableColumn());
		
		getTblPlayers().setItems(getVm().getPlayers());
		getTblPlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getBtnAddPlayer().disableProperty().bind(getVm().getTxtPlayerNameProperty().isEmpty());
		getTxtPlayerName().textProperty().bindBidirectional(getVm().getTxtPlayerNameProperty());
		
		getVm().loadPlayers();
	}
	
	@FXML
	public void onBtnAddPlayerClicked(ActionEvent event) {
		getVm().createNewPlayer();
		getTxtPlayerName().requestFocus();
	}
	
	@FXML
	public void onBackClicked(ActionEvent event) {
		getGuiService().switchToScene(FXMLGUI.MAIN_MENU);
	}
	
	@FXML
	public void onPlayerDeleteClicked(ActionEvent event) {
		getVm().deletePlayer(getTblPlayers().getSelectionModel().getSelectedItems());
	}

	@FXML 
	public void onPlayerNameChanged(CellEditEvent<Player, String> event) {
		getVm().changePlayerName(event.getNewValue(), getTblPlayers().getSelectionModel().getSelectedItem());
	}

	public List<Player> getSelectedPlayer() {
		
		return getTblPlayers().getSelectionModel().getSelectedItems();
	}
	
	public void loadPlayersNotInTournament() {
		getVm().loadPlayersNotInTournament();
	}
	
}
