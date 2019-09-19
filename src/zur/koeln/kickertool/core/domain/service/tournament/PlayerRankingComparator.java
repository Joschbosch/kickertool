package zur.koeln.kickertool.core.domain.service.tournament;

import java.util.Comparator;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;

public class PlayerRankingComparator
    implements Comparator<Player> {

    private final int roundForScoring;
    private final Tournament tournament;

    public PlayerRankingComparator(
        Tournament tournament,
        int roundForScoring) {
        this.tournament = tournament;
        this.roundForScoring = roundForScoring;
}

    @Override
    public int compare(Player o1, Player o2) {

        Player player1 = o1;
        Player player2 = o2;
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

        long p1Score = tournament.getScoreForPlayerInRound(player1, roundForScoring);
        long p2Score = tournament.getScoreForPlayerInRound(player2, roundForScoring);
        long score = p1Score - p2Score;
        if (score != 0) {
            return -Long.compare(tournament.getScoreForPlayerInRound(player1, roundForScoring), tournament.getScoreForPlayerInRound(player2, roundForScoring));
        }

        long p1GoalDiff = tournament.getGoalsForPlayerInRound(player1, roundForScoring) - tournament.getConcededGoalsForPlayerInRound(player1, roundForScoring);
        long p2GoalDiff = tournament.getGoalsForPlayerInRound(player2, roundForScoring) - tournament.getConcededGoalsForPlayerInRound(player2, roundForScoring);
        long goalDiff = p1GoalDiff - p2GoalDiff;
        if (goalDiff != 0) {
            return -Long.compare(p1GoalDiff, p2GoalDiff);
        }

        long p1MatchesWon = tournament.getMatchesWonForPlayerInRound(player1, roundForScoring);
        long p2MatchesWon = tournament.getMatchesWonForPlayerInRound(player2, roundForScoring);
        long wonDiff = p1MatchesWon - p2MatchesWon;
        if (wonDiff != 0) {
            return -Long.compare(p1MatchesWon, p2MatchesWon);
        }

        long p1MatchesDraw = tournament.getMatchesDrawForPlayerInRound(player1, roundForScoring);
        long p2MatchesDraw = tournament.getMatchesDrawForPlayerInRound(player2, roundForScoring);
        long drawDiff = p1MatchesDraw - p2MatchesDraw;
        if (drawDiff != 0) {
            return -Long.compare(p1MatchesDraw, p2MatchesDraw);
        }

        int firstnameCompare = player1.getFirstName().compareTo(player2.getFirstName());
        if (firstnameCompare != 0) {
            return firstnameCompare;
        }
        int lastnameCompare = player1.getLastName().compareTo(player2.getLastName());
        if (lastnameCompare != 0) {
            return lastnameCompare;
        }
        return player1.getUid().compareTo(player2.getUid());
    }
}