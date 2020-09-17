package zur.koeln.kickertool.core.ports.infrastructure;

import java.util.UUID;

import zur.koeln.kickertool.core.bl.model.tournament.Tournament;

public interface ITournamentPersistence {

    void insert(Tournament tournament);

    void update(Tournament tournament);

    Tournament findByUID(UUID tournamentUID);
}
