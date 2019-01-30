package zur.koeln.kickertool.core.api;

import zur.koeln.kickertool.core.entities.Player;

public interface IPlayerService {
    Player createNewPlayer(String firstName, String lastName);

    Player updatePlayer(Player player);

    void deletePlayer(Player player);

}
