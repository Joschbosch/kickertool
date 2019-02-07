package zur.koeln.kickertool.core.logic;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.IDummyPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.model.Player;

@Service
public class PlayerService
    implements IPlayerService {

	@Autowired
    private IPlayerRepository playerRepo;
	
	@Autowired
	private IDummyPlayerRepository dummyPlayerRepo;
    

    public Player createNewPlayer(String firstName, String lastName) {
        Player newPlayer = new Player(UUID.randomUUID(), firstName, lastName, false);
    	playerRepo.createOrUpdatePlayer(newPlayer);
    	return newPlayer;
    }
    
    public Player createDummyPlayer(String firstName, String lastName) {
        Player newDummyPlayer = new Player(UUID.randomUUID(), firstName, lastName, true);
    	return newDummyPlayer;
    }

    @Override
    public Player updatePlayer(Player player) {
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
}
