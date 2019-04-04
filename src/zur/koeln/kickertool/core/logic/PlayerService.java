package zur.koeln.kickertool.core.logic;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.spi.IPlayerRepository;

@Service
@Setter
public class PlayerService
    implements IPlayerService {

	@Autowired
    private IPlayerRepository playerRepo;

    public Player createNewPlayer(String firstName, String lastName) {

        return playerRepo.createNewPlayer(firstName, lastName);
    }
    
    @Override
    public Player updatePlayerName(UUID id, String newFirstName, String newLastName) {
        Player player = playerRepo.getPlayer(id);
        player.changeName(newFirstName, newLastName);
        return playerRepo.storeOrUpdatePlayer(player);
    }

    @Override
    public Player pauseOrUnpausePlayer(UUID playerToPause, boolean pausing) {
        Player p = playerRepo.getPlayer(playerToPause);
        p.setStatus(pausing ? PlayerStatus.PAUSING_TOURNAMENT : PlayerStatus.IN_TOURNAMENT);
        return p;
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
    public Player getPlayerById(UUID playerId) {
        return playerRepo.getPlayer(playerId);
    }

    @Override
    public Player getDummyPlayer() {
        return playerRepo.getNewOrFreeDummyPlayer();
    }

}
