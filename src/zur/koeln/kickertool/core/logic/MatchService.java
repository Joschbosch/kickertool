package zur.koeln.kickertool.core.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.core.api.IMatchService;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.MatchStatus;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.kernl.TournamentMode;
import zur.koeln.kickertool.core.model.Match;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Team;
import zur.koeln.kickertool.core.model.Tournament;

@Component
public class MatchService
    implements IMatchService {

    @Autowired
    private ITournamentService tournamentService;

    private final Random randomGenerator = new Random();

    @Override
    public void createNextMatches(int newRoundNumber, UUID tournamentUID) {
        Tournament tournament = tournamentService.getTournamentById(tournamentUID);
        List<Player> currentStandings = tournamentService.getCurrentTournamentStandings(tournamentUID);
        currentStandings.removeIf(p -> p.getStatus() == PlayerStatus.PAUSING_TOURNAMENT);

        if (newRoundNumber <= tournament.getSettings().getRandomRounds()) {
            while (currentStandings.size() > 3) {
                Team home = new Team(UUID.randomUUID(), getRandomPlayer(currentStandings), getRandomPlayer(currentStandings));
                Team visiting = new Team(UUID.randomUUID(), getRandomPlayer(currentStandings), getRandomPlayer(currentStandings));
                createMatch(newRoundNumber, home, visiting, tournament);
            }
        } else {
            createMatchesByTournamentType(newRoundNumber, currentStandings, tournament);
        }
    }

    @Override
    public boolean hasPlayerWon(Match m, Player player) {
        if (m.getHomeTeam().getPlayer1().equals(player) || m.getHomeTeam().getPlayer2().equals(player)) {
            return m.getScoreHome() > m.getScoreVisiting();
        }
        return false;
    }
    @Override
    public int getScoreForPlayer(Match m, Player player) {
        if (m.getHomeTeam().getPlayer1().equals(player) || m.getHomeTeam().getPlayer2().equals(player)) {
            return m.getScoreHome();
        }
        return m.getScoreVisiting();
    }

    private void createMatchesByTournamentType(int newRound, List<Player> standings, Tournament tournament) {
        if (tournament.getSettings().getMode() == TournamentMode.SWISS_DYP) {
            while (standings.size() > 3) {
                Team home = new Team(UUID.randomUUID(), standings.get(0), standings.get(3));
                Team visiting = new Team(UUID.randomUUID(), standings.get(1), standings.get(2));
                standings.remove(0);
                standings.remove(0);
                standings.remove(0);
                standings.remove(0);
                createMatch(newRound, home, visiting, tournament);
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
                Team home = new Team(UUID.randomUUID(), playerPairs.get(0).getValue0(), playerPairs.get(3).getValue1());
                Team visiting = new Team(UUID.randomUUID(), playerPairs.get(0).getValue1(), playerPairs.get(3).getValue0());
                createMatch(newRound, home, visiting, tournament);

                home = new Team(UUID.randomUUID(), playerPairs.get(1).getValue0(), playerPairs.get(2).getValue1());
                visiting = new Team(UUID.randomUUID(), playerPairs.get(1).getValue1(), playerPairs.get(2).getValue0());
                createMatch(newRound, home, visiting, tournament);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                Team home = new Team(UUID.randomUUID(), playerPairs.get(0).getValue0(), playerPairs.get(1).getValue1());
                Team visiting = new Team(UUID.randomUUID(), playerPairs.get(0).getValue1(), playerPairs.get(1).getValue0());
                createMatch(newRound, home, visiting, tournament);

            }
        }
    }
    private Player getRandomPlayer(List<Player> standings) {
        int random = randomGenerator.nextInt(standings.size());
        return standings.remove(random);
    }
    private void createMatch(int newRoundNumber, Team home, Team visiting, Tournament tournament) {
        Match m = new Match(UUID.randomUUID());
        m.setHomeTeam(home);
        m.setVisitingTeam(visiting);
        m.setRoundNumber(newRoundNumber);
        m.setStatus(MatchStatus.PLANNED);
        m.setTournament(tournament);

        tournamentService.addNewMatchToTournament(tournament.getUid(), m);
    }
}
