package zur.koeln.kickertool.uifxml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.*;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.uifxml.cells.PlayerListCell;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerSelectionController implements UpdateableUIComponent {
    @Autowired
    private BackendController backendController;
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

	private final ObservableList<Player> playerData = FXCollections.observableArrayList();
	private final ObservableList<Player> selectedPlayerData = FXCollections.observableArrayList();
	
	private static final DataFormat DATAFORMAT = new DataFormat("PLAYER"); //$NON-NLS-1$
	
	@FXML
	public void initialize() {
		
		getPlayerData().addAll(loadPlayerData());
		
		getLstPlayers().setItems(getPlayerData());
		getLstPlayers().setCellFactory(param -> new PlayerListCell());
		getLstPlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getLstPlayersForTournament().setItems(getSelectedPlayerData());
		getLstPlayersForTournament().setCellFactory(param -> new PlayerListCell());
		getLstPlayersForTournament().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getBtnStartTournament().disableProperty().bind(Bindings.size(getLstPlayersForTournament().getItems()).isEqualTo(0));
		
		getBtnAddPlayer().disableProperty().bind(getLstPlayers().getSelectionModel().selectedItemProperty().isNull());
		getBtnRemovePlayer().disableProperty().bind(getLstPlayersForTournament().getSelectionModel().selectedItemProperty().isNull());
		
		setupDragAndDropFeatures();
	}
	
	private List<Player> loadPlayerData() {
		
        return backendController.getPlayer();
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
		
		source.getSelectionModel().clearSelection();
		
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

	@FXML
	public void onBtnBackClicked(ActionEvent event) {
        backendController.showMainMenu();
	}
	
	@FXML
	public void onBtnStartTournamentClicked(ActionEvent event) {
		
		transferSelectedPlayersToTournamentConfig();
        backendController.startTournament();
		
	}

	private void transferSelectedPlayersToTournamentConfig() {
        getSelectedPlayerData().forEach(ePlayer -> backendController.addParticipantToTournament(ePlayer));
	}
	
	@FXML
	public void onBtnAddPlayerClicked(ActionEvent event) {
		
		getSelectedPlayerData().addAll(getLstPlayers().getSelectionModel().getSelectedItems());
		getPlayerData().removeAll(getLstPlayers().getSelectionModel().getSelectedItems());
		getLstPlayers().getSelectionModel().clearSelection();
		
	}
	
	@FXML
	public void onBtnRemovePlayerClicked(ActionEvent event) {
		
		getPlayerData().addAll(getLstPlayersForTournament().getSelectionModel().getSelectedItems());
		getSelectedPlayerData().removeAll(getLstPlayersForTournament().getSelectionModel().getSelectedItems());
		getLstPlayersForTournament().getSelectionModel().clearSelection();
		
	}
	
	@Override
	public void update() {
		//
	}
	
}
