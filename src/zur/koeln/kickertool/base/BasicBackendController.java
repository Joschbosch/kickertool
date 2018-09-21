/**
 * 
 */
package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.PersistenceService;
import zur.koeln.kickertool.api.ToolState;
import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.config.TournamentSetingsKeys;
import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.api.ui.GUIController;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
public class BasicBackendController
    implements BackendController {

    @Autowired
    private PlayerPoolService playerpool;

    @Autowired
    private TournamentFactory tournamentFactory;
    
    @Autowired
    private PersistenceService persistenceService;

    private GUIController guiController;

    private Tournament currentTournament;

    /**
     * 
     */
    @Override
    public void showPlayerPoolManagement() {
        guiController.switchToolState(ToolState.PLAYER_POOL);
    }

    /**
     * 
     */
    @Override
    public void showMainMenu() {
        guiController.switchToolState(ToolState.MAIN_MENU);
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
    public Player addPlayerToPool(String newPlayerName) {
        return playerpool.createAndAddPlayer(newPlayerName);
    }

    /**
     * @param player
     */
    @Override
    public void removePlayerFromPool(Player player) {
        playerpool.removePlayer(player);
    }

    /**
     * @param text
     */
    @Override
    public Tournament createNewTournament(String text) {
        currentTournament = tournamentFactory.createNewTournament();
        currentTournament.setName(text);
        guiController.switchToolState(ToolState.NEW_TOURNAMENT_CONFIG);
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
    public void changedTournamentConfig(TournamentSetingsKeys configKey, Integer newValue) {
        switch (configKey) {
            case TABLES :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setTableCount(newValue.intValue());
                break;
            case MATCHES_TO_WIN :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setMatchesToWin(newValue.intValue());
                break;
            case GOALS_FOR_WIN :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setGoalsToWin(newValue.intValue());
                break;
            case POINTS_FOR_WINNER :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setPointsForWinner(newValue.intValue());
                break;
            case POINTS_FOR_DRAW :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setPointsForDraw(newValue.intValue());
                break;
            case MINUTES_PER_MATCH :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setMinutesPerMatch(newValue.intValue());
                break;
            case RANDOM_ROUNDS :
                ((TournamentSettingsImpl) currentTournament.getSettings()).setRandomRounds(newValue.intValue());
                break;
            default :
                break;
        }

    }
    @Override
    public void changeMode(TournamentMode newMode) {
        ((TournamentSettingsImpl) currentTournament.getSettings()).setMode(newMode);
    }

    /**
     * 
     */
    @Override
    public void showPlayerSelection() {
        guiController.switchToolState(ToolState.PLAYER_CONFIG);
    }

    /**
     * 
     */
    @Override
    public void showTournamentConfig() {
        guiController.switchToolState(ToolState.NEW_TOURNAMENT_CONFIG);
    }

    /**
     * 
     */
    @Override
    public void startTournament() {
        currentTournament.startTournament();
        guiController.switchToolState(ToolState.TOURNAMENT);
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
        currentTournament.getParticipants().forEach(id -> participants.add(playerpool.getPlayerOrDummyById(id)));
        return participants;
    }

    /**
     * 
     */
    @Override
    public Round nextRound() {
        Round newRound = currentTournament.newRound();
        exportTournament();
        return newRound;
    }

    /**
     * @param match
     * @param scoreHome
     * @param value
     */
    @Override
    public void updateMatchResult(zur.koeln.kickertool.api.tournament.Match match, Integer scoreHome, Integer scoreVisiting) {
        try {
            if (match.getResult() == null) {
                match.setResultScores(scoreHome.intValue(), scoreVisiting.intValue());
                currentTournament.addMatchResult(match);
            } else {
                match.setResultScores(scoreHome.intValue(), scoreVisiting.intValue());
            }
        } catch (MatchException e) {
            e.printStackTrace();
        }
        exportTournament();

    }
    @Override
    public Player getPlayer(UUID selectedPlayer) {
        return playerpool.getPlayerOrDummyById(selectedPlayer);
    }
    @Override
    public void pausePlayer(UUID selectedPlayer) {
        currentTournament.pausePlayer(playerpool.getPlayerOrDummyById(selectedPlayer));

    }
    @Override
    public void unpausePlayer(UUID selectedPlayer) {
        currentTournament.unpausePlayer(playerpool.getPlayerOrDummyById(selectedPlayer));
    }

    @Override
    public List<Player> getPlayer() {
        return playerpool.getPlayers();
    }


	public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }
    @Override
    public void createAndAddNewPlayerToPoolAndTournament(String playerName) {
        Player newPlayer = addPlayerToPool(playerName);
        addParticipantToTournament(newPlayer);
    }

    @Override
    public List<Player> getPlayerListNotInTournament() {
        return playerpool.getPlayers().stream().filter(player -> !currentTournament.getScoreTable().containsKey(player.getUid())).collect(Collectors.toList());
    }

    @Override
    public SortedSet<PlayerTournamentStatistics> getCurrentTable() {
        return new TreeSet<>(currentTournament.getScoreTable().values());
    }

    @Override
    public List<Match> getMatchesForRound(int roundNo) {
    	List<Match> matches = currentTournament.getMatchesForRound(roundNo);
    	Collections.sort(matches);
        return matches;
    }

    @Override
    public Tournament getCurrentTournament() {
        return currentTournament;
    }

    @Override
    public PlayerPoolService getPlayerpool() {
        return playerpool;
    }
    @Override
    public void changePlayerName(String newName, Player selectedPlayer) {
        playerpool.changePlayerName(newName, selectedPlayer);
        savePlayerPool();

    }
    @Override
    public void exportTournament() {
        persistenceService.exportTournament(currentTournament);
    }
    @Override
    public List<String> createTournamentsListForImport() {
        return persistenceService.createTournamentsListForImport();
    }
    @Override
    public void importAndStartTournament(String tournamentNameToImport) throws IOException {
        currentTournament = persistenceService.importTournament(new File("tournament" + tournamentNameToImport + ".json")); //$NON-NLS-1$ //$NON-NLS-2$
        guiController.switchToolState(ToolState.TOURNAMENT);
        guiController.update();
    }
	@Override
	public boolean isPlayerPausing(Player selectedPlayer) {
		return currentTournament.getScoreTable().get(selectedPlayer.getUid()).isPlayerPausing();
	}
}
