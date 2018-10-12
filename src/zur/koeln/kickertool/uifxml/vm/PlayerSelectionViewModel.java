package zur.koeln.kickertool.uifxml.vm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.player.Player;

@Component
public class PlayerSelectionViewModel {

	@Autowired
	private BackendController backendController;
	
	private final ObservableList<Player> selectablePlayers = FXCollections.observableArrayList();
	private final ObservableList<Player> playersForTournament = FXCollections.observableArrayList();
	
	public static final int MINIMUM_PLAYERS = 4;
	
	public void loadPlayers() {
		getSelectablePlayers().clear();
		getPlayersForTournament().clear();
		getSelectablePlayers().addAll(getBackendController().getPlayer());
	}
	
	public ObservableList<Player> getSelectablePlayers() {
		return selectablePlayers;
	}
	public ObservableList<Player> getPlayersForTournament() {
		return playersForTournament;
	}
	private BackendController getBackendController() {
		return backendController;
	}

	public void transferSelectedPlayersToTournament() {
		getPlayersForTournament().forEach(ePlayer -> getBackendController().addParticipantToTournament(ePlayer));
	}

	public void addPlayersForTournament(ObservableList<Player> selectedPlayer) {
		getPlayersForTournament().addAll(selectedPlayer);
		getSelectablePlayers().removeAll(selectedPlayer);
	}

	public void removePlayersFromTourmanent(ObservableList<Player> selectedPlayer) {
		getSelectablePlayers().addAll(selectedPlayer);
		getPlayersForTournament().removeAll(selectedPlayer);
	}

	public void transferPlayersFromTo(List<Player> droppedPlayers, ListView source, ListView target) {
		target.getItems().addAll(droppedPlayers);
		source.getItems().removeAll(droppedPlayers);
	}

	public void startTournament() {
		getBackendController().getCurrentTournament().startTournament();
	}

}
