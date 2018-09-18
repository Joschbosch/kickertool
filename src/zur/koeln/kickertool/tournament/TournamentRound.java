/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.*;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
public class TournamentRound
    implements Round {

    @Autowired
    private TournamentFactory tournamentFactory;

    private int roundNo;

    @JsonIgnore
    private final Random r = new Random();

    private final List<Match> matches = new LinkedList<>();

    private final List<Match> completeMatches = new LinkedList<>();

    private Map<UUID, PlayerTournamentStatistics> scoreTableAtEndOfRound;

    /**
     * @param table
     * @param playtables
     * @return
     */
    public void createMatches(List<PlayerTournamentStatistics> table, TournamentSettings config) {
        table.removeIf(statistic -> statistic.getPlayer().isPausingTournament());
        if (roundNo <= config.getRandomRounds()) {
            while (table.size() > 3) {
                TournamentTeam home = new TournamentTeam(getRandomPlayer(table), getRandomPlayer(table));
                TournamentTeam visiting = new TournamentTeam(getRandomPlayer(table), getRandomPlayer(table));
                createMatch(config, home, visiting);
            }
        } else {
            createRoundByTournamentType(table, config);
        }

    }

    private void createRoundByTournamentType(List<PlayerTournamentStatistics> table, TournamentSettings config) {
        if (config.getMode() == TournamentMode.SWISS_DYP) {
            while (table.size() > 3) {
                TournamentTeam home = new TournamentTeam(table.get(0).getPlayer(), table.get(3).getPlayer());
                TournamentTeam visiting = new TournamentTeam(table.get(1).getPlayer(), table.get(2).getPlayer());
                table.remove(0);
                table.remove(0);
                table.remove(0);
                table.remove(0);
                createMatch(config, home, visiting);
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
                createMatch(config, home, visiting);

                home = new TournamentTeam(playerPairs.get(1).getValue0(), playerPairs.get(2).getValue1());
                visiting = new TournamentTeam(playerPairs.get(1).getValue1(), playerPairs.get(2).getValue0());
                createMatch(config, home, visiting);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                TournamentTeam home = new TournamentTeam(playerPairs.get(0).getValue0(), playerPairs.get(1).getValue1());
                TournamentTeam visiting = new TournamentTeam(playerPairs.get(0).getValue1(), playerPairs.get(1).getValue0());
                createMatch(config, home, visiting);

            }
        }
    }

    private void createMatch(TournamentSettings config, TournamentTeam home, TournamentTeam visiting) {
        TournamentMatch m = tournamentFactory.createNewMatch(Integer.valueOf(roundNo), home, visiting, config.getCurrentNoOfMatches());
        ((TournamentSettingsImpl) config).setCurrentNoOfMatches(config.getCurrentNoOfMatches() + 1);
        matches.add(m);
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
    
    @Override
    public int compareTo(Round o) {
    	if (o == null) {
    		return -1;
    	}
    
    	return Integer.compare(getRoundNo(), o.getRoundNo());
    }

    public TournamentFactory getTournamentFactory() {
        return tournamentFactory;
    }

    public void setTournamentFactory(TournamentFactory tournamentFactory) {
        this.tournamentFactory = tournamentFactory;
    }

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    public Map<UUID, PlayerTournamentStatistics> getScoreTableAtEndOfRound() {
        return scoreTableAtEndOfRound;
    }

    public void setScoreTableAtEndOfRound(Map<UUID, PlayerTournamentStatistics> scoreTableAtEndOfRound) {
        this.scoreTableAtEndOfRound = scoreTableAtEndOfRound;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Match> getCompleteMatches() {
        return completeMatches;
    }



}
