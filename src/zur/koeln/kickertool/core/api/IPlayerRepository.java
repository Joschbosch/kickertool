package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.entities.Player;

public interface IPlayerRepository
{

    void loadPlayerPool();

    void savePlayerPool();

    Player createAndAddPlayer(String newPlayerName);

    void removePlayer(Player player);

    void clear();

    Player getPlayerOrDummyById(UUID playerId);

    int getNoOfDummyPlayerUsed();

    Player createDummyPlayerWithUUID(UUID id);

    Player useNextDummyPlayer();

    UUID removeLastDummy();

    List<Player> getPlayers();

    void changePlayerName(String newName, Player selectedPlayer);

}
