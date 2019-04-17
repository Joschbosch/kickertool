package zur.koeln.kickertool.core.model.aggregates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javatuples.Pair;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.*;
import zur.koeln.kickertool.core.model.entities.GameTable;
import zur.koeln.kickertool.core.model.entities.Match;
import zur.koeln.kickertool.core.model.entities.Settings;
import zur.koeln.kickertool.core.model.valueobjects.Team;

@Getter
@Setter
@NoArgsConstructor
public class Tournament {

    private UUID uid;

    private String name;

    private TournamentStatus status;

    private Settings settings;

    private List<Player> participants = new ArrayList<>();

    private List<Player> dummyPlayer = new ArrayList<>();

    private List<Match> matches = new ArrayList<>();

    private List<GameTable> playtables = new ArrayList<>();

    private int currentRound;

    private final Random r = new Random();

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

    public void addParticipants(List<Player> participants) {
        participants.forEach(this::addParticipant);
    }

    public void addParticipant(Player player) {
        player.setStatus(PlayerStatus.IN_TOURNAMENT);
        participants.add(player);
    }

    public void startTournament() {
        createGametables(settings.getTableCount());
        setStatus(TournamentStatus.RUNNING);
    }



    public void removeParticipant(Player p) {
        participants.remove(p);
    }

    public List<Player> getAllParticipants() {
        List<Player> allPlayer = new ArrayList<>(participants);
        allPlayer.addAll(dummyPlayer);
        return allPlayer;
    }




    private List<Match> getOngoingMatchesInTournament() {
        return getMatchesInCurrentRound().stream().filter(m -> m.getStatus() == MatchStatus.ONGOING).collect(Collectors.toList());
    }

    public List<Match> getMatchesInTournamentRound(int roundNo) {
        return matches.stream().filter(m -> m.getRoundNumber() == roundNo).collect(Collectors.toList());

    }

    public void updateGameTableUsage() {
        for (Match ongoing : getOngoingMatchesInTournament()) {
            for (GameTable gameTable : getPlaytables()) {
                if (gameTable.getStatus() == GameTableStatus.ACTIVE) {
                    ongoing.setTable(gameTable);
                    gameTable.setStatus(GameTableStatus.IN_USE);
                    break;
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

    public void createNextMatches() {
        List<Player> currentStandings = getRanking();

        currentStandings.removeIf(p -> p.getStatus() == PlayerStatus.PAUSING_TOURNAMENT);

        if (currentRound <= getSettings().getRandomRounds()) {
            while (currentStandings.size() > 3) {
                Team home = new Team(getRandomPlayer(currentStandings).getUid(), getRandomPlayer(currentStandings).getUid());
                Team visiting = new Team(getRandomPlayer(currentStandings).getUid(), getRandomPlayer(currentStandings).getUid());
                createMatch(home, visiting);
            }
        } else {
            createMatchesByTournamentType(currentStandings);
        }
    }
    private List<Player> getRanking() {
        return getAllParticipants();
    }

    private void createMatch(Team home, Team visiting) {
        Match m = new Match(UUID.randomUUID());
        m.setHomeTeam(home);
        m.setVisitingTeam(visiting);
        m.setRoundNumber(currentRound);
        m.setStatus(MatchStatus.PLANNED);
        m.setTournament(this);

        matches.add(m);
    }

    private void createMatchesByTournamentType(List<Player> standings) {
        if (settings.getMode() == TournamentMode.SWISS_DYP) {
            while (standings.size() > 3) {
                Team home = new Team(standings.get(0).getUid(), standings.get(3).getUid());
                Team visiting = new Team(standings.get(1).getUid(), standings.get(2).getUid());
                standings.remove(0);
                standings.remove(0);
                standings.remove(0);
                standings.remove(0);
                createMatch(home, visiting);
            }
        } else if (settings.getMode() == TournamentMode.SWISS_TUPEL) {
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
                createMatch(home, visiting);

                home = new Team(playerPairs.get(1).getValue0().getUid(), playerPairs.get(2).getValue1().getUid());
                visiting = new Team(playerPairs.get(1).getValue1().getUid(), playerPairs.get(2).getValue0().getUid());
                createMatch(home, visiting);

                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
                playerPairs.remove(0);
            }
            if (playerPairs.size() > 1) { // notwendig?
                Team home = new Team(playerPairs.get(0).getValue0().getUid(), playerPairs.get(1).getValue1().getUid());
                Team visiting = new Team(playerPairs.get(0).getValue1().getUid(), playerPairs.get(1).getValue0().getUid());
                createMatch(home, visiting);

            }
        }
    }

    public void increaseRound() {
        currentRound++;
    }

    private Player getRandomPlayer(List<Player> standings) {
        int random = r.nextInt(standings.size());
        return standings.remove(random);
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

    public int getActivePlayerCount() {
        return (int) participants.stream().filter(p -> p.getStatus() == PlayerStatus.IN_TOURNAMENT || p.getStatus() == PlayerStatus.PLAYING).count();
    }

    public void removeLastDummyPlayer() {
        Player dummyPlayerToRemove = getDummyPlayer().remove(getDummyPlayer().size() - 1);
        dummyPlayerToRemove.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
    }

    public void addDummyPlayer(Player dummyPlayer) {
        getDummyPlayer().add(dummyPlayer);
        dummyPlayer.setStatus(PlayerStatus.IN_TOURNAMENT);
    }

    public void addMatchResult(UUID matchID, int scoreHome, int scoreVisiting) {
        if (scoreHome <= getSettings().getGoalsToWin() && scoreVisiting <= getSettings().getGoalsToWin()) {
            for (Match m : getMatches()) {
                if (m.getMatchID().equals(matchID)) {
                    m.setScoreHome(scoreHome);
                    m.setScoreVisiting(scoreVisiting);
                    m.setStatus(MatchStatus.FINISHED);
                }
            }
        }
    }
}
