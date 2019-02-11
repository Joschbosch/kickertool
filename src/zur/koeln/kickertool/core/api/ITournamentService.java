package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.model.Match;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Settings;
import zur.koeln.kickertool.core.model.Tournament;

public interface ITournamentService {

    Tournament createNewTournament();

    Tournament startTournament(UUID tournamentIDToStart);

    Tournament renameTournament(UUID tournamentIDToRename, String name);

    List<Player> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    List<Player> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant);

    Settings changeTournamentSettings(Settings settings);

    List<Player> getTournamentParticipants(UUID tournamentId);

    void startNewRound(UUID tournamentToStartNewRound);

    boolean isCurrentRoundComplete(UUID tournamentId);

    List<Match> getMatchesForRound(int roundNo, UUID tournamentId);

    Tournament getTournamentById(UUID tournamentUID);

    List<Player> getTournamentStandingsForRound(UUID tournamentUID, int roundNo);

    List<Player> getCurrentTournamentStandings(UUID tournamentUID);

    void addNewMatchToTournament(UUID uid, Match m);



}
