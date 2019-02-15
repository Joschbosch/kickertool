package zur.koeln.kickertool.core.logic.tests.stubs;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.spi.IDummyPlayerRepository;
import zur.koeln.kickertool.core.spi.IPlayerRepository;

public class PlayerAndDummyRepoStub implements IPlayerRepository, IDummyPlayerRepository{

	@Override
	public Player createOrUpdatePlayer(Player player) {
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
	public Player getDummyPlayer(UUID uid) {
		return null;
	}

	@Override
	public List<Player> getAllDummyPlayer() {
		return null;
	}

	@Override
	public Player getNewOrFreeDummyPlayer() {
		return null;
	}

}
