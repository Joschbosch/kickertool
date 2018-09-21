package zur.koeln.kickertool.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import zur.koeln.kickertool.api.tournament.Tournament;

public interface PersistenceService {

    void exportTournament(Tournament currentTournament);

    List<String> createTournamentsListForImport();

    Tournament importTournament(File tournamentToImport) throws IOException;

}