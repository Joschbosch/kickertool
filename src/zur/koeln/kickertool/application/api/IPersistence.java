package zur.koeln.kickertool.application.api;

import zur.koeln.kickertool.core.entities.Player;

public interface IPersistence {

    void insertPlayer(Player player);

    void updatePlayer(Player player);

    void deletePlayer(Player player);

}
