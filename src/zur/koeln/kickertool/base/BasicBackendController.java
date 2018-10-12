/**
 * 
 */
package zur.koeln.kickertool.base;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.PersistenceService;
import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.config.TournamentSettingsKeys;
import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.tournament.TournamentService;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
public class BasicBackendController
    implements BackendController {

    private final Logger logger = LogManager.getLogger(BackendController.class);

    private final PlayerPoolService playerpool;
    
    private final PersistenceService persistenceService;

    private final TournamentService tournamentService;

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
        return tournamentService.createNewTournament(text);
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
    public void changedTournamentConfig(TournamentSettingsKeys configKey, Integer newValue) {
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
    public void startTournament() {
        tournamentService.startTournament();
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
            logger.error(String.format("Error updating match result for match %s", String.valueOf(match.getMatchNo())), e);
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
        tournamentService.setCurrentTournament(persistenceService.importTournament(tournamentNameToImport));
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
