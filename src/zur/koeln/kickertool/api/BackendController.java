package zur.koeln.kickertool.api;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.api.content.Match;
import zur.koeln.kickertool.api.content.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.content.Round;
import zur.koeln.kickertool.api.content.Tournament;
import zur.koeln.kickertool.api.ui.GUIController;
import zur.koeln.kickertool.player.Player;

public interface BackendController {

    void showMainMenu();

    void showPlayerPoolManagement();

    void savePlayerPool();

    void addPlayerToPool(Player newPlayer);

    void removePlayerFromPool(Player player);

    Tournament createNewTournament(String text);

    boolean isCurrentRoundComplete();

    void changedTournamentConfig(TournamentConfigKeys configKey, Integer newValue);

    void showPlayerSelection();

    void showTournamentConfig();

    void startTournament();

    void importAndStartTournament(File tournamentToImport) throws IOException;

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

    List<PlayerTournamentStatistics> getCurrentTable();

    List<Player> getPlayerListNotInTournament();

    void createAndAddNewPlayerToPoolAndTournament(String name);
}
