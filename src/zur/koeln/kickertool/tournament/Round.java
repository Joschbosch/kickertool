/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.player.Player;

@RequiredArgsConstructor
@Getter
@Setter
public class Round {

    private final int roundNo;

    @JsonIgnore
    private Random r = new Random();

    private List<Match> matches = new LinkedList<>();

    /**
     * @param table
     * @param playtables
     * @return
     */
    public List<Match> createMatches(List<TournamentStatistics> table, List<GamingTable> playtables,
            TournamentConfiguration config) {
        table.removeIf(statistic -> statistic.getPlayer().isPausingTournament());
        while (table.size() > 3) {
            Team home;
            Team visiting;
            if (roundNo <= config.getRandomRounds()) {
                home = new Team(getRandomPlayer(table), getRandomPlayer(table));
                visiting = new Team(getRandomPlayer(table), getRandomPlayer(table));
            } else {
                home = new Team(table.get(0).getPlayer(), table.get(3).getPlayer());
                visiting = new Team(table.get(1).getPlayer(), table.get(2).getPlayer());
                table.remove(0);
                table.remove(0);
                table.remove(0);
                table.remove(0);
            }

            Match m = new Match(Integer.valueOf(roundNo), home, visiting);

            for (GamingTable playtable : playtables) {
                if (playtable.isActive() && !playtable.isInUse()) {
                    m.setTable(playtable);
                    playtable.setInUse(true);
                    break;
                }
            }
            matches.add(m);
        }
        return matches;
    }

    /**
     * @param table
     * @return
     */
    private Player getRandomPlayer(List<TournamentStatistics> table) {
        int random = r.nextInt(table.size());
        Player player = table.get(random).getPlayer();
        table.remove(random);
        return player;
    }

}
