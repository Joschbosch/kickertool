package zur.koeln.kickertool.application.repos;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zur.koeln.kickertool.application.api.IPersistence;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.entities.Player;

@Repository
public class PlayerRepository
    implements IPlayerRepository {

    @Autowired
    private IPersistence persistence;

    private final Map<UUID, Player> players = new HashMap<>();

    @Override
    public void addPlayer(Player player) {

        players.put(player.getUid(), player);
        persistence.insertPlayer(player);

    }

    @Override
    public void updatePlayer(Player player) {
        if (players.containsKey(player.getUid())) {
            Player storedPlayer = players.get(player.getUid());
            storedPlayer.setFirstName(player.getFirstName());
            storedPlayer.setSurname(player.getSurname());
            persistence.updatePlayer(player);
        }

    }

    @Override
    public void deletePlayer(Player player) {
        if (players.containsKey(player.getUid())) {
            players.remove(player.getUid());
            persistence.deletePlayer(player);
        }


    }

    @Override
    public Player getPlayer(UUID playerUID) {
        return players.getOrDefault(playerUID, null);
    }

    @Override
    public List<Player> getAllPlayer() {
        return new ArrayList<>(players.values());
    }

}
