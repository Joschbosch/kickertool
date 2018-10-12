package zur.koeln.kickertool.uifxml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
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
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.uifxml.cells.PlayerListCell;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;
import zur.koeln.kickertool.uifxml.vm.PlayerSelectionViewModel;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerSelectionController {

	@FXML
	private Button btnBack;
	@FXML
	private Button btnStartTournament;
	@FXML
	private ListView lstSelectablePlayers;
	@FXML
	private ListView lstPlayersForTournament;
	@FXML
	private Button btnAddPlayer;
	@FXML
	private Button btnRemovePlayer;

	@Autowired
	private PlayerSelectionViewModel vm;
	@Autowired
	private FXMLGUIservice guiService;
	
	private static final DataFormat DATAFORMAT = new DataFormat("PLAYER"); //$NON-NLS-1$
	
	@FXML
	public void initialize() {
		
		getLstSelectablePlayers().setItems(getVm().getSelectablePlayers());
		getLstSelectablePlayers().setCellFactory(param -> new PlayerListCell());
		getLstSelectablePlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getLstPlayersForTournament().setItems(getVm().getPlayersForTournament());
		getLstPlayersForTournament().setCellFactory(param -> new PlayerListCell());
		getLstPlayersForTournament().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		getBtnStartTournament().disableProperty().bind(Bindings.size(getVm().getPlayersForTournament()).lessThan(PlayerSelectionViewModel.MINIMUM_PLAYERS));
		getBtnAddPlayer().disableProperty().bind(getLstSelectablePlayers().getSelectionModel().selectedItemProperty().isNull());
		getBtnRemovePlayer().disableProperty().bind(getLstPlayersForTournament().getSelectionModel().selectedItemProperty().isNull());
		
		setupDragAndDropFeatures();
		
		getVm().loadPlayers();
	}
	
	private void setupDragAndDropFeatures() {
		
		getLstSelectablePlayers().setOnDragDetected(event -> onDragDetected(getLstSelectablePlayers(), event));
		
		getLstSelectablePlayers().setOnDragOver(event -> onDragOver(event, getLstSelectablePlayers()));
		
		getLstSelectablePlayers().setOnDragDropped(event -> onDragDropped(getLstSelectablePlayers(), getLstPlayersForTournament(), event));
		
		getLstPlayersForTournament().setOnDragDetected(event -> onDragDetected(getLstPlayersForTournament(), event));
		
		getLstPlayersForTournament().setOnDragOver(event -> onDragOver(event, getLstPlayersForTournament()));
		
		getLstPlayersForTournament().setOnDragDropped(event -> onDragDropped(getLstPlayersForTournament(), getLstSelectablePlayers(), event));
	}

	private void onDragDropped(ListView target, ListView source, DragEvent event) {
		
		Dragboard db = event.getDragboard();
		
		List<Player> droppedPlayers = (List<Player>) db.getContent(DATAFORMAT);
		
		getVm().transferPlayersFromTo(droppedPlayers, source, target);
		
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

	private void onDragOver(DragEvent event, ListView sourceListView) {
		
		if (event.getGestureSource() != sourceListView) {
			event.acceptTransferModes(TransferMode.MOVE);
		}
		
		event.consume();
		
	}

	@FXML
	public void onBtnBackClicked(ActionEvent event) {
		getGuiService().switchToScene(FXMLGUI.TOURNAMENT_CONFIGURATION);
	}
	
	@FXML
	public void onBtnStartTournamentClicked(ActionEvent event) {
		
		getVm().transferSelectedPlayersToTournament();
		getVm().startTournament();
		getGuiService().switchToScene(FXMLGUI.TOURMANENT);
		
	}

	@FXML
	public void onBtnAddPlayerClicked(ActionEvent event) {
		getVm().addPlayersForTournament(getLstSelectablePlayers().getSelectionModel().getSelectedItems());
		getLstSelectablePlayers().getSelectionModel().clearSelection();
		
	}
	
	@FXML
	public void onBtnRemovePlayerClicked(ActionEvent event) {
		getVm().removePlayersFromTourmanent(getLstPlayersForTournament().getSelectionModel().getSelectedItems());
		getLstPlayersForTournament().getSelectionModel().clearSelection();
		
	}
	
}
