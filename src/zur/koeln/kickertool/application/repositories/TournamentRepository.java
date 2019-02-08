package zur.koeln.kickertool.application.repositories;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.ITournamentPersistence;
import zur.koeln.kickertool.core.api.ITournamentRepository;
import zur.koeln.kickertool.core.model.Tournament;

@Component
public class TournamentRepository
    implements ITournamentRepository {

    @Autowired
    private ITournamentPersistence tournamentPersistence;

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
        return tournamentPersistence.findByUID(tournamentID);
    }

}
