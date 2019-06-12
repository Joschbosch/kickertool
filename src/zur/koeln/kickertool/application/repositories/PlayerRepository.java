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

    private final List<Player> dummyPlayer;

    @Inject
    public PlayerRepository(
        IPlayerPersistence playerPersistence) {
        this.playerPersistence = playerPersistence;
        dummyPlayer = new ArrayList<>();
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

        playerPersistence.removePlayer(getPlayerOrNewDummyWithId(playerUid));
    }

    @Override
    public Player getPlayerOrNewDummyWithId(UUID playerUID) {
        for (Player dummy : dummyPlayer) {
            if (dummy.getUid().equals(playerUID)) {
                return dummy;
            }
        }
        Player findPlayerByUID = playerPersistence.findPlayerByUID(playerUID);
        if (findPlayerByUID == null) {
            findPlayerByUID = createDummyWithUID(playerUID);
        }
        return findPlayerByUID;
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
    public List<Player> getAllPlayer() {
        return playerPersistence.getAllPlayer();
    }

    @Override
    public Player createNewPlayer(String firstName, String lastName) {
        Player newPlayer = createNewPlayerOrDummy(firstName, lastName, false);
        playerPersistence.insert(newPlayer);
        return newPlayer;
    }

    private Player createNewPlayerOrDummy(String firstName, String lastName, boolean dummy) {
        Player newPlayer = new Player();
        newPlayer.setDummy(dummy);
        if (firstName != null) {
            newPlayer.setFirstName(firstName);
        } else {
            newPlayer.setFirstName(""); //$NON-NLS-1$
        }
        if (lastName != null) {
            newPlayer.setLastName(lastName);
        } else {
            newPlayer.setLastName(""); //$NON-NLS-1$
        }
        newPlayer.setPlayedTournaments(new ArrayList<>());
        newPlayer.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
        newPlayer.setUid(UUID.randomUUID());
        return newPlayer;
    }

    private Player createDummyWithUID(UUID id) {
        Player newPlayer = new Player();
        newPlayer.setDummy(true);
        newPlayer.setFirstName("Dummy");
        newPlayer.setLastName("Player" + dummyPlayer.size());
        newPlayer.setPlayedTournaments(new ArrayList<>());
        newPlayer.setStatus(PlayerStatus.IN_TOURNAMENT);
        newPlayer.setUid(id);
        dummyPlayer.add(newPlayer);
        return newPlayer;
    }

    @Override
    public Player getNewOrFreeDummyPlayer() {
        for (Player dummy : dummyPlayer) {
            if (dummy.getStatus() == PlayerStatus.NOT_IN_TOURNAMENT) {
                dummy.setStatus(PlayerStatus.IN_TOURNAMENT);
                return dummy;
            }
        }
        Player dummy = createNewPlayerOrDummy("Dummy", "Player " + dummyPlayer.size(), true);
        dummyPlayer.add(dummy);
        return dummy;
    }

}
