package zur.koeln.kickertool.application.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.application.repositories.persistence.IPlayerPersistence;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.spi.IPlayerRepository;

@Named
public class PlayerRepository
    implements IPlayerRepository {

    private final IPlayerPersistence playerPersistence;

    @Inject
    public PlayerRepository(
        IPlayerPersistence playerPersistence) {
        this.playerPersistence = playerPersistence;
    }

    @Override
    public Player storeOrUpdatePlayer(Player player) {
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

        playerPersistence.removePlayer(getPlayer(playerUid));
    }

    @Override
    public Player getPlayer(UUID playerUID) {
        return playerPersistence.findPlayerByUID(playerUID);
    }

    @Override
    public List<Player> getAllPlayer() {
        return playerPersistence.getAllPlayer();
    }

    @Override
    public Player createNewPlayer(String firstName, String lastName) {
        Player newPlayer = new Player();
        newPlayer.setDummy(false);
        newPlayer.setFirstName(firstName);
        newPlayer.setLastName(lastName);
        newPlayer.setPlayedTournaments(new ArrayList<>());
        newPlayer.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
        newPlayer.setUid(UUID.randomUUID());
        playerPersistence.insert(newPlayer);
        return newPlayer;
    }

    @Override
    public Player getNewOrFreeDummyPlayer() {
        Player dummy = createNewPlayer("Dummy", "i");
        dummy.setDummy(true);
        return dummy;
    }

}
