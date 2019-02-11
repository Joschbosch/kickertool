package zur.koeln.kickertool.core.logic;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.core.api.IMatchService;
import zur.koeln.kickertool.core.api.IScoreCalcService;
import zur.koeln.kickertool.core.kernl.MatchStatus;
import zur.koeln.kickertool.core.model.Match;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Tournament;

@Component
public class ScoreCalcService
    implements IScoreCalcService {

    @Autowired
    private IMatchService matchService;

    @Override
    public int getScoreForPlayerAndTournamentUntilRound(Player player, Tournament tournament, int roundForScoring) {
        return getStreamWithMatchingMatches(player, tournament, roundForScoring).mapToInt(m -> matchService.hasPlayerWon(m, player) ? tournament.getSettings().getPointsForWinner() : 0).sum();
    }

    @Override
    public int getGoalDiffForPlayerUntilRound(Player player, Tournament tournament, int roundForScoring) {
        return getStreamWithMatchingMatches(player, tournament, roundForScoring).mapToInt(m -> matchService.getScoreForPlayer(m, player)).sum();
    }

    @Override
    public int getMatchesWonCountForPlayerUntilRound(Player player, Tournament tournament, int roundForScoring) {
        return (int) getStreamWithMatchingMatches(player, tournament, roundForScoring).filter(m -> matchService.hasPlayerWon(m, player)).count();
    }

    @Override
    public int getMatchesDrawCountForPlayerUntilRound(Player player, Tournament tournament, int roundForScoring) {
        return (int) getStreamWithMatchingMatches(player, tournament, roundForScoring).filter(m -> m.getScoreHome() == m.getScoreVisiting()).count();
    }

    private Stream<Match> getStreamWithMatchingMatches(Player player1, Tournament tournament, int roundForScoring) {
        return player1.getStatistics().getPlayedMatches().stream()
                .filter(m -> (m.getTournament().getUid().equals(tournament.getUid()) && m.getStatus() == MatchStatus.FINISHED && m.getRoundNumber() <= roundForScoring));
    }

}
