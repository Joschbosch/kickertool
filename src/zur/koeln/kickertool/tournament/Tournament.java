package zur.koeln.kickertool.tournament;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;

@RequiredArgsConstructor
@Getter
@Setter
public class Tournament {

    private final TournamentConfiguration config = new TournamentConfiguration();

    private String name;

    private boolean started = false;

    private Map<UUID, Player> participants = new HashMap<>();

    private Map<Integer, Round> completeRounds = new HashMap<>();

    private Map<Integer, Round> ongoingRounds = new HashMap<>();

    private List<Match> completeMatches = new LinkedList<>();

    private List<Match> ongoingMatches = new LinkedList<>();
    @JsonIgnore
    private Map<Player, TournamentStatistics> table = new HashMap<>();

    private List<GamingTable> playtables = new LinkedList<>();

    private List<Player> dummyPlayer = new LinkedList<>();

    private List<Player> dummyPlayerUnused = new LinkedList<>();

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
            if (!participants.containsKey(p.getUid())) {
                participants.put(p.getUid(), p);
                p.setPausingTournament(false);
            }
        } else {
            if (!table.containsKey(p)) {
                table.put(p, new TournamentStatistics(p));
                checkDummies();
            }
        }
    }

    public void removeParticipant(Player p) {
        if (!started) {
            participants.remove(p.getUid());
        } else {
            table.remove(p);
            checkDummies();
        }

    }

    public void pausePlayer(Player p) {
        p.setPausingTournament(true);
        checkDummies();
    }

    public void unpausePlayer(Player p) {
        p.setPausingTournament(false);
        checkDummies();
    }

    /**
     * 
     */
    private void checkDummies() {
        int usedDummies = dummyPlayer.size();
        int neededDummies = getActivePlayerCount() % 4 == 0 ? 0 : 4 - getActivePlayerCount() % 4;
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                Player removeDummy = dummyPlayer.remove(dummyPlayer.size() - 1);
                table.remove(removeDummy);
                dummyPlayerUnused.add(removeDummy);
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                if (dummyPlayerUnused.isEmpty()) {
                    createDummyPlayer(i);
                } else {
                    Player dummy = dummyPlayerUnused.remove(0);
                    table.put(dummy, new TournamentStatistics(dummy));
                    dummyPlayer.add(dummy);
                }

            }
        }
    }

    /**
     * @return
     */
    private int getActivePlayerCount() {
        int count = 0;
        for (TournamentStatistics ts : table.values()) {
            if (!ts.getPlayer().isPausingTournament() && !ts.getPlayer().isDummy()) {
                count++;
            }
        }
        return count;
    }

    public void startTournament() {
        if (!started) {
            for (int i = 1; i <= config.getTableCount(); i++) {
                playtables.add(new GamingTable(i));
            }
            for (Player p : participants.values()) {
                table.put(p, new TournamentStatistics(p));
            }
            checkDummies();
        }
        started = true;
    }

    /**
     * @param i
     */
    private void createDummyPlayer(int i) {
        Player dummy = new Player("Dummy Player " + i);
        dummy.setDummy(true);
        table.put(dummy, new TournamentStatistics(dummy));
        dummyPlayer.add(dummy);
    }

    public Round newRound() {
        Round r = new Round(completeRounds.size() + ongoingRounds.size() + 1);
        ongoingRounds.put(Integer.valueOf(r.getRoundNo()), r);
        ongoingMatches.addAll(r.createMatches(getTableCopySortedByPoints(), playtables, config));
        return r;
    }

    public void addMatchResult(Match m) throws MatchException {
        if (ongoingMatches.contains(m)) {
            m.getTable().setInUse(false);
            ongoingMatches.remove(m);
            completeMatches.add(m);

            table.get(m.getHomeTeam().getP1()).addMatchResult(m, config);
            table.get(m.getHomeTeam().getP2()).addMatchResult(m, config);
            table.get(m.getVisitingTeam().getP1()).addMatchResult(m, config);
            table.get(m.getVisitingTeam().getP2()).addMatchResult(m, config);
        } else {
            throw new MatchException();
        }

        for (Match ongoing : ongoingMatches) {
            if (ongoing.getTable() == null) {
                if (m.getTable().isActive() && !m.getTable().isInUse()) {
                    ongoing.setTable(m.getTable());
                    m.getTable().setInUse(true);
                }
            }
        }
    }

    public List<TournamentStatistics> getTableCopySortedByPoints() {
        List<TournamentStatistics> sorting = new LinkedList<>(table.values());
        Collections.sort(sorting, new Comparator<TournamentStatistics>() {
            @Override
            public int compare(TournamentStatistics o1, TournamentStatistics o2) {
                if (o1.getPlayer().isDummy()) {
                    return 1;
                }
                if (o2.getPlayer().isDummy()) {
                    return -1;
                }
                if (o1.getPoints() < o2.getPoints()) {
                    return 1;
                } else if (o1.getPoints() > o2.getPoints()) {
                    return -1;
                }
                if (o1.getGoalDiff() < o2.getGoalDiff()) {
                    return 1;
                }
                if (o1.getGoalDiff() > o2.getGoalDiff()) {
                    return -1;
                }

                return o1.getPlayer().getName().compareTo(o2.getPlayer().getName());
            }
        });

        return sorting;
    }

    public List<Match> getMatches() {
        List<Match> result = new LinkedList<>(completeMatches);
        result.addAll(ongoingMatches);
        Collections.sort(result, new Comparator<Match>() {

            @Override
            public int compare(Match o1, Match o2) {
                return o1.getMatchNo() - o2.getMatchNo();
            }
        });

        return result;
    }

    public void printTable() {
        System.out.println(String.format("\n%-20s\t%s\t%s\t%s\t%s\t%s\t%s", "Name", "Matches", "Win", "Loss", "Draw",
                "GoalDiff", "Points"));
        System.out.println(getTableCopySortedByPoints());
    }

    /**
     * 
     */
    public void printMatches() {
        System.out.println(getMatches());
    }
}
