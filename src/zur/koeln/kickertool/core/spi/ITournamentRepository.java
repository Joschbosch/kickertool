package zur.koeln.kickertool.core.spi;

import java.util.UUID;

import zur.koeln.kickertool.core.model.aggregates.Tournament;

public interface ITournamentRepository {

    void saveOrUpdateTournament(Tournament newTournament);

    Tournament getTournament(UUID tournamentIDToStart);

    Tournament createNewTournament(String tournamentName);

}
