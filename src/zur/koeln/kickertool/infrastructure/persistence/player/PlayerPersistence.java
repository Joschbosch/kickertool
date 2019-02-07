package zur.koeln.kickertool.infrastructure.persistence.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.IPlayerPersistence;
import zur.koeln.kickertool.core.model.Player;

@Component
public class PlayerPersistence
    implements IPlayerPersistence {

    @Autowired
    private PlayerPersistenceRepository playerRepo;

    @Autowired
    private PlayerPersistenceAdapter adapter;

    @Override
    public void insert(Player player) {
        playerRepo.save(adapter.toEntity(player));
    }

    @Override
    public void update(Player player) {
        playerRepo.save(adapter.toEntity(player));

    }

    @Override
    public Player findPlayerByUID(UUID playerUID) {
        Optional<PlayerEntity> findById = playerRepo.findById(playerUID);
        if (findById.isPresent()) {
            return adapter.fromEntity(findById.get());

        } else {
            return null;
        }
    }

    @Override
    public List<Player> getAllPlayer() {
        Iterable<PlayerEntity> findAll = playerRepo.findAll();
        List<Player> result = new ArrayList<>();
        findAll.forEach(entity -> result.add(adapter.fromEntity(entity)));
        return result;
    }

    @Override
    public void removePlayer(Player player) {
        playerRepo.deleteById(player.getUid());
    }

}
