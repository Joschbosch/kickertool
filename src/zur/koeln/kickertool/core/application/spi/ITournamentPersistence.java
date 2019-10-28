package zur.koeln.kickertool.core.application.spi;

import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public interface ITournamentPersistence {

    void insert(Tournament tournament);

    void update(Tournament tournament);

    Tournament findByUID(UUID tournamentUID);
}
