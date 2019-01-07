package zur.koeln.kickertool.tournament;

import java.util.*;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.tournament.data.*;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Service
public class TournamentService {

    private Tournament currentTournament;

    private final PlayerPoolService playerPool;

    private final MatchCreationService matchCreationService;

    public TournamentService(
        PlayerPoolService playerPool,
        MatchCreationService matchCreationService) {
        this.playerPool = playerPool;
        this.matchCreationService = matchCreationService;
    }

    /**
     * @param players
     */
    public void addParticipants(List<Player> players) {
        for (Player p : players) {
            addParticipant(p);
        }
    }

    public void addParticipant(Player p) {
        if (!currentTournament.isStarted()) {
            if (!currentTournament.getParticipants().contains(p.getUid())) {
                currentTournament.getParticipants().add(p.getUid());
            }
        } else {
            if (!currentTournament.getScoreTable().containsKey(p.getUid())) {
                currentTournament.getScoreTable().put(p.getUid(), new PlayerTournamentStatisticsImpl(p));
                checkDummies();
            }
        }
    }

    public void removeParticipant(Player p) {
        if (!currentTournament.isStarted()) {
            currentTournament.getParticipants().remove(p.getUid());
        } else {
            currentTournament.getScoreTable().remove(p.getUid());
            checkDummies();
        }

    }

    public void pausePlayer(Player selectedPlayer) {
        ((PlayerTournamentStatisticsImpl) currentTournament.getScoreTable().get(selectedPlayer.getUid())).setPlayerPausing(true);
        checkDummies();
    }

    public void unpausePlayer(Player selectedPlayer) {
        ((PlayerTournamentStatisticsImpl) currentTournament.getScoreTable().get(selectedPlayer.getUid())).setPlayerPausing(false);
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
                currentTournament.getScoreTable().remove(removeDummy);
                currentTournament.getDummyPlayerActive().remove(removeDummy);
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                Player dummy = playerPool.useNextDummyPlayer();
                currentTournament.getScoreTable().put(dummy.getUid(), new PlayerTournamentStatisticsImpl(dummy));
                currentTournament.getDummyPlayerActive().add(dummy.getUid());
            }
        }
    }

    /**
     * @return
     */
    private int getActivePlayerCount() {
        int count = 0;
        for (PlayerTournamentStatistics ts : currentTournament.getScoreTable().values()) {
            Player player = playerPool.getPlayerOrDummyById(ts.getPlayerId());
            if (!currentTournament.getScoreTable().get(player.getUid()).isPlayerPausing() && !player.isDummy()) {
                count++;
            }
        }
        return count;
    }

    public void startTournament() {
        if (!currentTournament.isStarted()) {
            for (int i = 1; i <= currentTournament.getSettings().getTableCount(); i++) {
                currentTournament.getPlaytables().put(Integer.valueOf(i), new GamingTable(i));
            }
            for (UUID pid : currentTournament.getParticipants()) {
                currentTournament.getScoreTable().put(pid, new PlayerTournamentStatisticsImpl(playerPool.getPlayerOrDummyById(pid)));
            }
            checkDummies();
        }
        currentTournament.setStarted(true);
    }

    public Round newRound() {

        if (isCurrentRoundComplete()) {
            int nextRoundNumber = 1;
            if (currentTournament.getCurrentRound() != null) {
                Map<UUID, PlayerTournamentStatistics> scoreTableClone = new HashMap<>();
                currentTournament.getScoreTable().forEach((key, value) -> {
                    PlayerTournamentStatistics clonedStatistics = new PlayerTournamentStatisticsImpl(value.getPlayer());
                    ((PlayerTournamentStatisticsImpl) value).getClone((PlayerTournamentStatisticsImpl) clonedStatistics);
                    scoreTableClone.put(key, clonedStatistics);
                });
                ((TournamentRound) currentTournament.getCurrentRound()).setScoreTableAtEndOfRound(scoreTableClone);
                currentTournament.getCompleteRounds().add(currentTournament.getCurrentRound());
                nextRoundNumber = currentTournament.getCurrentRound().getRoundNo() + 1;
            }
            Round newRound = new TournamentRound(nextRoundNumber);
            currentTournament.setCurrentRound(newRound);
            matchCreationService.createMatches(getCurrentTableCopySortedByPoints(), currentTournament.getSettings(), newRound);
            updatePlayTableUsage();
            return newRound;
        }
        return null;
    }

    public void addMatchResult(Match m) throws MatchException {
        ((TournamentRound) currentTournament.getCurrentRound()).addMatchResult(m);
        currentTournament.getPlaytables().get(Integer.valueOf(m.getTableNo())).setInUse(false);
        updatePlayTableUsage();

        updateTable(m, m.getHomeTeam().getPlayer1Id());
        updateTable(m, m.getHomeTeam().getPlayer2Id());
        updateTable(m, m.getVisitingTeam().getPlayer1Id());
        updateTable(m, m.getVisitingTeam().getPlayer2Id());
    }

    private void updateTable(Match m, UUID playerId) {
        PlayerTournamentStatistics homeP1Stat = currentTournament.getScoreTable().get(playerId);
        if (homeP1Stat != null) {
            homeP1Stat.addMatchResult(m);
        }
    }

    private void updatePlayTableUsage() {
        for (Match ongoing : currentTournament.getCurrentRound().getMatches()) {
            if (ongoing.getTableNo() == -1) {
                for (GamingTable gameTable : currentTournament.getPlaytables().values()) {
                    if (!gameTable.isInUse() && gameTable.isActive()) {
                        ((TournamentMatch) ongoing).setTableNo(gameTable.getTableNumber());
                        gameTable.setInUse(true);
                        break;
                    }
                }
            }
        }
    }

    @JsonIgnore
    public List<PlayerTournamentStatistics> getHistoricTableCopySortedByPoints(int roundNo) {
        Collection<PlayerTournamentStatistics> tableToSort = currentTournament.getScoreTable().values();

        if (currentTournament.getCurrentRound().getRoundNo() != roundNo) {
            for (Round completeRound : currentTournament.getCompleteRounds()) {
                if (completeRound.getRoundNo() == roundNo && completeRound.getScoreTableAtEndOfRound() != null) {
                    tableToSort = completeRound.getScoreTableAtEndOfRound().values();
                    break;
                }
            }
        }

        List<PlayerTournamentStatistics> sorting = new LinkedList<>(tableToSort);
        Collections.sort(sorting, currentTournament.getTableComparator());
        return sorting;
    }


    @JsonIgnore
    public List<PlayerTournamentStatistics> getCurrentTableCopySortedByPoints() {
        return getHistoricTableCopySortedByPoints(currentTournament.getCurrentRound().getRoundNo());
    }

    @JsonIgnore
    public List<Match> getAllMatches() {

        List<Match> result = new LinkedList<>(currentTournament.getCurrentRound().getAllMatches());
        currentTournament.getCompleteRounds().forEach(r -> result.addAll(r.getAllMatches()));
        Collections.sort(result, (o1, o2) -> o1.getMatchNo() - o2.getMatchNo());

        return result;
    }

    @JsonIgnore
    public boolean isCurrentRoundComplete() {
        return currentTournament.getCurrentRound() != null ? currentTournament.getCurrentRound().isComplete() : Boolean.TRUE.booleanValue();
    }

    public List<Match> getMatchesForRound(int roundNo) {
        if (currentTournament.getCurrentRound().getRoundNo() == roundNo) {
            return currentTournament.getCurrentRound().getAllMatches();
        }
        for (Round r : currentTournament.getCompleteRounds()) {
            if (r.getRoundNo() == roundNo) {
                return r.getAllMatches();
            }
        }
        return new ArrayList<>();
    }

    public Tournament getCurrentTournament() {
        return currentTournament;
    }

    public void setCurrentTournament(Tournament importTournament) {
        currentTournament = importTournament;
    }

    public Tournament createNewTournament(String text) {
        currentTournament = new TournamentImpl(new TournamentSettingsImpl());
        currentTournament.setName(text);
        return currentTournament;
    }

    public SortedSet<PlayerTournamentStatistics> getCurrentTableAsSet() {
        TreeSet<PlayerTournamentStatistics> table = new TreeSet<>(currentTournament.getTableComparator());
        Collection<PlayerTournamentStatistics> values = currentTournament.getScoreTable().values();
        table.addAll(values);
        return table;
    }

}
