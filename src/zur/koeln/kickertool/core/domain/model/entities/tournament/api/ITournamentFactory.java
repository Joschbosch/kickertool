package zur.koeln.kickertool.core.domain.model.entities.tournament.api;

import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public interface ITournamentFactory {

    Tournament createNewTournament(String name);
}
