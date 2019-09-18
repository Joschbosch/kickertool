package zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament.spi;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament.persistencemodel.TournamentPersistenceModelObject;

public interface TournamentPersistenceRepository
    extends CrudRepository<TournamentPersistenceModelObject, UUID> {
	// nothing to do
}
