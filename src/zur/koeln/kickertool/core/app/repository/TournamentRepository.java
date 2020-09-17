package zur.koeln.kickertool.core.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.bl.model.tournament.Tournament;
import zur.koeln.kickertool.core.ports.infrastructure.ITournamentPersistence;

@Named
public class TournamentRepository{
    private final ITournamentPersistence tournamentPersistence;

    private final Map<UUID, Tournament> cachedTournaments;

    @Inject
    public TournamentRepository(
        ITournamentPersistence tournamentPersistence) {
        this.tournamentPersistence = tournamentPersistence;
        cachedTournaments = new HashMap<>();
    }

    public void saveOrUpdateTournament(Tournament newTournament) {
        Tournament oldTournament = this.getTournament(newTournament.getUid());
        if (oldTournament == null) {
            tournamentPersistence.insert(newTournament);
        } else {
            tournamentPersistence.update(newTournament);
        }
    }

    public Tournament getTournament(UUID tournamentID) {
        if (cachedTournaments.containsKey(tournamentID)) {
            return cachedTournaments.get(tournamentID);
        }
        return tournamentPersistence.findByUID(tournamentID);
    }



}
