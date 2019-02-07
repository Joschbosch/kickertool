package zur.koeln.kickertool.infrastructure.persistence.player;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.persistence.entities.PlayerEntity;

public interface PlayerPersistenceRepository
    extends CrudRepository<PlayerEntity, UUID> {
    // nothing needed
}
