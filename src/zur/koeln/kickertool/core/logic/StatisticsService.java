package zur.koeln.kickertool.core.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.entities.Player;
import zur.koeln.kickertool.core.entities.PlayerStatistics;
import zur.koeln.kickertool.core.entities.Settings;

@Service
public class StatisticsService {

    @Autowired
    private MatchService matchService;

    public long getPointsForConfiguration(PlayerStatistics playerStats, Settings settings) {
        if (playerStats.getPlayer().isDummy()) {
            return 0;
        }

        long won = getMatchesWonCount(playerStats);
        long draw = getMatchesDrawCount(playerStats);

        return won * settings.getPointsForWinner() + draw * settings.getPointsForDraw();

    }

    public double getMeanPoints(PlayerStatistics playerStats, Settings config) {
        return getPointsForConfiguration(playerStats, config) / (double) playerStats.getMatches().size();
    }

    public int compareStatistics(PlayerStatistics stat1, PlayerStatistics stat2, Settings config) {
        Player player1 = stat1.getPlayer();
        Player player2 = stat2.getPlayer();
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

        long pointsForConfiguration = getPointsForConfiguration(stat1, config) - getPointsForConfiguration(stat2, config);
        if (pointsForConfiguration != 0) {
            return -Long.compare(getPointsForConfiguration(stat1, config), getPointsForConfiguration(stat2, config));
        }

        int goalDiff = getGoalDiff(stat1) - getGoalDiff(stat2);
        if (goalDiff != 0) {
            return -Integer.compare(getGoalDiff(stat1), getGoalDiff(stat2));
        }

        int wonDiff = (int) (getMatchesWonCount(stat1) - getMatchesWonCount(stat2));
        if (wonDiff != 0) {
            return -Long.compare(getMatchesWonCount(stat1), getMatchesWonCount(stat2));
        }

        int drawDiff = (int) (getMatchesDrawCount(stat1) - getMatchesDrawCount(stat2));
        if (drawDiff != 0) {
            return -Long.compare(getMatchesDrawCount(stat1), getMatchesDrawCount(stat2));
        }
        int nameCompare = player1.getName().compareTo(player2.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }
        return player1.getUid().compareTo(player2.getUid());
    }

    public void getClone(PlayerStatistics statsToClone, PlayerStatistics tournamentStatistics) {
        tournamentStatistics.getMatches().addAll(statsToClone.getMatches());
        tournamentStatistics.setUidToMatch(statsToClone.getUidToMatch());
        tournamentStatistics.setPlayer(statsToClone.getPlayer());
    }
    public int getMatchesDone(PlayerStatistics playerStats) {
        return playerStats.getMatches().size();
    }

    public int getGoalsShot(PlayerStatistics playerStats) {
        return playerStats.getMatches().stream().mapToInt(m -> matchService.getGoalsForPlayerInMatch(playerStats.getPlayerId(), playerStats.getUidToMatch().get(m))).sum();
    }

    public int getGoalsConceded(PlayerStatistics playerStats) {
        return playerStats.getMatches().stream().mapToInt(m -> matchService.getConcededGoalsForPlayer(playerStats.getPlayerId(), playerStats.getUidToMatch().get(m))).sum();
    }

    public int getGoalDiff(PlayerStatistics playerStats) {
        return getGoalsShot(playerStats) - getGoalsConceded(playerStats);
    }

    public long getMatchesWonCount(PlayerStatistics playerStats) {
        return playerStats.getMatches().stream().filter(m -> matchService.didPlayerWin(playerStats.getPlayerId(), playerStats.getUidToMatch().get(m))).count();
    }

    public long getMatchesLostCount(PlayerStatistics playerStats) {
        return playerStats.getMatches().stream()
                .filter(m -> !matchService.didPlayerWin(playerStats.getPlayerId(), playerStats.getUidToMatch().get(m)) && !matchService.isDraw(playerStats.getUidToMatch().get(m)))
                .count();
    }

    public long getMatchesDrawCount(PlayerStatistics playerStats) {
        return playerStats.getMatches().stream().filter(m -> matchService.isDraw(playerStats.getUidToMatch().get(m))).count();
    }

    public PlayerStatistics createNewStatisticForPlayer(Player playerOrDummyById) {
        PlayerStatistics statistics = new PlayerStatistics();
        statistics.setPlayer(playerOrDummyById);
        statistics.setPlayerId(playerOrDummyById.getUid());
        return statistics;
    }
}
