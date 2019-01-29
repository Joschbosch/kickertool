/**
 * 
 */
package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.ToolState;
import zur.koeln.kickertool.api.PersistenceService;
import zur.koeln.kickertool.api.ui.GUIController;
import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.entities.*;
import zur.koeln.kickertool.core.kernl.TournamentMode;
import zur.koeln.kickertool.core.kernl.TournamentSetingsKeys;
import zur.koeln.kickertool.core.logic.MatchService;
import zur.koeln.kickertool.core.logic.TournamentService;

@Service
public class BasicBackendController {

    @Autowired
    private IPlayerRepository playerpool;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    private GUIController guiController;

    /**
     * 
     */
    public void showPlayerPoolManagement() {
        guiController.switchToolState(ToolState.PLAYER_POOL);
    }

    /**
     * 
     */
    public void showMainMenu() {
        guiController.switchToolState(ToolState.MAIN_MENU);
    }

    /**
     * 
     */
    public void savePlayerPool() {
        playerpool.savePlayerPool();
    }

    /**
     * @param newPlayer
     */
    public Player addPlayerToPool(String newPlayerName) {
        return playerpool.createAndAddPlayer(newPlayerName);
    }

    /**
     * @param player
     */
    public void removePlayerFromPool(Player player) {
        playerpool.removePlayer(player);
    }

    /**
     * @param text
     */
    public Tournament createNewTournament(String text) {
        tournamentService.createNewTournament();
        tournamentService.getTournament().setName(text);
        guiController.switchToolState(ToolState.NEW_TOURNAMENT_CONFIG);
        return tournamentService.getTournament();
    }
    public boolean isCurrentRoundComplete() {
        return tournamentService.isCurrentRoundComplete();
    }

    /**
     * @param configKey
     * @param newValue
     */
    public void changedTournamentConfig(TournamentSetingsKeys configKey, Integer newValue) {
        switch (configKey) {
            case TABLES :
                tournamentService.getTournamentSettings().setTableCount(newValue.intValue());
                break;
            case MATCHES_TO_WIN :
                tournamentService.getTournamentSettings().setMatchesToWin(newValue.intValue());
                break;
            case GOALS_FOR_WIN :
                tournamentService.getTournamentSettings().setGoalsToWin(newValue.intValue());
                break;
            case POINTS_FOR_WINNER :
                tournamentService.getTournamentSettings().setPointsForWinner(newValue.intValue());
                break;
            case POINTS_FOR_DRAW :
                tournamentService.getTournamentSettings().setPointsForDraw(newValue.intValue());
                break;
            case MINUTES_PER_MATCH :
                tournamentService.getTournamentSettings().setMinutesPerMatch(newValue.intValue());
                break;
            case RANDOM_ROUNDS :
                tournamentService.getTournamentSettings().setRandomRounds(newValue.intValue());
                break;
            default :
                break;
        }

    }
    public void changeMode(TournamentMode newMode) {
        tournamentService.getTournamentSettings().setMode(newMode);
    }

    /**
     * 
     */
    public void showPlayerSelection() {
        guiController.switchToolState(ToolState.PLAYER_CONFIG);
    }

    /**
     * 
     */
    public void showTournamentConfig() {
        guiController.switchToolState(ToolState.NEW_TOURNAMENT_CONFIG);
    }

    /**
     * 
     */
    public void startTournament() {
        tournamentService.startTournament();
        guiController.switchToolState(ToolState.TOURNAMENT);
    }

    /**
     * @param p
     */
    public void addParticipantToTournament(Player p) {
        tournamentService.addParticipant(p);
    }

    /**
     * @param p
     */
    public void removeParticipantFromTournament(Player p) {
        tournamentService.removeParticipant(p);
    }

    /**
     * @return
     */
    public Collection<Player> getParticipantList() {
        Set<Player> participants = new HashSet<>();
        tournamentService.getTournament().getParticipants().forEach(id -> participants.add(playerpool.getPlayerOrDummyById(id)));
        return participants;
    }

    /**
     * 
     */
    public Round nextRound() {
        Round newRound = tournamentService.newRound();
        exportTournament();
        return newRound;
    }

    /**
     * @param match
     * @param scoreHome
     * @param value
     */
    public void updateMatchResult(Match match, Integer scoreHome, Integer scoreVisiting) {
        if (match.getResult() == null) {
            tournamentService.addMatchResult(match);
        }
        matchService.setResultScores(match, scoreHome.intValue(), scoreVisiting.intValue());
        exportTournament();

    }
    public Player getPlayer(UUID selectedPlayer) {
        return playerpool.getPlayerOrDummyById(selectedPlayer);
    }
    public void pausePlayer(UUID selectedPlayer) {
        tournamentService.pausePlayer(playerpool.getPlayerOrDummyById(selectedPlayer));

    }
    public void unpausePlayer(UUID selectedPlayer) {
        tournamentService.unpausePlayer(playerpool.getPlayerOrDummyById(selectedPlayer));
    }

    public List<Player> getPlayer() {
        return playerpool.getPlayers();
    }

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }
    public void createAndAddNewPlayerToPoolAndTournament(String playerName) {
        Player newPlayer = addPlayerToPool(playerName);
        addParticipantToTournament(newPlayer);
    }

    public List<Player> getPlayerListNotInTournament() {
        return playerpool.getPlayers().stream().filter(player -> !tournamentService.getTournament().getScoreTable().containsKey(player.getUid())).collect(Collectors.toList());
    }

    public List<PlayerStatistics> getCurrentTable() {
        return new ArrayList<>(tournamentService.getTournament().getScoreTable().values());
    }

    public List<Match> getMatchesForRound(int roundNo) {
        List<Match> matches = tournamentService.getMatchesForRound(roundNo);
        //    	Collections.sort(matches);
        return matches;
    }

    public Tournament getCurrentTournament() {
        return tournamentService.getTournament();
    }

    public IPlayerRepository getPlayerpool() {
        return playerpool;
    }
    public void changePlayerName(String newName, Player selectedPlayer) {
        playerpool.changePlayerName(newName, selectedPlayer);
        savePlayerPool();

    }
    public void exportTournament() {
        persistenceService.exportTournament(getCurrentTournament());
    }
    public List<String> createTournamentsListForImport() {
        return persistenceService.createTournamentsListForImport();
    }
    public void importAndStartTournament(String tournamentNameToImport) throws IOException {
        tournamentService.setTournament(persistenceService.importTournament(new File("tournament" + tournamentNameToImport + ".json"))); //$NON-NLS-1$ //$NON-NLS-2$
        guiController.switchToolState(ToolState.TOURNAMENT);
        guiController.update();
    }
    public boolean isPlayerPausing(Player selectedPlayer) {
        return getCurrentTournament().getScoreTable().get(selectedPlayer.getUid()).isPlayerPausing();
    }
}
