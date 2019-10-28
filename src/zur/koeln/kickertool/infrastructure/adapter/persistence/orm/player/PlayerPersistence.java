package zur.koeln.kickertool.infrastructure.adapter.persistence.orm.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.ModelMapper;

import zur.koeln.kickertool.core.application.spi.IPlayerPersistence;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.player.persistencemodel.PlayerPersistenceModelObject;
import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.player.spi.PlayerPersistenceDatabase;

@Named
public class PlayerPersistence
    implements IPlayerPersistence {

    private final PlayerPersistenceDatabase playerDatabase;

    private final ModelMapper mapper;

    @Inject
    public PlayerPersistence(
        PlayerPersistenceDatabase playerRepo,
        ModelMapper mapper) {
        this.playerDatabase = playerRepo;
        this.mapper = mapper;
    }

    @Override
    public void insert(Player player) {
        if (!player.isDummy()) {
            playerDatabase.save(mapper.map(player, PlayerPersistenceModelObject.class));
        }
    }

    @Override
    public void update(Player player) {
        if (!player.isDummy()) {
            playerDatabase.save(mapper.map(player, PlayerPersistenceModelObject.class));
        }

    }

    @Override
    public Player findPlayerByUID(UUID playerUID) {
        Optional<PlayerPersistenceModelObject> findById = playerDatabase.findById(playerUID);
        if (findById.isPresent()) {
            return mapper.map(findById.get(), Player.class);

        } else {
            return null;
        }
    }

    @Override
    public List<Player> getAllPlayer() {
        Iterable<PlayerPersistenceModelObject> findAll = playerDatabase.findAll();
        List<Player> result = new ArrayList<>();
        findAll.forEach(entity -> result.add(mapper.map(entity, Player.class)));
        return result;
    }

    @Override
    public void removePlayer(Player player) {
        if (!player.isDummy()) {
            playerDatabase.deleteById(player.getUid());
        }
    }

}
