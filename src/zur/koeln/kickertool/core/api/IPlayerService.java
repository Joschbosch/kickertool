package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Player;

public interface IPlayerService {
    Player createNewPlayer(String firstName, String lastName);

    Player updatePlayer(Player player);

    void deletePlayer(UUID player);

    List<Player> getAllPlayer();

}
