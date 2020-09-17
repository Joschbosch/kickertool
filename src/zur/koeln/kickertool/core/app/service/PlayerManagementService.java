package zur.koeln.kickertool.core.app.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Setter;
import zur.koeln.kickertool.core.app.repository.PlayerRepository;
import zur.koeln.kickertool.core.bl.api.IPlayerFactory;
import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.bl.model.player.PlayerStatus;
import zur.koeln.kickertool.core.ports.ui.IPlayerManagementService;

@Named
@Setter
public class PlayerManagementService
    implements IPlayerManagementService {

    private PlayerRepository playerRepo;

    private IPlayerFactory playerFactory;

    @Inject
    public PlayerManagementService(
        PlayerRepository repository,
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

    @Override
    public Map<UUID, Player> getPlayersMapByIds(List<UUID> playerList) {
        return playerList.stream().collect(Collectors.toMap(k -> k, k -> playerRepo.getPlayer(k)));
    }

	@Override
	public void storeNewDummyPlayer(List<Player> newDummyPlayer) {
		newDummyPlayer.forEach(dummy -> playerRepo.storeOrUpdatePlayer(dummy));
	}

	@Override
	public List<Player> getDummyPlayers() {
		return playerRepo.getFreeDummyPlayers();
	}
}
