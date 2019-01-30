package zur.koeln.kickertool.core.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.IDummyPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.entities.Player;

@Service
public class PlayerService
    implements IPlayerService {

	@Autowired
    private IPlayerRepository playerRepo;
	
	@Autowired
	private IDummyPlayerRepository dummyPlayerRepo;
    

    public Player createNewPlayer(String firstName, String lastName) {
        Player newPlayer = new Player(UUID.randomUUID(), firstName, lastName, false);
    	playerRepo.addPlayer(newPlayer);
    	return newPlayer;
    }
    
    public Player createDummyPlayer(String firstName, String lastName) {
        Player newDummyPlayer = new Player(UUID.randomUUID(), firstName, lastName, true);
    	return newDummyPlayer;
    }

    @Override
    public void updatePlayer(Player player) {
        playerRepo.updatePlayer(player);
    }

    @Override
    public void deletePlayer(Player player) {
        playerRepo.deletePlayer(player);
    }
}
