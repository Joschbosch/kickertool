/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import zur.koeln.kickertool.base.AbstractGUIController;
import zur.koeln.kickertool.base.PlayerPoolService;
import zur.koeln.kickertool.base.TournamentControllerService;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tools.Timer;
import zur.koeln.kickertool.tournament.content.Match;
import zur.koeln.kickertool.tournament.content.Tournament;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;
import zur.koeln.kickertool.ui.GUIState;

@Getter
@Component
public class BasicTournamentController
    implements TournamentControllerService {

    @Autowired
    private PlayerPoolService playerpool;

    @Autowired
    private TournamentFactory tournamentFactory;

    @Autowired
    private Timer timer;

    private AbstractGUIController guiController;

    private Tournament currentTournament;


    /**
     * 
     */
    @Override
    public void showPlayerPoolManagement() {
        guiController.switchStateTo(GUIState.PLAYER_POOL);
    }

    /**
     * 
     */
    @Override
    public void showMainMenu() {
        guiController.switchStateTo(GUIState.MAIN_MENU);
    }

    /**
     * 
     */
    @Override
    public void savePlayerPool() {
        playerpool.savePlayerPool();
    }

    /**
     * @param newPlayer
     */
    @Override
    public void addPlayer(Player newPlayer) {
        playerpool.addPlayer(newPlayer);
    }

    /**
     * @param player
     */
    @Override
    public void removePlayer(Player player) {
        playerpool.removePlayer(player);
    }

    /**
     * @param text
     */
    @Override
    public Tournament createNewTournament(String text) {
        currentTournament = tournamentFactory.createNewTournament();
        currentTournament.setName(text);
        guiController.switchStateTo(GUIState.NEW_TOURNAMENT_CONFIG);
        return currentTournament;
    }
    @Override
    public boolean isCurrentRoundComplete() {
        return currentTournament.isCurrentRoundComplete();
    }

    /**
     * @param configKey
     * @param newValue
     */
    @Override
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
    @Override
    public void showPlayerSelection() {
        guiController.switchStateTo(GUIState.PLAYER_CONFIG);
    }

    /**
     * 
     */
    @Override
    public void showTournamentConfig() {
        guiController.switchStateTo(GUIState.NEW_TOURNAMENT_CONFIG);
    }

    /**
     * 
     */
    @Override
    public void startTournament() {
        currentTournament.startTournament();
        guiController.switchStateTo(GUIState.TOURNAMENT);
        timer.setTimer(currentTournament.getConfig().getMinutesPerMatch());
    }
    @Override
    public void importAndStartTournament(File tournamentToImport) throws IOException {
        importTournament(tournamentToImport);
        guiController.switchStateTo(GUIState.TOURNAMENT);
        timer.setTimer(currentTournament.getConfig().getMinutesPerMatch());
    }

    /**
     * @param p
     */
    @Override
    public void addParticipantToTournament(Player p) {
        currentTournament.addParticipant(p);
    }

    /**
     * @param p
     */
    @Override
    public void removeParticipantFromTournament(Player p) {
        currentTournament.removeParticipant(p);
    }

    /**
     * @return
     */
    @Override
    public Collection<Player> getParticipantList() {
        Set<Player> participants = new HashSet<>();
        currentTournament.getParticipants().forEach(id -> participants.add(playerpool.getPlayerById(id)));
        return participants;
    }

    /**
     * 
     */
    @Override
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
    @Override
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
    @Override
    public Player getPlayer(UUID selectedPlayer) {
        return playerpool.getPlayerById(selectedPlayer);
    }
    @Override
    public void pausePlayer(UUID selectedPlayer) {
        currentTournament.pausePlayer(selectedPlayer);

    }
    @Override
    public void unpausePlayer(UUID selectedPlayer) {
        currentTournament.unpausePlayer(selectedPlayer);

    }
    @Override
    public void startStopwatch() {
        timer.start();
    }
    @Override
    public void stopStopwatch() {
        timer.stop();
    }
    @Override
    public void resetStopWatch() {
        timer.reset();
    }
    private Tournament importTournament(File tournamentToImport) throws IOException {
        ObjectMapper m = new ObjectMapper();
        currentTournament = m.readValue(tournamentToImport, Tournament.class);
        currentTournament.setPlayerPool(playerpool);
        currentTournament.setTournamentFactory(tournamentFactory);
        currentTournament.initializeAfterImport();
        return currentTournament;

    }
    @Override
    public List<Player> getPlayer() {
        return playerpool.getPlayers();
    }
    @Override
    public void exportTournament() {
        File tournamentFile = new File("tournament" + currentTournament.getName() + ".json"); //$NON-NLS-1$
        ObjectMapper m = new ObjectMapper();
        try {
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentFile, currentTournament);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGuiController(AbstractGUIController guiController) {
        this.guiController = guiController;
    }

}
