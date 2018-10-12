package zur.koeln.kickertool.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.tournament.data.TournamentMatch;
import zur.koeln.kickertool.tournament.data.TournamentTeam;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Service
public class MatchCreationService {

    private final Random r = new Random();

    /**
     * @param table
     * @param playtables
     * @return
     */
    public void createMatches(List<PlayerTournamentStatistics> table, TournamentSettings config, Round round) {
        table.removeIf(PlayerTournamentStatistics::isPlayerPausing);
        if (round.getRoundNo() <= config.getRandomRounds()) {
            while (table.size() > 3) {
                TournamentTeam home = new TournamentTeam(getRandomPlayer(table), getRandomPlayer(table));
                TournamentTeam visiting = new TournamentTeam(getRandomPlayer(table), getRandomPlayer(table));
                createMatch(config, home, visiting, round);
            }
        } else {
            createRoundByTournamentType(table, config, round);
        }

    }

    private void createRoundByTournamentType(List<PlayerTournamentStatistics> table, TournamentSettings config, Round round) {
        if (config.getMode() == TournamentMode.SWISS_DYP) {
            while (table.size() > 3) {
                TournamentTeam home = new TournamentTeam(table.get(0).getPlayer(), table.get(3).getPlayer());
                TournamentTeam visiting = new TournamentTeam(table.get(1).getPlayer(), table.get(2).getPlayer());
                table.remove(0);
                table.remove(0);
                table.remove(0);
                table.remove(0);
                createMatch(config, home, visiting, round);
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
                TournamentTeam home = new TournamentTeam(playerPairs.get(0).getValue0(), playerPairs.get(3).getValue1());
                TournamentTeam visiting = new TournamentTeam(playerPairs.get(0).getValue1(), playerPairs.get(3).getValue0());
                createMatch(config, home, visiting, round);

                home = new TournamentTeam(playerPairs.get(1).getValue0(), playerPairs.get(2).getValue1());
                visiting = new TournamentTeam(playerPairs.get(1).getValue1(), playerPairs.get(2).getValue0());
                createMatch(config, home, visiting, round);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                TournamentTeam home = new TournamentTeam(playerPairs.get(0).getValue0(), playerPairs.get(1).getValue1());
                TournamentTeam visiting = new TournamentTeam(playerPairs.get(0).getValue1(), playerPairs.get(1).getValue0());
                createMatch(config, home, visiting, round);

            }
        }
    }

    private void createMatch(TournamentSettings config, TournamentTeam home, TournamentTeam visiting, Round round) {
        TournamentMatch m = new TournamentMatch(Integer.valueOf(round.getRoundNo()), home, visiting, config.getCurrentNoOfMatches());
        ((TournamentSettingsImpl) config).setCurrentNoOfMatches(config.getCurrentNoOfMatches() + 1);
        round.addMatch(m);
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
}
