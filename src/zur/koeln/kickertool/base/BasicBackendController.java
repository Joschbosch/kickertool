/**
 * 
 */
package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
import zur.koeln.kickertool.tournament.TournamentService;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
public class BasicBackendController
    implements BackendController {

    private final PlayerPoolService playerpool;
    
    private final PersistenceService persistenceService;

    private final TournamentService tournamentService;

    private GUIController guiController;


    public BasicBackendController(
        PlayerPoolService playerpool,
        PersistenceService persistenceService,
        TournamentService tournamentService) {
        this.playerpool = playerpool;
        this.persistenceService = persistenceService;
        this.tournamentService = tournamentService;
    }

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
        Tournament newTournament = tournamentService.createNewTournament(text);
        guiController.switchToolState(ToolState.NEW_TOURNAMENT_CONFIG);
        return newTournament;
    }
    @Override
    public boolean isCurrentRoundComplete() {
        return tournamentService.isCurrentRoundComplete();
    }

    /**
     * @param configKey
     * @param newValue
     */
    @Override
    public void changedTournamentConfig(TournamentSetingsKeys configKey, Integer newValue) {
        switch (configKey) {
            case TABLES :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setTableCount(newValue.intValue());
                break;
            case MATCHES_TO_WIN :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setMatchesToWin(newValue.intValue());
                break;
            case GOALS_FOR_WIN :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setGoalsToWin(newValue.intValue());
                break;
            case POINTS_FOR_WINNER :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setPointsForWinner(newValue.intValue());
                break;
            case POINTS_FOR_DRAW :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setPointsForDraw(newValue.intValue());
                break;
            case MINUTES_PER_MATCH :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setMinutesPerMatch(newValue.intValue());
                break;
            case RANDOM_ROUNDS :
                ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setRandomRounds(newValue.intValue());
                break;
            default :
                break;
        }

    }
    @Override
    public void changeMode(TournamentMode newMode) {
        ((TournamentSettingsImpl) tournamentService.getCurrentTournament().getSettings()).setMode(newMode);
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
        tournamentService.startTournament();
        guiController.switchToolState(ToolState.TOURNAMENT);
    }


    /**
     * @param p
     */
    @Override
    public void addParticipantToTournament(Player p) {
        tournamentService.addParticipant(p);
    }

    /**
     * @param p
     */
    @Override
    public void removeParticipantFromTournament(Player p) {
        tournamentService.removeParticipant(p);
    }

    /**
     * @return
     */
    @Override
    public Collection<Player> getParticipantList() {
        Set<Player> participants = new HashSet<>();
        tournamentService.getCurrentTournament().getParticipants().forEach(id -> participants.add(playerpool.getPlayerOrDummyById(id)));
        return participants;
    }

    /**
     * 
     */
    @Override
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
    @Override
    public void updateMatchResult(zur.koeln.kickertool.api.tournament.Match match, Integer scoreHome, Integer scoreVisiting) {
        try {
            if (match.getResult() == null) {
                match.setResultScores(scoreHome.intValue(), scoreVisiting.intValue());
                tournamentService.addMatchResult(match);
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
        tournamentService.pausePlayer(playerpool.getPlayerOrDummyById(selectedPlayer));

    }
    @Override
    public void unpausePlayer(UUID selectedPlayer) {
        tournamentService.unpausePlayer(playerpool.getPlayerOrDummyById(selectedPlayer));
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
        return playerpool.getPlayers().stream().filter(player -> !tournamentService.getCurrentTournament().getScoreTable().containsKey(player.getUid())).collect(Collectors.toList());
    }

    @Override
    public SortedSet<PlayerTournamentStatistics> getCurrentTable() {

        return tournamentService.getCurrentTableAsSet();
    }

    @Override
    public List<Match> getMatchesForRound(int roundNo) {
        List<Match> matches = tournamentService.getMatchesForRound(roundNo);
    	Collections.sort(matches);
        return matches;
    }

    @Override
    public Tournament getCurrentTournament() {
        return tournamentService.getCurrentTournament();
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
        persistenceService.exportTournament(tournamentService.getCurrentTournament());
    }
    @Override
    public List<String> createTournamentsListForImport() {
        return persistenceService.createTournamentsListForImport();
    }
    @Override
    public void importAndStartTournament(String tournamentNameToImport) throws IOException {
        tournamentService.setCurrentTournament(persistenceService.importTournament(new File("tournament" + tournamentNameToImport + ".json"))); //$NON-NLS-1$ //$NON-NLS-2$
        guiController.switchToolState(ToolState.TOURNAMENT);
        guiController.update();
    }
	@Override
	public boolean isPlayerPausing(Player selectedPlayer) {
        return tournamentService.getCurrentTournament().getScoreTable().get(selectedPlayer.getUid()).isPlayerPausing();
	}

    @Override
    public void init() {
        playerpool.loadPlayerPool();
    }
}
