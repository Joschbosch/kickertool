package zur.koeln.kickertool.core.api;

import java.util.UUID;

import zur.koeln.kickertool.core.model.Tournament;

public interface ITournamentRepository {

    void saveOrUpdateTournament(Tournament newTournament);

    Tournament getTournament(UUID tournamentIDToStart);

}
