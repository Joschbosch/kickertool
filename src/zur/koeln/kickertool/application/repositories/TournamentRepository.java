package zur.koeln.kickertool.application.repositories;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.ITournamentPersistence;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.spi.ITournamentRepository;

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

    @Override
    public Tournament createNewTournament(String tournamentName) {
        Tournament newTournament = new Tournament();
        newTournament.setName(tournamentName);
        newTournament.setUid(UUID.randomUUID());
        newTournament.setStatus(TournamentStatus.PREPARING);
        return newTournament;
    }

}
