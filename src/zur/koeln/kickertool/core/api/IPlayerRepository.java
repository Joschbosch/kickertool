package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.entities.Player;

public interface IPlayerRepository
{

	public void addPlayer(Player player);
	
	public void updatePlayer(Player player);
	
	public void deletePlayer(Player player);
	
	public Player getPlayer(UUID playerUID);
	
	public List<Player> getAllPlayer();

}
