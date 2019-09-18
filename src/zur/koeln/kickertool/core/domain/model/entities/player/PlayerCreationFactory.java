package zur.koeln.kickertool.core.domain.model.entities.player;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Named;

import zur.koeln.kickertool.core.domain.model.entities.player.api.IPlayerFactory;

@Named
public class PlayerCreationFactory
    implements IPlayerFactory {

    @Override
    public Player createNewPlayer(String firstName, String lastName) {
        Player newPlayer = new Player();
        newPlayer.changeName(firstName, lastName);
        newPlayer.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
        newPlayer.setUid(UUID.randomUUID());
        newPlayer.setPlayedTournaments(new ArrayList<>());
        return newPlayer;
    }
    @Override
    public Player createNewDummyPlayer(int dummyPlayerNumber) {
        Player newDummy = new Player();
        newDummy.changeName("Dummy", "Player " + dummyPlayerNumber);
        newDummy.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
        newDummy.setUid(UUID.randomUUID());
        newDummy.setPlayedTournaments(new ArrayList<>());
        newDummy.setDummy(true);
        return newDummy;
    }
}
