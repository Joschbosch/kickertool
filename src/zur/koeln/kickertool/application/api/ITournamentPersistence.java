package zur.koeln.kickertool.application.api;

import java.util.UUID;

import zur.koeln.kickertool.core.model.aggregates.Tournament;

public interface ITournamentPersistence {

    void insert(Tournament tournament);

    void update(Tournament tournament);

    Tournament findByUID(UUID tournamentUID);
}
