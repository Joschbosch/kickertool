package zur.koeln.kickertool.core.logic.tests.stubs;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.spi.IPlayerRepository;

public class PlayerRepoStub
    implements IPlayerRepository {

	@Override
	public Player storeOrUpdatePlayer(Player player) {
		return null;
	}

	@Override
	public void deletePlayer(UUID player) {
        // no impl
	}

	@Override
	public Player getPlayer(UUID playerUID) {
		return null;
	}

	@Override
	public List<Player> getAllPlayer() {
		return null;
	}

	@Override
	public Player getNewOrFreeDummyPlayer() {
		return null;
	}

    @Override
    public Player createNewPlayer(String firstName, String lastName) {
        // TODO sazu 5.5 2019 Auto-generated method stub
        return null;
    }

}
