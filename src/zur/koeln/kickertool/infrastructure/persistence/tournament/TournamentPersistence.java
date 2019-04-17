package zur.koeln.kickertool.infrastructure.persistence.tournament;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.repositories.persistence.ITournamentPersistence;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.infrastructure.persistence.entities.TournamentEntity;

@Component
public class TournamentPersistence
    implements ITournamentPersistence {

    @Autowired
    private TournamentPersistenceRepository repo;

    @Autowired
    private ModelMapper mapper;

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
