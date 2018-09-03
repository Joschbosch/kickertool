/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.player.PlayerPool;

@RequiredArgsConstructor
@Getter
@Setter
public class Round {

    private int roundNo;

    @JsonIgnore
    private Random r = new Random();

    private List<Match> matches = new LinkedList<>();

    private List<Match> completeMatches = new LinkedList<>();

    @JsonIgnore
    private Map<Player, TournamentStatistics> scoreTableAtEndOfRound;

    /**
     * @param table
     * @param playtables
     * @return
     */
    public void createMatches(List<TournamentStatistics> table, TournamentConfiguration config) {
        table.removeIf(statistic -> PlayerPool.getInstance().getPlayerById(statistic.getPlayerId()).isPausingTournament());
        while (table.size() > 3) {
            Team home;
            Team visiting;
            if (roundNo <= config.getRandomRounds()) {
                home = new Team(getRandomPlayer(table), getRandomPlayer(table));
                visiting = new Team(getRandomPlayer(table), getRandomPlayer(table));
            } else {
                home = new Team(table.get(0).getPlayerId(), table.get(3).getPlayerId());
                visiting = new Team(table.get(1).getPlayerId(), table.get(2).getPlayerId());
                table.remove(0);
                table.remove(0);
                table.remove(0);
                table.remove(0);
            }

            Match m = new Match(Integer.valueOf(roundNo), home, visiting);

            matches.add(m);
        }
    }

    /**
     * @param table
     * @return
     */
    private UUID getRandomPlayer(List<TournamentStatistics> table) {
        int random = r.nextInt(table.size());
        UUID playerId = table.get(random).getPlayerId();
        table.remove(random);
        return playerId;
    }

    public boolean isComplete() {
        return matches.isEmpty();
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
    public List<Match> getAllMatches() {
        List<Match> allMatches = new LinkedList<>(matches);
        allMatches.addAll(completeMatches);
        return allMatches;
    }

}
