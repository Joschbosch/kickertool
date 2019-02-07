package zur.koeln.kickertool.application.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zur.koeln.kickertool.application.api.IPlayerPersistence;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.model.Player;

@Repository
public class PlayerRepository
    implements IPlayerRepository {

    @Autowired
    private IPlayerPersistence playerPersistence;

    @Override
    public Player createOrUpdatePlayer(Player player) {
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

}
