package zur.koeln.kickertool.core.ports.infrastructure;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.bl.model.player.Player;

public interface IPlayerPersistence {

    void insert(Player player);

    void update(Player player);

    Player findPlayerByUID(UUID playerUID);

    List<Player> getAllPlayer();

    void removePlayer(Player player);

}
