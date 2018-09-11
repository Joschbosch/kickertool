package zur.koeln.kickertool.tournament.content;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.base.PlayerPoolService;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.tournament.TournamentConfig;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;

@Getter
@Setter
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

    private TournamentConfig config;

    private String name;

    private List<UUID> participants = new ArrayList<>();

    private List<Round> completeRounds = new ArrayList<>();

    private Round currentRound;

    private Map<UUID, TournamentStatistics> scoreTable = new HashMap<>();

    private Map<Integer, GamingTable> playtables = new HashMap<>();

    private List<UUID> dummyPlayerActive = new ArrayList<>();

    public Tournament() {
        this.config = new TournamentConfig();
    }
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
        for (TournamentStatistics ts : scoreTable.values()) {
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
                Map<UUID, TournamentStatistics> scoreTableClone = new HashMap<>();
                scoreTable.forEach((key, value) -> {
                    TournamentStatistics clonedStatistics = tournamentFactory.createNewTournamentStatistics(key);
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
    public List<TournamentStatistics> getHistoricTableCopySortedByPoints(int roundNo) {
        Collection<TournamentStatistics> tableToSort = scoreTable.values();
        
        if (currentRound.getRoundNo() != roundNo) {
            for (Round completeRound : completeRounds) {
                if (completeRound.getRoundNo() == roundNo && completeRound.getScoreTableAtEndOfRound() != null) {
                    tableToSort = completeRound.getScoreTableAtEndOfRound().values();
                    break;
                }
            }
        }

        List<TournamentStatistics> sorting = new LinkedList<>(tableToSort);
        Collections.sort(sorting, (o1, o2) -> {

            Player player1 = playerPool.getPlayerById(o1.getPlayerId());
            Player player2 = playerPool.getPlayerById(o2.getPlayerId());
            if (player1 == null) {
                return 1;
            }
            if (player2 == null) {
                return -1;
            }
            if (player1.isDummy()) {
                return 1;
            }
            if (player2.isDummy()) {
                return -1;
            }
            long pointsForConfiguration = o1.getPointsForConfiguration(config);
            long pointsForConfiguration2 = o2.getPointsForConfiguration(config);
            if (pointsForConfiguration < pointsForConfiguration2) {
                return 1;
            } else if (pointsForConfiguration > pointsForConfiguration2) {
                return -1;
            }
            int goalDiff = o1.getGoalDiff();
            int goalDiff2 = o2.getGoalDiff();

            if (goalDiff < goalDiff2) {
                return 1;
            }
            if (goalDiff > goalDiff2) {
                return -1;
            }

            return player1.getName().compareTo(player2.getName());
        });

        return sorting;
    }

    @JsonIgnore
    public List<TournamentStatistics> getCurrentTableCopySortedByPoints() {
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

}
