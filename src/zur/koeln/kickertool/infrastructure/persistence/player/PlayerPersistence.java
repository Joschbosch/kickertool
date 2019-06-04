package zur.koeln.kickertool.infrastructure.persistence.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.ModelMapper;

import zur.koeln.kickertool.application.repositories.persistence.IPlayerPersistence;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.infrastructure.persistence.entities.PlayerEntity;

@Named
public class PlayerPersistence
    implements IPlayerPersistence {

    private final PlayerPersistenceRepository playerRepo;

    private final ModelMapper mapper;

    @Inject
    public PlayerPersistence(
        PlayerPersistenceRepository playerRepo,
        ModelMapper mapper) {
        this.playerRepo = playerRepo;
        this.mapper = mapper;
    }

    @Override
    public void insert(Player player) {
        if (!player.isDummy()) {
            playerRepo.save(mapper.map(player, PlayerEntity.class));
        }
    }

    @Override
    public void update(Player player) {
        if (!player.isDummy()) {
            playerRepo.save(mapper.map(player, PlayerEntity.class));
        }

    }

    @Override
    public Player findPlayerByUID(UUID playerUID) {
        Optional<PlayerEntity> findById = playerRepo.findById(playerUID);
        if (findById.isPresent()) {
            return mapper.map(findById.get(), Player.class);

        } else {
            return null;
        }
    }

    @Override
    public List<Player> getAllPlayer() {
        Iterable<PlayerEntity> findAll = playerRepo.findAll();
        List<Player> result = new ArrayList<>();
        findAll.forEach(entity -> result.add(mapper.map(entity, Player.class)));
        return result;
    }

    @Override
    public void removePlayer(Player player) {
        if (!player.isDummy()) {
            playerRepo.deleteById(player.getUid());
        }
    }

}
