package zur.koeln.kickertool.uifxml.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.player.Player;

@Component
public class PlayerPoolManagementViewModel {

	@Autowired
	private BackendController backendController;
	
	private final StringProperty txtPlayerNameProperty = new SimpleStringProperty();
	private final ObservableList<Player> players = FXCollections.observableArrayList();

	public void loadPlayers() {
		getPlayers().clear();
		getPlayers().addAll(getBackendController().getPlayer());
	}
	
	private BackendController getBackendController() {
		return backendController;
	}

	public StringProperty getTxtPlayerNameProperty() {
		return txtPlayerNameProperty;
	}
	
	public ObservableList<Player> getPlayers() {
		return players;
	}

	public void createNewPlayer() {
		Player newPlayer = getBackendController().addPlayerToPool(getTxtPlayerNameProperty().get());
		getPlayers().add(newPlayer);
		getTxtPlayerNameProperty().set(null);
	}

	public void deletePlayer(ObservableList<Player> selectedPlayer) {
		selectedPlayer.forEach(ePlayer -> {
			getBackendController().removePlayerFromPool(ePlayer);
			getPlayers().remove(ePlayer);
		});
	}

	public void changePlayerName(String newName, Player selectedPlayer) {
		if (!newName.isEmpty()) {
			getBackendController().changePlayerName(newName, selectedPlayer);
		}
	}

	public void loadPlayersNotInTournament() {
		getPlayers().clear();
		getPlayers().addAll(getBackendController().getPlayerListNotInTournament());
	}
	
}
