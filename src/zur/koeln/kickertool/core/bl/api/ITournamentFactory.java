package zur.koeln.kickertool.core.bl.api;

import zur.koeln.kickertool.core.bl.model.tournament.Tournament;

public interface ITournamentFactory {

    Tournament createNewTournament(String name);
}
