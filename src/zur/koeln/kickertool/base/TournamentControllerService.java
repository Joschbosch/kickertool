package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.TournamentConfigurationKeys;
import zur.koeln.kickertool.tournament.content.Match;
import zur.koeln.kickertool.tournament.content.Tournament;

public interface TournamentControllerService {

    void showMainMenu();

    void showPlayerPoolManagement();

    void savePlayerPool();

    void addPlayer(Player newPlayer);

    void removePlayer(Player player);

    Tournament createNewTournament(String text);

    boolean isCurrentRoundComplete();

    void changedTournamentConfig(TournamentConfigurationKeys configKey, Integer newValue);

    void showPlayerSelection();

    void showTournamentConfig();

    void startTournament();

    void importAndStartTournament(File tournamentToImport) throws IOException;

    void addParticipantToTournament(Player p);

    void removeParticipantFromTournament(Player p);

    Collection<Player> getParticipantList();

    void nextRound();

    void updateMatchResult(Match match, Integer scoreHome, Integer scoreVisiting);

    Player getPlayer(UUID selectedPlayer);

    void pausePlayer(UUID selectedPlayer);

    void unpausePlayer(UUID selectedPlayer);

    void startStopwatch();

    void stopStopwatch();

    void resetStopWatch();

    void exportTournament();

    List<Player> getPlayer();

    Tournament getCurrentTournament();

    PlayerPoolService getPlayerpool();

    void setGuiController(AbstractGUIController guiController);

}
