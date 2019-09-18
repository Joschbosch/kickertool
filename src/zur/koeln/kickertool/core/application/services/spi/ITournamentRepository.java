package zur.koeln.kickertool.core.application.services.spi;

import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public interface ITournamentRepository {

    void saveOrUpdateTournament(Tournament newTournament);

    Tournament getTournament(UUID tournamentIDToStart);

}