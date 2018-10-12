package zur.koeln.kickertool.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.UUID;

import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.config.TournamentSetingsKeys;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.api.ui.GUIController;

public interface BackendController {

    void showMainMenu();

    void showPlayerPoolManagement();

    void savePlayerPool();

    Player addPlayerToPool(String playerName);

    void removePlayerFromPool(Player player);

    Tournament createNewTournament(String text);

    boolean isCurrentRoundComplete();

    void changedTournamentConfig(TournamentSetingsKeys configKey, Integer newValue);

    void showPlayerSelection();

    void showTournamentConfig();

    void startTournament();

    void importAndStartTournament(String tournamentNameToImport) throws IOException;

    void addParticipantToTournament(Player p);

    void removeParticipantFromTournament(Player p);

    Collection<Player> getParticipantList();

    Round nextRound();

    void updateMatchResult(Match match, Integer scoreHome, Integer scoreVisiting);

    Player getPlayer(UUID selectedPlayer);

    void pausePlayer(UUID selectedPlayer);

    void unpausePlayer(UUID selectedPlayer);

    void exportTournament();

    List<Player> getPlayer();

    Tournament getCurrentTournament();

    PlayerPoolService getPlayerpool();

    void setGuiController(GUIController guiController);

    void changeMode(TournamentMode newMode);

    SortedSet<PlayerTournamentStatistics> getCurrentTable();

    List<Player> getPlayerListNotInTournament();

    void createAndAddNewPlayerToPoolAndTournament(String name);

    List<Match> getMatchesForRound(int roundNo);

    void changePlayerName(String newName, Player selectedPlayer);

    List<String> createTournamentsListForImport();

	boolean isPlayerPausing(Player selectedPlayer);

    void init();
}
