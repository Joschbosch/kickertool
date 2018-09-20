package zur.koeln.kickertool.api.player;

import java.util.List;
import java.util.UUID;

public interface PlayerPoolService {

    void loadPlayerPool();

    void savePlayerPool();

    Player createAndAddPlayer(String newPlayerName);

    void removePlayer(Player player);

    void clear();

    Player getPlayerById(UUID playerId);

    int getNoOfDummyPlayerUsed();

    Player createDummyPlayerWithUUID(UUID id);

    Player useNextDummyPlayer();

    UUID removeLastDummy();

    List<Player> getPlayers();

    void changePlayerName(String newName, Player selectedPlayer);

}
