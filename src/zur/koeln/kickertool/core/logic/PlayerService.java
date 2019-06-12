package zur.koeln.kickertool.core.logic;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Setter;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.spi.IPlayerRepository;

@Named
@Setter
public class PlayerService
    implements IPlayerService {

    private IPlayerRepository playerRepo;

    @Inject
    public PlayerService(
        IPlayerRepository repository) {
        this.playerRepo = repository;
    }

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
    public void deletePlayer(UUID player) {
        playerRepo.deletePlayer(player);
    }
    @Override
    public List<Player> getAllPlayer() {
        return playerRepo.getAllPlayer();
    }

    @Override
    public Player getPlayerById(UUID playerId) {
        if (playerId != null) {
            return playerRepo.getPlayerOrNewDummyWithId(playerId);
        }
        return null;

    }

    @Override
    public Player getDummyPlayer() {
        return playerRepo.getNewOrFreeDummyPlayer();
    }

    @Override
    public void setPlayerStatus(UUID player, PlayerStatus status) {
        Player p = playerRepo.getPlayerOrNewDummyWithId(player);
        p.setStatus(status);
        playerRepo.storeOrUpdatePlayer(p);
    }
}
