package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Player;

public interface IDummyPlayerRepository {

	
	Player getDummyPlayer(UUID uid);
	
	List<Player> getAllDummyPlayer();
}
