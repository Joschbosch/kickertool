package zur.koeln.kickertool.core.domain.model.entities.player.api;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;

public interface IPlayerFactory {

    Player createNewPlayer(String firstName, String lastName);

    Player createNewDummyPlayer(int dummyPlayerNumber);

}
