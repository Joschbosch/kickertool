package zur.koeln.kickertool.infrastructure.persistence.tournament;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.ITournamentPersistence;
import zur.koeln.kickertool.core.model.Tournament;
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
        return mapper.map(repo.findById(tournamentUID), Tournament.class);
    }

}
