package zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.ModelMapper;

import zur.koeln.kickertool.core.application.spi.persistence.ITournamentPersistence;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;
import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament.persistencemodel.TournamentPersistenceModelObject;
import zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament.spi.TournamentPersistenceRepository;

@Named
public class TournamentPersistence
    implements ITournamentPersistence {

    private final TournamentPersistenceRepository repo;

    private final ModelMapper mapper;

    @Inject
    public TournamentPersistence(
        TournamentPersistenceRepository repo,
        ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void insert(Tournament tournament) {
        repo.save(mapper.map(tournament, TournamentPersistenceModelObject.class));
    }

    @Override
    public void update(Tournament tournament) {
        repo.save(mapper.map(tournament, TournamentPersistenceModelObject.class));
    }

    @Override
    public Tournament findByUID(UUID tournamentUID) {
        Optional<TournamentPersistenceModelObject> entityOptional = repo.findById(tournamentUID);
        if (entityOptional.isPresent()) {
            return mapper.map(entityOptional.get(), Tournament.class);
        }
        return null;
    }

}
