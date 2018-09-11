package zur.koeln.kickertool.tournament.content;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.base.PlayerPoolService;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.tournament.TournamentConfig;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;

@Component
public class Tournament {

    @JsonIgnore
    @Autowired
    private PlayerPoolService playerPool;

    @JsonIgnore
    @Autowired
    private TournamentFactory tournamentFactory;

    @JsonIgnore
    private boolean started = false;

    @Autowired
    private TournamentConfig config;

    private String name;

    private final List<UUID> participants = new ArrayList<>();

    private final List<Round> completeRounds = new ArrayList<>();

    private Round currentRound;

    private final Map<UUID, PlayerTournamentStatistics> scoreTable = new HashMap<>();

    private final Map<Integer, GamingTable> playtables = new HashMap<>();

    private final List<UUID> dummyPlayerActive = new ArrayList<>();

    /**
     * @param players
     */
    public void addParticipants(List<Player> players) {
        for (Player p : players) {
            addParticipant(p);
        }
    }

    public void addParticipant(Player p) {
        if (!started) {
            if (!participants.contains(p.getUid())) {
                participants.add(p.getUid());
                p.setPausingTournament(false);
            }
        } else {
            if (!scoreTable.containsKey(p.getUid())) {
                scoreTable.put(p.getUid(), tournamentFactory.createNewTournamentStatistics(p.getUid()));
                checkDummies();
            }
        }
    }

    public void removeParticipant(Player p) {
        if (!started) {
            participants.remove(p.getUid());
        } else {
            scoreTable.remove(p.getUid());
            checkDummies();
        }

    }

    public void pausePlayer(UUID selectedPlayer) {
        playerPool.getPlayerById(selectedPlayer).setPausingTournament(true);
        checkDummies();
    }

    public void unpausePlayer(UUID selectedPlayer) {
        playerPool.getPlayerById(selectedPlayer).setPausingTournament(false);
        checkDummies();
    }

    /**
     * 
     */
    private void checkDummies() {
        int usedDummies = playerPool.getNoOfDummyPlayerUsed();
        int neededDummies = getActivePlayerCount() % 4 == 0 ? 0 : 4 - getActivePlayerCount() % 4;
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                UUID removeDummy = playerPool.removeLastDummy();
                scoreTable.remove(removeDummy);
                dummyPlayerActive.remove(removeDummy);
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                UUID dummy = playerPool.useNextDummyPlayer();
                scoreTable.put(dummy, tournamentFactory.createNewTournamentStatistics(dummy));
                dummyPlayerActive.add(dummy);
            }
        }
    }

    /**
     * @return
     */
    private int getActivePlayerCount() {
        int count = 0;
        for (PlayerTournamentStatistics ts : scoreTable.values()) {
            Player player = playerPool.getPlayerById(ts.getPlayerId());
            if (!player.isPausingTournament() && !player.isDummy()) {
                count++;
            }
        }
        return count;
    }

    public void startTournament() {
        if (!started) {
            for (int i = 1; i <= config.getTableCount(); i++) {
                playtables.put(Integer.valueOf(i), new GamingTable(i));
            }
            for (UUID pid : participants) {
                scoreTable.put(pid, tournamentFactory.createNewTournamentStatistics(pid));
            }
            checkDummies();
        }
        started = true;
    }

    public Round newRound() {

        if (isCurrentRoundComplete()) {
            int nextRoundNumber = 1;
            if (currentRound != null) {
                Map<UUID, PlayerTournamentStatistics> scoreTableClone = new HashMap<>();
                scoreTable.forEach((key, value) -> {
                    PlayerTournamentStatistics clonedStatistics = tournamentFactory.createNewTournamentStatistics(key);
                    value.getClone(clonedStatistics);
                    scoreTableClone.put(key, clonedStatistics);
                });
                currentRound.setScoreTableAtEndOfRound(scoreTableClone);
                completeRounds.add(currentRound);
                nextRoundNumber = currentRound.getRoundNo() + 1;
            }
            Round newRound = tournamentFactory.createNewRound(nextRoundNumber);
            currentRound = newRound;
            newRound.createMatches(getCurrentTableCopySortedByPoints(), config);
            updatePlayTableUsage();
            return newRound;
        }
        return null;
    }

    public void addMatchResult(Match m) throws MatchException {
        currentRound.addMatchResult(m);
        playtables.get(Integer.valueOf(m.getTableNo())).setInUse(false);
        updatePlayTableUsage();

        scoreTable.get(m.getHomeTeam().getP1()).addMatchResult(m);
        scoreTable.get(m.getHomeTeam().getP2()).addMatchResult(m);
        scoreTable.get(m.getVisitingTeam().getP1()).addMatchResult(m);
        scoreTable.get(m.getVisitingTeam().getP2()).addMatchResult(m);
    }

    private void updatePlayTableUsage() {
        for (Match ongoing : currentRound.getMatches()) {
            if (ongoing.getTableNo() == -1) {
                for (GamingTable gameTable : playtables.values()) {
                    if (!gameTable.isInUse() && gameTable.isActive()) {
                        ongoing.setTableNo(gameTable.getTableNumber());
                        gameTable.setInUse(true);
                        break;
                    }
                }
            }
        }
    }
    @JsonIgnore
    public List<PlayerTournamentStatistics> getHistoricTableCopySortedByPoints(int roundNo) {
        Collection<PlayerTournamentStatistics> tableToSort = scoreTable.values();

        if (currentRound.getRoundNo() != roundNo) {
            for (Round completeRound : completeRounds) {
                if (completeRound.getRoundNo() == roundNo && completeRound.getScoreTableAtEndOfRound() != null) {
                    tableToSort = completeRound.getScoreTableAtEndOfRound().values();
                    break;
                }
            }
        }

        List<PlayerTournamentStatistics> sorting = new LinkedList<>(tableToSort);
        Collections.sort(sorting);
        return sorting;
    }

    @JsonIgnore
    public List<PlayerTournamentStatistics> getCurrentTableCopySortedByPoints() {
        return getHistoricTableCopySortedByPoints(currentRound.getRoundNo());
    }

    @JsonIgnore
    public List<Match> getAllMatches() {

        List<Match> result = new LinkedList<>(currentRound.getAllMatches());
        completeRounds.forEach(r -> result.addAll(r.getAllMatches()));
        Collections.sort(result, (o1, o2) -> o1.getMatchNo() - o2.getMatchNo());

        return result;
    }

    @JsonIgnore
    public boolean isCurrentRoundComplete() {
        return currentRound != null ? currentRound.isComplete() : Boolean.TRUE.booleanValue();
    }

    @JsonIgnore
    public void initializeAfterImport() {
        started = true;
        Map<UUID, Match> uuidToMatch = new HashMap<>();
        getAllMatches().forEach(m -> uuidToMatch.put(m.getMatchID(), m));
        scoreTable.values().forEach(tournamentStatistic -> {
            tournamentStatistic.setUidToMatch(uuidToMatch);
            if (dummyPlayerActive.contains(tournamentStatistic.getPlayerId())) {
                playerPool.createDummyPlayerWithUUID(tournamentStatistic.getPlayerId());
            }
        });

    }

    public void setPlayerPool(PlayerPoolService playerPool) {
        this.playerPool = playerPool;
    }

    public void setTournamentFactory(TournamentFactory tournamentFactory) {
        this.tournamentFactory = tournamentFactory;
    }
    public TournamentConfig getConfig() {
        return config;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
    public Round getCurrentRound() {
        return currentRound;
    }
    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }
    public PlayerPoolService getPlayerPool() {
        return playerPool;
    }
    public TournamentFactory getTournamentFactory() {
        return tournamentFactory;
    }
    public String getName() {
        return name;
    }
    public List<UUID> getParticipants() {
        return participants;
    }
    public List<Round> getCompleteRounds() {
        return completeRounds;
    }
    public Map<UUID, PlayerTournamentStatistics> getScoreTable() {
        return scoreTable;
    }
    public Map<Integer, GamingTable> getPlaytables() {
        return playtables;
    }
    public List<UUID> getDummyPlayerActive() {
        return dummyPlayerActive;
    }

}
