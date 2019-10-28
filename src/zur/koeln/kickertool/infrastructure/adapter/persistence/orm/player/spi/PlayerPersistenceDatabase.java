package zur.koeln.kickertool.infrastructure.adapter.persistence.orm.player.spi;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.player.persistencemodel.PlayerPersistenceModelObject;

public interface PlayerPersistenceDatabase
    extends CrudRepository<PlayerPersistenceModelObject, UUID> {
    // nothing needed
}
