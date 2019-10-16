package zur.koeln.kickertool.core.application.spi.persistence;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;

public interface IPlayerPersistence {

    void insert(Player player);

    void update(Player player);

    Player findPlayerByUID(UUID playerUID);

    List<Player> getAllPlayer();

    void removePlayer(Player player);

}
