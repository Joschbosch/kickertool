package zur.koeln.kickertool.core.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.IDummyPlayerRepository;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.entities.Player;

@Service
public class PlayerService{

	@Autowired
    private IPlayerRepository playerRepo;
	
	@Autowired
	private IDummyPlayerRepository dummyPlayerRepo;
    
       
    public Player createNewPlayer(String name) {
    	Player newPlayer = new Player(UUID.randomUUID(), name, false );
    	playerRepo.insertPlayer(newPlayer);
    	return newPlayer;
    }
    
    public Player createDummyPlayer(String name) {
    	Player newDummyPlayer = new Player(UUID.randomUUID(), name, true);
    	
    	return newDummyPlayer;
    }
}
