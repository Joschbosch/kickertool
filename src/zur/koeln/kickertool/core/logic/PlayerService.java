package zur.koeln.kickertool.core.logic;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.IDummyPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.PlayerStatistics;

@Service
public class PlayerService
    implements IPlayerService {

	@Autowired
    private IPlayerRepository playerRepo;
	
	@Autowired
	private IDummyPlayerRepository dummyPlayerRepo;
    

    public Player createNewPlayer(String firstName, String lastName) {
        Player newPlayer = new Player(UUID.randomUUID(), firstName, lastName, false);
        newPlayer.setStatistics(new PlayerStatistics(newPlayer));

    	playerRepo.createOrUpdatePlayer(newPlayer);
    	return newPlayer;
    }
    
    @Override
    public Player updatePlayerName(UUID id, String newFirstName, String newLastName) {
        Player player = playerRepo.getPlayer(id);
        player.setFirstName(newFirstName);
        player.setLastName(newLastName);
        return playerRepo.createOrUpdatePlayer(player);
    }

    @Override
    public void deletePlayer(UUID player) {
        playerRepo.deletePlayer(player);
    }
    @Override
    public List<Player> getAllPlayer() {
        return playerRepo.getAllPlayer();
    }
    @Override
    public Map<UUID, PlayerStatistics> getAllPlayerStatistics() {
        return playerRepo.getAllPlayer().stream().collect(Collectors.toMap(Player::getUid, Player::getStatistics));
    }

    @Override
    public Player getPlayerById(UUID playerId) {
        Player returningPlayer = playerRepo.getPlayer(playerId);
        if (returningPlayer == null) {
            dummyPlayerRepo.getDummyPlayer(playerId);
        }
        return returningPlayer;
    }

    @Override
    public Player getNextOrNewDummyPlayer() {
        return dummyPlayerRepo.getNewOrFreeDummyPlayer();
    }
}
