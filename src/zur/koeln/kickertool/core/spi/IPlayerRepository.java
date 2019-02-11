package zur.koeln.kickertool.core.spi;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Player;

public interface IPlayerRepository
{

    public Player createOrUpdatePlayer(Player player);
	
    public void deletePlayer(UUID player);
	
	public Player getPlayer(UUID playerUID);
	
	public List<Player> getAllPlayer();

}
