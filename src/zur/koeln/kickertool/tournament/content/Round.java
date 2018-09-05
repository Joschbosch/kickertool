/**
 * 
 */
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
public class Round {

    @JsonIgnore
    @Autowired
    private PlayerPoolService playerPool;

    @Autowired
    private TournamentFactory tournamentFactory;

    private int roundNo;

    @JsonIgnore
    private Random r = new Random();

    private List<Match> matches = new LinkedList<>();

    private List<Match> completeMatches = new LinkedList<>();

    private Map<Player, TournamentStatistics> scoreTableAtEndOfRound;

    /**
     * @param table
     * @param playtables
     * @return
     */
    public void createMatches(List<TournamentStatistics> table, TournamentConfig config) {
        table.removeIf(statistic -> playerPool.getPlayerById(statistic.getPlayerId()).isPausingTournament());
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

            Match m = tournamentFactory.createNewMatch(Integer.valueOf(roundNo), home, visiting, config.getCurrentNoOfMatches());
            m.setPlayerPool(playerPool);
            config.setCurrentNoOfMatches(config.getCurrentNoOfMatches() + 1);

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

}
