package zur.koeln.kickertool.core.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.core.api.IPlayerRepository;
import zur.koeln.kickertool.core.entities.*;

@Service
public class TournamentService {

    @Autowired
    private IPlayerRepository playerPool;

    @Autowired
    private StatisticsService staticService;

    @Autowired
    private RoundService roundService;

    private Tournament tournament;

    public void addParticipant(Player p) {
        if (!tournament.isStarted()) {
            if (!tournament.getParticipants().contains(p.getUid())) {
                tournament.getParticipants().add(p.getUid());
            }
        } else {
            if (!tournament.getScoreTable().containsKey(p.getUid())) {
                tournament.getScoreTable().put(p.getUid(), staticService.createNewStatisticForPlayer(p));
                checkDummies();
            }
        }
    }

    public void removeParticipant(Player p) {
        if (!tournament.isStarted()) {
            tournament.getParticipants().remove(p.getUid());
        } else {
            tournament.getScoreTable().remove(p.getUid());
            checkDummies();
        }

    }

    public void pausePlayer(Player selectedPlayer) {
        tournament.getScoreTable().get(selectedPlayer.getUid()).setPlayerPausing(true);
        checkDummies();
    }

    public void unpausePlayer(Player selectedPlayer) {
        tournament.getScoreTable().get(selectedPlayer.getUid()).setPlayerPausing(false);
        checkDummies();
    }

    /**
     * 
     */
    private void checkDummies() {
        int usedDummies = playerPool.getNoOfDummyPlayerUsed();
        int neededDummies = getActivePlayerCount() % 4 == 0 ? 0 : 4 - getActivePlayerCount() % 4;
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                UUID removeDummy = playerPool.removeLastDummy();
                tournament.getScoreTable().remove(removeDummy);
                tournament.getDummyPlayerActive().remove(removeDummy);
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                Player dummy = playerPool.useNextDummyPlayer();
                tournament.getScoreTable().put(dummy.getUid(), staticService.createNewStatisticForPlayer(dummy));
                tournament.getDummyPlayerActive().add(dummy.getUid());
            }
        }
    }

    /**
     * @return
     */
    private int getActivePlayerCount() {
        int count = 0;
        for (PlayerStatistics ts : tournament.getScoreTable().values()) {
            Player player = playerPool.getPlayerOrDummyById(ts.getPlayerId());
            if (!tournament.getScoreTable().get(player.getUid()).isPlayerPausing() && !player.isDummy()) {
                count++;
            }
        }
        return count;
    }

    public void startTournament() {
        if (!tournament.isStarted()) {
            for (int i = 1; i <= tournament.getSettings().getTableCount(); i++) {
                tournament.getPlaytables().put(Integer.valueOf(i), new GameTable(i));
            }
            for (UUID pid : tournament.getParticipants()) {
                tournament.getScoreTable().put(pid, staticService.createNewStatisticForPlayer(playerPool.getPlayerOrDummyById(pid)));
            }
            checkDummies();
        }
        tournament.setStarted(true);
    }

    public Round newRound() {

        if (isCurrentRoundComplete()) {
            int nextRoundNumber = 1;
            if (tournament.getCurrentRound() != null) {
                Map<UUID, PlayerStatistics> scoreTableClone = new HashMap<>();
                tournament.getScoreTable().forEach((key, value) -> {
                    PlayerStatistics clonedStatistics = staticService.createNewStatisticForPlayer(value.getPlayer());
                    staticService.getClone(value, clonedStatistics);
                    scoreTableClone.put(key, clonedStatistics);
                });
                tournament.getCurrentRound().setScoreTableAtEndOfRound(scoreTableClone);
                tournament.getCompleteRounds().add(tournament.getCurrentRound());
                nextRoundNumber = tournament.getCurrentRound().getRoundNo() + 1;
            }
            Round newRound = roundService.createNewRound(nextRoundNumber);
            tournament.setCurrentRound(newRound);
            roundService.createMatches(newRound, getCurrentTableCopySortedByPoints(), tournament.getSettings());
            updatePlayTableUsage();
            return newRound;
        }
        return null;
    }

    public void addMatchResult(Match m) {
        roundService.addMatchResult(m, tournament.getCurrentRound());
        tournament.getPlaytables().get(Integer.valueOf(m.getTableNo())).setInUse(false);
        updatePlayTableUsage();

        updateTable(m, m.getHomeTeam().getPlayer1Id());
        updateTable(m, m.getHomeTeam().getPlayer2Id());
        updateTable(m, m.getVisitingTeam().getPlayer1Id());
        updateTable(m, m.getVisitingTeam().getPlayer2Id());
    }

    private void updateTable(Match m, UUID playerId) {
        PlayerStatistics homeP1Stat = tournament.getScoreTable().get(playerId);
        if (homeP1Stat != null) {
            homeP1Stat.addMatchResult(m);
        }
    }

    private void updatePlayTableUsage() {
        for (Match ongoing : tournament.getCurrentRound().getMatches()) {
            if (ongoing.getTableNo() == -1) {
                for (GameTable gameTable : tournament.getPlaytables().values()) {
                    if (!gameTable.isInUse() && gameTable.isActive()) {
                        ongoing.setTableNo(gameTable.getTableNumber());
                        gameTable.setInUse(true);
                        break;
                    }
                }
            }
        }
    }

    @JsonIgnore
    public List<PlayerStatistics> getHistoricTableCopySortedByPoints(int roundNo) {
        Collection<PlayerStatistics> tableToSort = tournament.getScoreTable().values();

        if (tournament.getCurrentRound().getRoundNo() != roundNo) {
            for (Round completeRound : tournament.getCompleteRounds()) {
                if (completeRound.getRoundNo() == roundNo && completeRound.getScoreTableAtEndOfRound() != null) {
                    tableToSort = completeRound.getScoreTableAtEndOfRound().values();
                    break;
                }
            }
        }

        List<PlayerStatistics> sorting = new LinkedList<>(tableToSort);
        //        Collections.sort(sorting);
        return sorting;
    }

    @JsonIgnore
    public List<PlayerStatistics> getCurrentTableCopySortedByPoints() {
        return getHistoricTableCopySortedByPoints(tournament.getCurrentRound().getRoundNo());
    }

    @JsonIgnore
    public List<Match> getAllMatches() {

        List<Match> result = new LinkedList<>(tournament.getCurrentRound().getAllMatches());
        tournament.getCompleteRounds().forEach(r -> result.addAll(r.getAllMatches()));
        Collections.sort(result, (o1, o2) -> o1.getMatchNo() - o2.getMatchNo());

        return result;
    }

    @JsonIgnore
    public boolean isCurrentRoundComplete() {
        return tournament.getCurrentRound() != null ? tournament.getCurrentRound().isComplete() : Boolean.TRUE.booleanValue();
    }

    public List<Match> getMatchesForRound(int roundNo) {
        if (tournament.getCurrentRound().getRoundNo() == roundNo) {
            return tournament.getCurrentRound().getAllMatches();
        }
        for (Round r : tournament.getCompleteRounds()) {
            if (r.getRoundNo() == roundNo) {
                return r.getAllMatches();
            }
        }
        return new ArrayList<>();
    }

    public Tournament createNewTournament() {
        Tournament newTournament = new Tournament();
        newTournament.setConfig(new Settings());
        setTournament(newTournament);
        return newTournament;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Settings getTournamentSettings() {
        return tournament.getSettings();
    }

    public void setTournament(Tournament importTournament) {
        tournament = importTournament;
    }

}
