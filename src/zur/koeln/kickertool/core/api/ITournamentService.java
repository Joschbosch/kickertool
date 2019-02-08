package zur.koeln.kickertool.core.api;

import java.util.UUID;

import zur.koeln.kickertool.core.model.Settings;
import zur.koeln.kickertool.core.model.Tournament;

public interface ITournamentService {

    Tournament createNewTournament();

    Tournament startTournament(UUID tournamentIDToStart);

    Tournament renameTournament(UUID tournamentIDToRename, String name);

    boolean addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    boolean removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant);

    Settings changeTournamentSettings(Settings settings);

}
