package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Match;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Settings;
import zur.koeln.kickertool.core.model.Tournament;

public interface ITournamentService {

    Tournament createAndStartNewTournament(String tournamentName, List<Player> list, Settings settings);

    Tournament startTournament(UUID tournamentIDToStart);

    Tournament renameTournament(UUID tournamentIDToRename, String name);

    List<Player> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    List<Player> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant);

    List<Player> getTournamentParticipants(UUID tournamentId);

    Tournament startNewRound(UUID tournamentToStartNewRound);

    boolean isCurrentRoundComplete(UUID tournamentId);

    List<Match> getMatchesForRound(int roundNo, UUID tournamentId);

    Tournament getTournamentById(UUID tournamentUID);

    List<Player> getTournamentStandingsForRound(UUID tournamentUID, int roundNo);

    List<Player> getCurrentTournamentStandings(UUID tournamentUID);

    void addNewMatchToTournament(UUID uid, Match m);

    Settings changeTournamentSettings(UUID uuid, Settings settings);

    Player pauseOrUnpausePlayer(UUID playerToPause, boolean pausing);

    Settings getDefaultSettings();

    Settings getSettings(UUID tournamentUid);

}
