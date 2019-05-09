package zur.koeln.kickertool.infrastructure.persistence.tournament;

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.ModelMapper;

import zur.koeln.kickertool.application.repositories.persistence.ITournamentPersistence;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.infrastructure.persistence.entities.TournamentEntity;

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
        repo.save(mapper.map(tournament, TournamentEntity.class));
    }

    @Override
    public void update(Tournament tournament) {
        repo.save(mapper.map(tournament, TournamentEntity.class));
    }

    @Override
    public Tournament findByUID(UUID tournamentUID) {
        Optional<TournamentEntity> entityOptional = repo.findById(tournamentUID);
        if (entityOptional.isPresent()) {
            return mapper.map(entityOptional.get(), Tournament.class);
        }
        return null;
    }

}
