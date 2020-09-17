package zur.koeln.kickertool.infrastructure.persistence.spi;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.persistence.persistencemodel.player.PlayerPersistenceModelObject;

public interface PlayerPersistenceDatabase
    extends CrudRepository<PlayerPersistenceModelObject, UUID> {
    // nothing needed
}
