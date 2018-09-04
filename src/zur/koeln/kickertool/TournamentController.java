/**
 * 
 */
package zur.koeln.kickertool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import zur.koeln.kickertool.base.AbstractGUIController;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.player.PlayerPool;
import zur.koeln.kickertool.tools.SimpleTimer;
import zur.koeln.kickertool.tools.Timer;
import zur.koeln.kickertool.tournament.Match;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.tournament.Tournament;
import zur.koeln.kickertool.tournament.TournamentConfigurationKeys;
import zur.koeln.kickertool.ui.GUIState;

@Getter
public class TournamentController {

    private final PlayerPool playerpool;

    private final AbstractGUIController guiController;

    private static TournamentController instance;

    private Tournament currentTournament;

    private final Timer timer;

    /**
     * 
     */
    public TournamentController(AbstractGUIController guiController) {
        instance = this;
        playerpool = new PlayerPool();
        timer = new SimpleTimer();
        this.guiController = guiController;
    }

    public static TournamentController getInstance() {
        return instance;
    }

    /**
     * 
     */
    public void showPlayerPoolManagement() {
        guiController.switchStateTo(GUIState.PLAYER_POOL);
    }

    /**
     * 
     */
    public void backToMainMenu() {
        guiController.switchStateTo(GUIState.MAIN_MENU);
    }

    /**
     * 
     */
    public void playerEdited() {
        playerpool.savePlayerPool();
    }

    /**
     * @param newPlayer
     */
    public void addPlayer(Player newPlayer) {
        playerpool.addPlayer(newPlayer);
    }

    /**
     * @param player
     */
    public void removePlayer(Player player) {
        playerpool.removePlayer(player);
    }

    /**
     * @param text
     */
    public void createNewTournament(String text) {
        currentTournament = new Tournament();
        currentTournament.setName(text);
        guiController.switchStateTo(GUIState.NEW_TOURNAMENT_CONFIG);
    }

    public boolean isCurrentRoundComplete() {
        return currentTournament.isCurrentRoundComplete();
    }

    /**
     * @param configKey
     * @param newValue
     */
    public void changedTournamentConfig(TournamentConfigurationKeys configKey, Integer newValue) {
        switch (configKey) {
        case TABLES:
            currentTournament.getConfig().setTableCount(newValue.intValue());
            break;
        case MATCHES_TO_WIN:
            currentTournament.getConfig().setMatchesToWin(newValue.intValue());
            break;
        case GOALS_FOR_WIN:
            currentTournament.getConfig().setGoalsToWin(newValue.intValue());
            break;
        case POINTS_FOR_WINNER:
            currentTournament.getConfig().setPointsForWinner(newValue.intValue());
            break;
        case POINTS_FOR_DRAW:
            currentTournament.getConfig().setPointsForDraw(newValue.intValue());
            break;
        case MINUTES_PER_MATCH:
            currentTournament.getConfig().setMinutesPerMatch(newValue.intValue());
            break;
        case RANDOM_ROUNDS:
            currentTournament.getConfig().setRandomRounds(newValue.intValue());
            break;
        default:
            break;
        }

    }

    /**
     * 
     */
    public void selectPlayer() {
        guiController.switchStateTo(GUIState.PLAYER_CONFIG);
    }

    /**
     * 
     */
    public void backToTournamentConfig() {
        guiController.switchStateTo(GUIState.NEW_TOURNAMENT_CONFIG);
    }

    /**
     * 
     */
    public void startTournament() {
        currentTournament.startTournament();
        guiController.switchStateTo(GUIState.TOURNAMENT);
        timer.setTimer(currentTournament.getConfig().getMinutesPerMatch());
    }

    public void importAndStartTournament(File tournamentToImport) throws IOException {
        importTournament(tournamentToImport);
        guiController.switchStateTo(GUIState.TOURNAMENT);
        timer.setTimer(currentTournament.getConfig().getMinutesPerMatch());
    }

    /**
     * @param p
     */
    public void addParticipantToTournament(Player p) {
        currentTournament.addParticipant(p);
    }

    /**
     * @param p
     */
    public void removeParticipantFromTournament(Player p) {
        currentTournament.removeParticipant(p);
    }

    /**
     * @return
     */
    public Collection<Player> getParticipantList() {
        Set<Player> participants = new HashSet<>();
        currentTournament.getParticipants().forEach(id -> participants.add(PlayerPool.getInstance().getPlayerById(id)));
        return participants;
    }

    /**
     * 
     */
    public void nextRound() {
        currentTournament.newRound();
        guiController.update();
        exportTournament();
    }

    /**
     * @param match
     * @param scoreHome
     * @param value
     */
    public void updateMatchResult(Match match, Integer scoreHome, Integer scoreVisiting) {
        try {
            if (match.getResult() == null) {
                match.setResult(scoreHome.intValue(), scoreVisiting.intValue());
                currentTournament.addMatchResult(match);
            } else {
                match.setResult(scoreHome.intValue(), scoreVisiting.intValue());
            }
        } catch (MatchException e) {
            e.printStackTrace();
        }
        guiController.update();
        exportTournament();

    }

    public Player getPlayer(UUID selectedPlayer) {
        return PlayerPool.getInstance().getPlayerById(selectedPlayer);
    }

    public void pausePlayer(UUID selectedPlayer) {
        currentTournament.pausePlayer(selectedPlayer);

    }

    public void unpausePlayer(UUID selectedPlayer) {
        currentTournament.unpausePlayer(selectedPlayer);

    }

    public void startStopwatch() {
        timer.start();
    }

    public void stopStopwatch() {
        timer.stop();
    }

    public void resetStopWatch() {
        timer.reset();
    }
    private Tournament importTournament(File tournamentToImport) throws IOException {
        ObjectMapper m = new ObjectMapper();
        currentTournament = m.readValue(tournamentToImport, Tournament.class);
        currentTournament.initializeAfterImport();

        return currentTournament;

    }


    public void exportTournament() {
        File tournamentFile = new File("tournament" + currentTournament.getName() + ".json"); //$NON-NLS-1$
        ObjectMapper m = new ObjectMapper();
        try {
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentFile, currentTournament);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
