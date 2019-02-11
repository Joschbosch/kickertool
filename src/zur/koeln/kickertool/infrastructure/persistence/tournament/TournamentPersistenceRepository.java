package zur.koeln.kickertool.infrastructure.persistence.tournament;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.persistence.entities.TournamentEntity;

public interface TournamentPersistenceRepository
    extends CrudRepository<TournamentEntity, UUID> {
	// nothing to do
}
