package zur.koeln.kickertool.api;

import java.io.IOException;
import java.util.List;

import zur.koeln.kickertool.core.entities.Tournament;

public interface PersistenceService {

    void exportTournament(Tournament currentTournament);

    List<String> createTournamentsListForImport();

    Tournament importTournament(String tournamentNameToImport) throws IOException;

}
