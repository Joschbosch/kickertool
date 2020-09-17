package zur.koeln.kickertool.core.bl.model.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.bl.model.player.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
public class Tournament {

    private UUID uid;

    private String name;

    private TournamentStatus status;

    private Settings settings;

    private List<UUID> participants = new ArrayList<>();

    private List<Match> matches = new ArrayList<>();

    private List<GameTable> playtables = new ArrayList<>();

    private int currentRound;

    public Tournament(
        UUID id,
        Settings newSettings) {
        this.uid = id;
        this.settings = newSettings;
        status = TournamentStatus.PREPARING;
        currentRound = 0;

    }

    public void addMatch(Match m) {
        getMatches().add(m);
    }

    public List<Match> getMatchesWithPlayer(Player player) {

        return matches.stream().filter(m -> m.getHomeTeam().hasPlayer(player) || m.getVisitingTeam().hasPlayer(player)).collect(Collectors.toList());
    }

    public boolean isCurrentRoundComplete() {
        return getNotFinishedMatchesInCurrentRound().isEmpty();
    }
    public List<Match> getNotFinishedMatchesInCurrentRound() {
        return getMatchesInCurrentRound().stream().filter(m -> m.getStatus() != MatchStatus.FINISHED).collect(Collectors.toList());
    }
    public List<Match> getMatchesInCurrentRound() {
        return matches.stream().filter(m -> m.getRoundNumber() == currentRound).collect(Collectors.toList());
    }

    public void configureSettings(Settings settings) {
        setSettings(settings);
    }

    public void addParticipants(List<UUID> participants) {
        participants.forEach(this::addParticipant);
    }

    public void addParticipant(UUID participant) {
        participants.add(participant);
    }

    public void startTournament() {
        createGametables(settings.getTableCount());
        setStatus(TournamentStatus.RUNNING);
    }

    public void removeParticipant(UUID p) {
        participants.remove(p);
    }

    public List<UUID> getAllParticipants() {
        return new ArrayList<>(participants);
    }

    private List<Match> getPlannedMatchesInTournament() {
        return getMatchesInCurrentRound().stream().filter(m -> m.getStatus() == MatchStatus.PLANNED).collect(Collectors.toList());
    }

    public List<Match> getMatchesInTournamentRound(int roundNo) {
        return matches.stream().filter(m -> m.getRoundNumber() == roundNo).collect(Collectors.toList());

    }

    public void updateGameTableUsage(GameTable gameTable2) {
        for (Match ongoing : getPlannedMatchesInTournament()) {
            if (ongoing.getTable() == null) {
            for (GameTable gameTable : getPlaytables()) {
            	if (gameTable2 != null && gameTable.getTableNumber() == gameTable2.getTableNumber()) {
            		gameTable.setStatus(GameTableStatus.ACTIVE);
            	}
                if (gameTable.getStatus() == GameTableStatus.ACTIVE) {
                    ongoing.setTable(gameTable);
                    gameTable.setStatus(GameTableStatus.IN_USE);
                    break;
                }
            }
        }
        }
    }

    private void createGametables(int tableCount) {
        List<GameTable> newTables = new ArrayList<>();
        for (int i = 0; i < tableCount; i++) {
            GameTable newTable = new GameTable();
            newTable.setStatus(GameTableStatus.ACTIVE);
            newTable.setTableNumber(i + 1);
            newTables.add(newTable);
        }
        playtables = newTables;
    }


    public void createMatch(Team home, Team visiting) {
        Match m = new Match(UUID.randomUUID());
        m.setHomeTeam(home);
        m.setVisitingTeam(visiting);
        m.setRoundNumber(currentRound);
        m.setStatus(MatchStatus.PLANNED);
        m.setTournament(this);

        matches.add(m);
    }


    public void increaseRound() {
        currentRound++;
    }


    private Stream<Match> getFinishedMatchesForPlayerUntilRound(Player player, int round) {
        return getMatchesWithPlayer(player).stream().filter(m -> m.getRoundNumber() <= round && m.getStatus() == MatchStatus.FINISHED);
    }

    public long getScoreForPlayerInRound(Player player, int round) {
        List<Match> matchesWithPlayerUntilGivenRound = getFinishedMatchesForPlayerUntilRound(player, round).collect(Collectors.toList());
        long wonMatches = matchesWithPlayerUntilGivenRound.stream().filter(m -> m.hasPlayerWon(player)).count();
        long drawMatches = matchesWithPlayerUntilGivenRound.stream().filter(Match::isDraw).count();
        return wonMatches * settings.getPointsForWinner() + drawMatches * settings.getPointsForDraw();
    }

    public long getMatchesWonForPlayerInRound(Player player, int round) {
        List<Match> matchesWithPlayerUntilGivenRound = getFinishedMatchesForPlayerUntilRound(player, round).collect(Collectors.toList());
        return matchesWithPlayerUntilGivenRound.stream().filter(m -> m.hasPlayerWon(player)).count();
    }

    public long getMatchesDrawForPlayerInRound(Player player, int round) {
        List<Match> matchesWithPlayerUntilGivenRound = getFinishedMatchesForPlayerUntilRound(player, round).collect(Collectors.toList());
        return matchesWithPlayerUntilGivenRound.stream().filter(Match::isDraw).count();
    }

    public long getNoOfFinishedMatchesForPlayerInRound(Player player, int round) {
        List<Match> matchesWithPlayerUntilGivenRound = getFinishedMatchesForPlayerUntilRound(player, round).collect(Collectors.toList());
        return matchesWithPlayerUntilGivenRound.size();
    }

    public long getGoalsForPlayerInRound(Player player, int round) {
        List<Match> matchesWithPlayerUntilGivenRound = getFinishedMatchesForPlayerUntilRound(player, round).collect(Collectors.toList());
        return matchesWithPlayerUntilGivenRound.stream().collect(Collectors.summarizingInt(m -> m.getGoalsForPlayer(player))).getSum();
    }
    public long getConcededGoalsForPlayerInRound(Player player, int round) {
        List<Match> matchesWithPlayerUntilGivenRound = getFinishedMatchesForPlayerUntilRound(player, round).collect(Collectors.toList());
        return matchesWithPlayerUntilGivenRound.stream().collect(Collectors.summarizingInt(m -> m.getGoalsAgainstPlayer(player))).getSum();
    }

    public void addDummyPlayer(Player dummyPlayer) {
        dummyPlayer.setStatus(PlayerStatus.IN_TOURNAMENT);
        addParticipant(dummyPlayer.getUid());
    }

    public boolean addMatchResult(UUID matchID, int scoreHome, int scoreVisiting) {
        if (scoreHome <= getSettings().getGoalsToWin() && scoreVisiting <= getSettings().getGoalsToWin()) {
            for (Match m : getMatches()) {
                if (m.getMatchID().equals(matchID)) {
                    m.setScoreHome(scoreHome);
                    m.setScoreVisiting(scoreVisiting);
                    m.setStatus(MatchStatus.FINISHED);
                    if (m.getTable() != null) {
                        m.getTable().setStatus(GameTableStatus.ACTIVE);
                        updateGameTableUsage(m.getTable());
                    }
                    m.setTable(null);
                    return true;
                }
            }
        }
        return false;
    }


}
