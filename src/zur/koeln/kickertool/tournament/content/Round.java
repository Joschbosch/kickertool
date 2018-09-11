/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import java.util.*;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.tournament.TournamentConfig;
import zur.koeln.kickertool.tournament.TournamentMode;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;

@Component
public class Round {

    @Autowired
    private TournamentFactory tournamentFactory;

    private int roundNo;

    @JsonIgnore
    private final Random r = new Random();

    private final List<Match> matches = new LinkedList<>();

    private final List<Match> completeMatches = new LinkedList<>();

    private Map<UUID, PlayerTournamentStatistics> scoreTableAtEndOfRound;

    /**
     * @param table
     * @param playtables
     * @return
     */
    public void createMatches(List<PlayerTournamentStatistics> table, TournamentConfig config) {
        table.removeIf(statistic -> statistic.getPlayer().isPausingTournament());
        if (roundNo <= config.getRandomRounds()) {
            while (table.size() > 3) {
                Team home = new Team(getRandomPlayer(table), getRandomPlayer(table));
                Team visiting = new Team(getRandomPlayer(table), getRandomPlayer(table));
                createMatch(config, home, visiting);
            }
        } else {
            createRoundByTournamentType(table, config);
        }

    }

    private void createRoundByTournamentType(List<PlayerTournamentStatistics> table, TournamentConfig config) {
        if (config.getMode() == TournamentMode.SWISS_DYP) {
            while (table.size() > 3) {
                Team home = new Team(table.get(0).getPlayer(), table.get(3).getPlayer());
                Team visiting = new Team(table.get(1).getPlayer(), table.get(2).getPlayer());
                table.remove(0);
                table.remove(0);
                table.remove(0);
                table.remove(0);
                createMatch(config, home, visiting);
            }
        } else if (config.getMode() == TournamentMode.SWISS_TUPEL) {
            List<Pair<Player, Player>> playerPairs = new ArrayList<>();
            while (!table.isEmpty()) {
                Pair<Player, Player> newPair = new Pair<>(table.get(0).getPlayer(), table.get(1).getPlayer());
                playerPairs.add(newPair);
                table.remove(0);
                table.remove(0);
            }
            while (playerPairs.size() > 3) {
                Team home = new Team(playerPairs.get(0).getValue0(), playerPairs.get(3).getValue1());
                Team visiting = new Team(playerPairs.get(0).getValue1(), playerPairs.get(3).getValue0());
                createMatch(config, home, visiting);

                home = new Team(playerPairs.get(1).getValue0(), playerPairs.get(2).getValue1());
                visiting = new Team(playerPairs.get(1).getValue1(), playerPairs.get(2).getValue0());
                createMatch(config, home, visiting);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                Team home = new Team(playerPairs.get(0).getValue0(), playerPairs.get(1).getValue1());
                Team visiting = new Team(playerPairs.get(0).getValue1(), playerPairs.get(1).getValue0());
                createMatch(config, home, visiting);

            }
        }
    }

    private void createMatch(TournamentConfig config, Team home, Team visiting) {
        Match m = tournamentFactory.createNewMatch(Integer.valueOf(roundNo), home, visiting, config.getCurrentNoOfMatches());
        config.setCurrentNoOfMatches(config.getCurrentNoOfMatches() + 1);
        matches.add(m);
    }

    /**
     * @param table
     * @return
     */
    private Player getRandomPlayer(List<PlayerTournamentStatistics> table) {
        int random = r.nextInt(table.size());
        Player playerId = table.get(random).getPlayer();
        table.remove(random);
        return playerId;
    }

    public void addMatchResult(Match m) throws MatchException {
        if (matches.contains(m)) {
            matches.remove(m);
            completeMatches.add(m);

        } else {
            throw new MatchException();
        }
    }
    @JsonIgnore
    public boolean isComplete() {
        return matches.isEmpty();
    }
    @JsonIgnore
    public List<Match> getAllMatches() {
        List<Match> allMatches = new LinkedList<>(matches);
        allMatches.addAll(completeMatches);
        return allMatches;
    }

    public TournamentFactory getTournamentFactory() {
        return tournamentFactory;
    }

    public void setTournamentFactory(TournamentFactory tournamentFactory) {
        this.tournamentFactory = tournamentFactory;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    public Map<UUID, PlayerTournamentStatistics> getScoreTableAtEndOfRound() {
        return scoreTableAtEndOfRound;
    }

    public void setScoreTableAtEndOfRound(Map<UUID, PlayerTournamentStatistics> scoreTableAtEndOfRound) {
        this.scoreTableAtEndOfRound = scoreTableAtEndOfRound;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Match> getCompleteMatches() {
        return completeMatches;
    }

}
