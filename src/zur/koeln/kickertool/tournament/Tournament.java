package zur.koeln.kickertool.tournament;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.player.PlayerPool;

@RequiredArgsConstructor
@Getter
@Setter
public class Tournament {

    private final TournamentConfiguration config = new TournamentConfiguration();

    private String name;

    @JsonIgnore
    private boolean started = false;

    private List<UUID> participants = new ArrayList<>();

    private List<Round> completeRounds = new ArrayList<>();

    private Round currentRound;

    private Map<UUID, TournamentStatistics> scoreTable = new HashMap<>();

    private Map<Integer, GamingTable> playtables = new HashMap<>();

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
                scoreTable.put(p.getUid(), new TournamentStatistics(p.getUid()));
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
        PlayerPool.getInstance().getPlayerById(selectedPlayer).setPausingTournament(true);
        checkDummies();
    }

    public void unpausePlayer(UUID selectedPlayer) {
        PlayerPool.getInstance().getPlayerById(selectedPlayer).setPausingTournament(false);
        checkDummies();
    }

    /**
     * 
     */
    private void checkDummies() {
        int usedDummies = PlayerPool.getInstance().getDummyPlayerUsed();
        int neededDummies = getActivePlayerCount() % 4 == 0 ? 0 : 4 - getActivePlayerCount() % 4;
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                UUID removeDummy = PlayerPool.getInstance().removeLastDummy();
                scoreTable.remove(removeDummy);

            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                UUID dummy = PlayerPool.getInstance().useNextDummyPlayer();
                scoreTable.put(dummy, new TournamentStatistics(dummy));
            }
        }
    }

    /**
     * @return
     */
    private int getActivePlayerCount() {
        int count = 0;
        for (TournamentStatistics ts : scoreTable.values()) {
            Player player = PlayerPool.getInstance().getPlayerById(ts.getPlayerId());
            if (!player.isPausingTournament() && !player.isDummy()) {
                count++;
            }
        }
        return count;
    }

    public void exportTournament() {
        File tournamentFile = new File("tournament.json"); //$NON-NLS-1$
        ObjectMapper m = new ObjectMapper();
        try {
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentFile, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTournament() {
        if (!started) {
            for (int i = 1; i <= config.getTableCount(); i++) {
                playtables.put(Integer.valueOf(i), new GamingTable(i));
            }
            for (UUID pid : participants) {
                scoreTable.put(pid, new TournamentStatistics(pid));
            }
            checkDummies();
        }
        started = true;
    }

    public Round newRound() {

        if (isCurrentRoundComplete()) {
            int nextRoundNumber = 1;
            if (currentRound != null) {
                currentRound.setScoreTableAtEndOfRound(null);
                completeRounds.add(currentRound);
                nextRoundNumber = currentRound.getRoundNo() + 1;
            }
            Round newRound = new Round();
            newRound.setRoundNo(nextRoundNumber);
            currentRound = newRound;
            newRound.createMatches(getTableCopySortedByPoints(), config);
            updatePlayTableUsage();
            return newRound;
        }
        return null;
    }

    public void addMatchResult(Match m) throws MatchException {
        currentRound.addMatchResult(m);
        playtables.get(m.getTableNo()).setInUse(false);
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
    public List<TournamentStatistics> getTableCopySortedByPoints() {
        List<TournamentStatistics> sorting = new LinkedList<>(scoreTable.values());
        Collections.sort(sorting, (o1, o2) -> {

            Player player1 = PlayerPool.getInstance().getPlayerById(o1.getPlayerId());
            Player player2 = PlayerPool.getInstance().getPlayerById(o2.getPlayerId());
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
            long pointsForConfiguration = o1.calcPointsForConfiguration(config);
            long pointsForConfiguration2 = o2.calcPointsForConfiguration(config);
            if (pointsForConfiguration < pointsForConfiguration2) {
                return 1;
            } else if (pointsForConfiguration > pointsForConfiguration2) {
                return -1;
            }
            int goalDiff = o1.calcGoalDiff();
            int goalDiff2 = o2.calcGoalDiff();

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
    public List<Match> getAllMatches() {

        List<Match> result = new LinkedList<>(currentRound.getAllMatches());
        completeRounds.forEach(r -> result.addAll(r.getAllMatches()));
        Collections.sort(result, (o1, o2) -> o1.getMatchNo() - o2.getMatchNo());

        return result;
    }

    public void printTable() {
        System.out
            .println(String.format("%n%-20s\t%s\t%s\t%s\t%s\t%s\t%s", "Name", "Matches", "Win", "Loss", "Draw", "GoalDiff", "Points"));
        System.out.println(getTableCopySortedByPoints());
    }

    /**
     * 
     */
    public void printMatches() {
        System.out.println(getAllMatches());
    }

    @JsonIgnore
    public boolean isCurrentRoundComplete() {
        return currentRound != null ? currentRound.isComplete() : true;
    }

}
