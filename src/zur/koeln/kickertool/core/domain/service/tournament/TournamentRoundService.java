package zur.koeln.kickertool.core.domain.service.tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Named;

import org.javatuples.Pair;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Team;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;
import zur.koeln.kickertool.core.domain.model.entities.tournament.TournamentMode;
import zur.koeln.kickertool.core.domain.service.tournament.api.ITournamentRoundService;
import zur.koeln.kickertool.core.domain.service.tournament.api.PlayerRankingRow;

@Named
public class TournamentRoundService
    implements ITournamentRoundService {

    @Override
    public boolean startNewRound(Tournament tournament, List<Player> participants) {
        if (!tournament.isCurrentRoundComplete()) {
            return false;
        }
        tournament.increaseRound();
        createNextMatches(tournament, participants);
        tournament.updateGameTableUsage();
        return true;
    }

    private void createNextMatches(Tournament tournament, List<Player> currentPlayerRanking) {
        Collections.sort(currentPlayerRanking, new PlayerRankingComparator(tournament, tournament.getCurrentRound()));

        currentPlayerRanking.removeIf(p -> p.getStatus() == PlayerStatus.PAUSING_TOURNAMENT);

        if (tournament.getCurrentRound() <= tournament.getSettings().getRandomRounds()) {
            while (currentPlayerRanking.size() > 3) {
                Team home = new Team(getRandomPlayer(currentPlayerRanking).getUid(), getRandomPlayer(currentPlayerRanking).getUid());
                Team visiting = new Team(getRandomPlayer(currentPlayerRanking).getUid(), getRandomPlayer(currentPlayerRanking).getUid());
                tournament.createMatch(home, visiting);
            }
        } else {
            createMatchesByTournamentType(currentPlayerRanking, tournament);
        }
    }

    private Player getRandomPlayer(List<Player> standings) {
        int random = new Random().nextInt(standings.size());
        return standings.remove(random);
    }

    private void createMatchesByTournamentType(List<Player> standings, Tournament tournament) {
        if (tournament.getSettings().getMode() == TournamentMode.SWISS_DYP) {
            while (standings.size() > 3) {
                Team home = new Team(standings.get(0).getUid(), standings.get(3).getUid());
                Team visiting = new Team(standings.get(1).getUid(), standings.get(2).getUid());
                standings.remove(0);
                standings.remove(0);
                standings.remove(0);
                standings.remove(0);
                tournament.createMatch(home, visiting);
            }
        } else if (tournament.getSettings().getMode() == TournamentMode.SWISS_TUPEL) {
            List<Pair<Player, Player>> playerPairs = new ArrayList<>();
            while (!standings.isEmpty()) {
                Pair<Player, Player> newPair = new Pair<>(standings.get(0), standings.get(1));
                playerPairs.add(newPair);
                standings.remove(0);
                standings.remove(0);
            }
            while (playerPairs.size() > 3) {
                Team home = new Team(playerPairs.get(0).getValue0().getUid(), playerPairs.get(3).getValue1().getUid());
                Team visiting = new Team(playerPairs.get(0).getValue1().getUid(), playerPairs.get(3).getValue0().getUid());
                tournament.createMatch(home, visiting);

                home = new Team(playerPairs.get(1).getValue0().getUid(), playerPairs.get(2).getValue1().getUid());
                visiting = new Team(playerPairs.get(1).getValue1().getUid(), playerPairs.get(2).getValue0().getUid());
                tournament.createMatch(home, visiting);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                Team home = new Team(playerPairs.get(0).getValue0().getUid(), playerPairs.get(1).getValue1().getUid());
                Team visiting = new Team(playerPairs.get(0).getValue1().getUid(), playerPairs.get(1).getValue0().getUid());
                tournament.createMatch(home, visiting);

            }
        }
    }
    @Override
    public List<PlayerRankingRow> getRankingByRound(Tournament tournament, List<Player> allParticipants, int round) {
        Collections.sort(allParticipants, new PlayerRankingComparator(tournament, round));

        List<PlayerRankingRow> ranking = new ArrayList<>();
        for (int i = 0; i < allParticipants.size(); i++) {
            PlayerRankingRow newRow = new PlayerRankingRow();
            Player player = allParticipants.get(i);
            newRow.setPlayer(player);
            newRow.setRank(i + 1);
            if (!player.isDummy()) {
                newRow.setScore((int) tournament.getScoreForPlayerInRound(player, round));

                newRow.setMatchesPlayed((int) tournament.getNoOfFinishedMatchesForPlayerInRound(player, round));
                newRow.setMatchesWon((int) tournament.getMatchesWonForPlayerInRound(player, round));
                newRow.setMatchesDraw((int) tournament.getMatchesDrawForPlayerInRound(player, round));
                newRow.setMatchesLost(newRow.getMatchesPlayed() - newRow.getMatchesWon() - newRow.getMatchesDraw());

                newRow.setGoals((int) tournament.getGoalsForPlayerInRound(player, round));
                newRow.setConcededGoals((int) tournament.getConcededGoalsForPlayerInRound(player, round));
                newRow.setGoaldiff(newRow.getGoals() - newRow.getConcededGoals());
            }
            ranking.add(newRow);

        }
        return ranking;
    }

}
