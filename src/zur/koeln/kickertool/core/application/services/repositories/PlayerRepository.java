package zur.koeln.kickertool.core.application.services.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.application.services.usecases.spi.IPlayerRepository;
import zur.koeln.kickertool.core.application.spi.IPlayerPersistence;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;

@Named
public class PlayerRepository
    implements IPlayerRepository {

    private final IPlayerPersistence playerPersistence;

    private final List<Player> dummyPlayer;

    @Inject
    public PlayerRepository(
        IPlayerPersistence playerPersistence) {
        this.playerPersistence = playerPersistence;
        dummyPlayer = new ArrayList<>();
    }

    @Override
    public Player getPlayer(UUID playerUID) {
        for (Player dummy : dummyPlayer) {
            if (dummy.getUid().equals(playerUID)) {
                return dummy;
            }
        }
        return playerPersistence.findPlayerByUID(playerUID);
    }

    @Override
    public Player storeOrUpdatePlayer(Player player) {
        if (player.isDummy()) {
            return handleStoreOrUpdateDummy(player);
        }
        Player oldPlayer = this.getPlayer(player.getUid());

        if (oldPlayer == null) {
            playerPersistence.insert(player);
        } else {
            playerPersistence.update(player);
        }
        return player;
    }


    @Override
    public void deletePlayer(UUID playerUid) {
        Player playerToDelete = playerPersistence.findPlayerByUID(playerUid);
        if (playerToDelete != null) {
            playerPersistence.removePlayer(playerToDelete);
        }

    }

    @Override
    public List<Player> getFreeDummyPlayers() {
        List<Player> freeDummyPlayer = new ArrayList<>();
        for (Player dummy : dummyPlayer) {
            if (dummy.getStatus() == PlayerStatus.NOT_IN_TOURNAMENT) {
                freeDummyPlayer.add(dummy);
            }
        }
        return freeDummyPlayer;
    }

    @Override
    public List<Player> getAllDummyPlayers() {

        return dummyPlayer;
    }


    @Override
    public List<Player> getAllPlayer() {
        return playerPersistence.getAllPlayer();
    }
    private Player handleStoreOrUpdateDummy(Player player) {
        return player;
    }

}
