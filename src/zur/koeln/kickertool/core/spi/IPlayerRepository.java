package zur.koeln.kickertool.core.spi;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.aggregates.Player;

public interface IPlayerRepository
{

    public Player storeOrUpdatePlayer(Player player);

    public void deletePlayer(UUID player);

	public Player getPlayerOrNewDummyWithId(UUID playerUID);

	public List<Player> getAllPlayer();

    public Player createNewPlayer(String firstName, String lastName);

    public Player getNewOrFreeDummyPlayer();

    Player getPlayer(UUID playerUID);

}
