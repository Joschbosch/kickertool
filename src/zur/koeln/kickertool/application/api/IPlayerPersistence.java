package zur.koeln.kickertool.application.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.aggregates.Player;

public interface IPlayerPersistence {

    void insert(Player player);

    void update(Player player);

    Player findPlayerByUID(UUID playerUID);

    List<Player> getAllPlayer();

    void removePlayer(Player player);

}
