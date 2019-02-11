package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.PlayerStatistics;

public interface IPlayerService {
    Player createNewPlayer(String firstName, String lastName);

    Player updatePlayerName(UUID id, String newFirstName, String newLastName);

    void deletePlayer(UUID player);

    List<Player> getAllPlayer();

    Map<UUID, PlayerStatistics> getAllPlayerStatistics();

    Player getPlayerById(UUID participant);

    Player getNextOrNewDummyPlayer();

}
