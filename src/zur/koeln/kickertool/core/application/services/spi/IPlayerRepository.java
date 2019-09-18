package zur.koeln.kickertool.core.application.services.spi;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;

public interface IPlayerRepository
{

    public Player storeOrUpdatePlayer(Player player);

    public void deletePlayer(UUID player);

	public List<Player> getAllPlayer();

    Player getPlayer(UUID playerUID);

    public List<Player> getFreeDummyPlayers();

    public List<Player> getAllDummyPlayers();

}
