package zur.koeln.kickertool.base;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.player.Player;

public interface PlayerPoolService {

    void loadPlayerPool();

    void savePlayerPool();

    void addPlayer(Player player);

    void removePlayer(Player player);

    void clear();

    Player getPlayerById(UUID playerId);

    int getNoOfDummyPlayerUsed();

    void createDummyPlayerWithUUID(UUID id);

    UUID useNextDummyPlayer();

    UUID removeLastDummy();

    List<Player> getPlayers();

}
