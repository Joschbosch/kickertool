package zur.koeln.kickertool.core.logic.deprecated;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import zur.koeln.kickertool.core.kernl.TournamentMode;
import zur.koeln.kickertool.core.model.*;

public class RoundService {

    @Autowired
    private MatchService matchService;

    private final Random r = new Random();

    /**
     * @param newRound 
     * @param table
     * @param playtables
     * @return
     */
    public void createMatches(Round newRound, List<PlayerStatistics> table, Settings config) {
        table.removeIf(PlayerStatistics::isPlayerPausing);
        if (newRound.getRoundNo() <= config.getRandomRounds()) {
            while (table.size() > 3) {
                Team home = new Team(getRandomPlayer(table), getRandomPlayer(table));
                Team visiting = new Team(getRandomPlayer(table), getRandomPlayer(table));
                createMatch(newRound, config, home, visiting);
            }
        } else {
            createRoundByTournamentType(newRound, table, config);
        }

    }


    private void createRoundByTournamentType(Round newRound, List<PlayerStatistics> table, Settings config) {
        if (config.getMode() == TournamentMode.SWISS_DYP) {
            while (table.size() > 3) {
                Team home = new Team(table.get(0).getPlayer(), table.get(3).getPlayer());
                Team visiting = new Team(table.get(1).getPlayer(), table.get(2).getPlayer());
                table.remove(0);
                table.remove(0);
                table.remove(0);
                table.remove(0);
                createMatch(newRound, config, home, visiting);
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
                createMatch(newRound, config, home, visiting);

                home = new Team(playerPairs.get(1).getValue0(), playerPairs.get(2).getValue1());
                visiting = new Team(playerPairs.get(1).getValue1(), playerPairs.get(2).getValue0());
                createMatch(newRound, config, home, visiting);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                Team home = new Team(playerPairs.get(0).getValue0(), playerPairs.get(1).getValue1());
                Team visiting = new Team(playerPairs.get(0).getValue1(), playerPairs.get(1).getValue0());
                createMatch(newRound, config, home, visiting);

            }
        }
    }

    private void createMatch(Round newRound, Settings config, Team home, Team visiting) {
        Match m = matchService.createNewMatch(Integer.valueOf(newRound.getRoundNo()), home, visiting, config.getCurrentNoOfMatches());
        config.setCurrentNoOfMatches(config.getCurrentNoOfMatches() + 1);
        newRound.getMatches().add(m);
    }

    /**
     * @param table
     * @return
     */
    private Player getRandomPlayer(List<PlayerStatistics> table) {
        int random = r.nextInt(table.size());
        Player playerId = table.get(random).getPlayer();
        table.remove(random);
        return playerId;
    }

    public void addMatchResult(Match m, Round round) {
        if (round.getMatches().contains(m)) {
            round.getMatches().remove(m);
            round.getCompleteMatches().add(m);

        }
    }

    public Round createNewRound(int nextRoundNumber) {
        Round bean = new Round();
        bean.setRoundNo(nextRoundNumber);
        return bean;
    }


}
