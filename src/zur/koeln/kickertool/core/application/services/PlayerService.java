package zur.koeln.kickertool.core.application.services;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Setter;
import zur.koeln.kickertool.core.application.api.IPlayerService;
import zur.koeln.kickertool.core.application.services.spi.IPlayerRepository;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;
import zur.koeln.kickertool.core.domain.model.entities.player.api.IPlayerFactory;

@Named
@Setter
public class PlayerService
    implements IPlayerService {

    private IPlayerRepository playerRepo;
    private IPlayerFactory playerFactory;

    @Inject
    public PlayerService(
        IPlayerRepository repository,
        IPlayerFactory playerFactory) {
        this.playerRepo = repository;
        this.playerFactory = playerFactory;
    }

    public Player createNewPlayer(String firstName, String lastName) {

        Player newPlayer = playerFactory.createNewPlayer(firstName, lastName);
        playerRepo.storeOrUpdatePlayer(newPlayer);
        return newPlayer;
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
            return playerRepo.getPlayer(playerId);
        }
        return null;

    }

    @Override
    public Player getDummyPlayer() {
        List<Player> dummyPlayer = playerRepo.getFreeDummyPlayers();
        if (dummyPlayer.isEmpty()) {
            Player dummy = playerFactory.createNewDummyPlayer(playerRepo.getAllDummyPlayers().size() + 1);
            playerRepo.storeOrUpdatePlayer(dummy);
            return dummy;
        }

        return dummyPlayer.get(0);
    }

    @Override
    public void setPlayerStatus(UUID player, PlayerStatus status) {
        Player p = playerRepo.getPlayer(player);
        p.setStatus(status);
        playerRepo.storeOrUpdatePlayer(p);
    }
}
