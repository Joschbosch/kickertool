package zur.koeln.kickertool.core.application.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.common.PlayerRankingRow;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Match;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Settings;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public interface ITournamentService {

    Tournament createNewTournament(String tournamentName, List<Player> list, Settings settings);

    Tournament startTournament(UUID tournamentIDToStart);

    Tournament renameTournament(UUID tournamentIDToRename, String name);

    List<Player> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant);

    List<Player> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participant);

    List<Player> getTournamentParticipants(UUID tournamentId);

    Tournament startNewRound(UUID tournamentToStartNewRound);

    boolean isCurrentRoundComplete(UUID tournamentId);

    List<Match> getMatchesForRound(int roundNo, UUID tournamentId);

    Tournament getTournamentById(UUID tournamentUID);

    Settings getDefaultSettings();

    Settings getSettings(UUID tournamentUid);

    List<PlayerRankingRow> getRankingByRound(UUID tournamentUID, int round);

    boolean enterOrChangeMatchResult(UUID tournamentId, UUID matchID, int scoreHome, int scoreVisiting);

    Tournament pauseOrUnpausePlayer(UUID tournamentId, UUID playerToPause, boolean pausing);

}
