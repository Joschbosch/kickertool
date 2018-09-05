package zur.koeln.kickertool.uifxml;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.uifxml.cells.PlayerListCell;

@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerSelectionController {
	
	@FXML
	private Button btnBack;
	@FXML
	private Button btnStartTournament;
	@FXML
	private ListView lstPlayers;
	@FXML
	private ListView lstPlayersForTournament;
	@FXML
	private Button btnAddPlayer;
	@FXML
	private Button btnRemovePlayer;

	private ObservableList<Player> playerData = FXCollections.observableArrayList();
	private ObservableList<Player> selectedPlayerData = FXCollections.observableArrayList();
	
	private static final DataFormat DATAFORMAT = new DataFormat("PLAYER");
	
	@FXML
	public void initialize() {
		
		getPlayerData().addAll(loadPlayerData());
		
		getLstPlayers().setItems(getPlayerData());
		getLstPlayers().setCellFactory(param -> new PlayerListCell());
		getLstPlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getLstPlayersForTournament().setItems(getSelectedPlayerData());
		getLstPlayersForTournament().setCellFactory(param -> new PlayerListCell());
		getLstPlayersForTournament().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setupDragAndDropFeatures();
	}
	
	private void setupDragAndDropFeatures() {
		
		getLstPlayers().setOnDragDetected(event -> onDragDetected(getLstPlayers(), event));
		
		getLstPlayers().setOnDragOver(this::onDragOver);
		
		getLstPlayers().setOnDragDropped(event -> onDragDropped(getLstPlayers(), getLstPlayersForTournament(), event));
		
		getLstPlayersForTournament().setOnDragDetected(event -> onDragDetected(getLstPlayersForTournament(), event));
		
		getLstPlayersForTournament().setOnDragOver(this::onDragOver);
		
		getLstPlayersForTournament().setOnDragDropped(event -> onDragDropped(getLstPlayersForTournament(), getLstPlayers(), event));
	}

	private void onDragDropped(ListView target, ListView source, DragEvent event) {
		
		Dragboard db = event.getDragboard();
		
		List<Player> droppedPlayers = (List<Player>) db.getContent(DATAFORMAT);
		
		target.getItems().addAll(droppedPlayers);
		source.getItems().removeAll(droppedPlayers);
		
		event.consume();
		
	}

	private void onDragDetected(ListView list, MouseEvent event) {
		
		list.getSelectionModel().getSelectedItems();
		
		Dragboard dragboard = list.startDragAndDrop(TransferMode.MOVE);
		ClipboardContent content = new ClipboardContent();
		content.put(DATAFORMAT, new ArrayList<>(list.getSelectionModel().getSelectedItems()));
		
		dragboard.setContent(content);
		event.consume();
		
	}

	private void onDragOver(DragEvent event) {
		
		if (event.getGestureSource() != event.getTarget()) {
			event.acceptTransferModes(TransferMode.MOVE);
		}
		event.consume();
		
	}
	
	private List<Player> loadPlayerData() {
		
		return TournamentController.getInstance().getPlayerpool().getPlayers();
	}
	
	@FXML
	public void onBtnBackClicked(ActionEvent event) {
		TournamentController.getInstance().backToTournamentConfig();
	}
	
	@FXML
	public void onBtnStartTournamentClicked(ActionEvent event) {
		
		transferSelectedPlayersToTournamentConfig();
		TournamentController.getInstance().startTournament();
		
	}

	private void transferSelectedPlayersToTournamentConfig() {
		getSelectedPlayerData().forEach(ePlayer -> TournamentController.getInstance().addParticipantToTournament(ePlayer));
	}
	
	@FXML
	public void onBtnAddPlayerClicked(ActionEvent event) {
		
		getSelectedPlayerData().addAll(getLstPlayers().getSelectionModel().getSelectedItems());
		getPlayerData().removeAll(getLstPlayers().getSelectionModel().getSelectedItems());
		
	}
	// Event Listener on Button[#btnRemovePlayer].onAction
	@FXML
	public void onBtnRemovePlayerClicked(ActionEvent event) {
		
		getPlayerData().addAll(getLstPlayersForTournament().getSelectionModel().getSelectedItems());
		getSelectedPlayerData().removeAll(getLstPlayersForTournament().getSelectionModel().getSelectedItems());
		
	}
}
