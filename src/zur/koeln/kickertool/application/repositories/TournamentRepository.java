package zur.koeln.kickertool.application.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.application.repositories.persistence.ITournamentPersistence;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.spi.ITournamentRepository;

@Named
public class TournamentRepository
    implements ITournamentRepository {

    private final ITournamentPersistence tournamentPersistence;

    private final Map<UUID, Tournament> cachedTournaments;

    @Inject
    public TournamentRepository(
        ITournamentPersistence tournamentPersistence) {
        this.tournamentPersistence = tournamentPersistence;
        cachedTournaments = new HashMap<>();
    }

    @Override
    public void saveOrUpdateTournament(Tournament newTournament) {
        Tournament oldTournament = this.getTournament(newTournament.getUid());
        if (oldTournament == null) {
            tournamentPersistence.insert(newTournament);
        } else {
            tournamentPersistence.update(newTournament);
        }
    }

    @Override
    public Tournament getTournament(UUID tournamentID) {
        if (cachedTournaments.containsKey(tournamentID)) {
            return cachedTournaments.get(tournamentID);
        }
        return tournamentPersistence.findByUID(tournamentID);
    }

    @Override
    public Tournament createNewTournament(String tournamentName) {
        Tournament newTournament = new Tournament();
        newTournament.setName(tournamentName);
        newTournament.setUid(UUID.randomUUID());
        newTournament.setStatus(TournamentStatus.PREPARING);
        cachedTournaments.put(newTournament.getUid(), newTournament);
        return newTournament;
    }

}
