package zur.koeln.kickertool.core.domain.service.tournament;

import java.util.UUID;

import javax.inject.Named;

import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;
import zur.koeln.kickertool.core.domain.model.entities.tournament.TournamentStatus;
import zur.koeln.kickertool.core.domain.service.tournament.api.ITournamentFactory;

@Named
public class TournamentFactory
    implements ITournamentFactory {

    @Override
    public Tournament createNewTournament(String name) {
        Tournament newTournament = new Tournament();
        newTournament.setName(name);
        newTournament.setUid(UUID.randomUUID());
        newTournament.setStatus(TournamentStatus.PREPARING);
        return newTournament;
    }

}
