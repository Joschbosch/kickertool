package zur.koeln.kickertool.core.app.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.bl.model.player.PlayerStatus;
import zur.koeln.kickertool.core.ports.infrastructure.IPlayerPersistence;

@Named
public class PlayerRepository{

	private final IPlayerPersistence playerPersistence;

	private final List<Player> dummyPlayer;

	@Inject
	public PlayerRepository(IPlayerPersistence playerPersistence) {
		this.playerPersistence = playerPersistence;
		dummyPlayer = new ArrayList<>();
	}

	public Player getPlayer(UUID playerUID) {
		for (Player dummy : dummyPlayer) {
			if (dummy.getUid().equals(playerUID)) {
				return dummy;
			}
		}
		return playerPersistence.findPlayerByUID(playerUID);
	}

	public Player storeOrUpdatePlayer(Player player) {
		if (player.isDummy()) {
			return handleStoreOrUpdateDummy(player);
		}
		Player oldPlayer = this.getPlayer(player.getUid());

		if (!player.isDummy()) {
			if (oldPlayer == null) {
				playerPersistence.insert(player);
			} else {
				playerPersistence.update(player);
			}
		}
		return player;
	}

	public void deletePlayer(UUID playerUid) {
		Player playerToDelete = playerPersistence.findPlayerByUID(playerUid);
		if (playerToDelete != null) {
			playerPersistence.removePlayer(playerToDelete);
		}

	}

	public List<Player> getFreeDummyPlayers() {
		List<Player> freeDummyPlayer = new ArrayList<>();
		for (Player dummy : dummyPlayer) {
			if (dummy.getStatus() == PlayerStatus.NOT_IN_TOURNAMENT) {
				freeDummyPlayer.add(dummy);
			}
		}
		return freeDummyPlayer;
	}
	public List<Player> getAllDummyPlayers() {

		return dummyPlayer;
	}

	public List<Player> getAllPlayer() {
		return playerPersistence.getAllPlayer();
	}

	private Player handleStoreOrUpdateDummy(Player player) {
		dummyPlayer.add(player);
		return player;
	}

}
