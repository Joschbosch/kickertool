package zur.koeln.kickertool.tournament.data;

import java.util.Comparator;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.TournamentSettings;

public class PlayerTournamentStatisticsComparator
    implements Comparator<PlayerTournamentStatistics> {

    private final TournamentSettings settings;

    public PlayerTournamentStatisticsComparator(
        TournamentSettings settings) {
        this.settings = settings;
    }

    @Override
    public int compare(PlayerTournamentStatistics o1, PlayerTournamentStatistics o2) {
        Player player1 = o1.getPlayer();
        Player player2 = o2.getPlayer();
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

        long pointsForConfiguration = o1.getPointsForConfiguration(settings) - o2.getPointsForConfiguration(settings);
        if (pointsForConfiguration != 0) {
            return -Long.compare(o1.getPointsForConfiguration(settings), o2.getPointsForConfiguration(settings));
        }

        int goalDiff = o1.getGoalDiff() - o2.getGoalDiff();
        if (goalDiff != 0) {
            return -Integer.compare(o1.getGoalDiff(), o2.getGoalDiff());
        }

        int wonDiff = (int) (o1.getMatchesWonCount() - o2.getMatchesWonCount());
        if (wonDiff != 0) {
            return -Long.compare(o1.getMatchesWonCount(), o2.getMatchesWonCount());
        }

        int drawDiff = (int) (o1.getMatchesDrawCount() - o2.getMatchesDrawCount());
        if (drawDiff != 0) {
            return -Long.compare(o1.getMatchesDrawCount(), o2.getMatchesDrawCount());
        }
        int nameCompare = player1.getName().compareTo(player2.getName());

        if (nameCompare != 0) {
            return nameCompare;
        }
        return player1.getUid().compareTo(player2.getUid());
    }

}
