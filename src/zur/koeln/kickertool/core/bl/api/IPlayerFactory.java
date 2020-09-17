package zur.koeln.kickertool.core.bl.api;

import zur.koeln.kickertool.core.bl.model.player.Player;

public interface IPlayerFactory {

    Player createNewPlayer(String firstName, String lastName);

    Player createNewDummyPlayer(int dummyPlayerNumber);

}
