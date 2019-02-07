package zur.koeln.kickertool.infrastructure.persistence.player;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface PlayerPersistenceRepository
    extends CrudRepository<PlayerEntity, UUID> {

}
