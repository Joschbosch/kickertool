package zur.koeln.kickertool.infrastructure.persistence.spi;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.persistence.persistencemodel.tournament.TournamentPersistenceModelObject;

public interface TournamentPersistenceRepository
    extends CrudRepository<TournamentPersistenceModelObject, UUID> {
	// nothing to do
}
