package zur.koeln.kickertool.core.bl.logic.tournament;

import java.util.UUID;

import javax.inject.Named;

import zur.koeln.kickertool.core.bl.api.ITournamentFactory;
import zur.koeln.kickertool.core.bl.model.tournament.Tournament;
import zur.koeln.kickertool.core.bl.model.tournament.TournamentStatus;

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
