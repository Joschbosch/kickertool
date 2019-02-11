package zur.koeln.kickertool.core.logic;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.*;
import zur.koeln.kickertool.core.kernl.GameTableStatus;
import zur.koeln.kickertool.core.kernl.MatchStatus;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.core.model.*;

@Service
public class TournamentService
    implements ITournamentService {

    @Autowired
    private ITournamentRepository tournamentRepo;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private IMatchService roundService;

    @Autowired
    private IScoreCalcService scoreService;

    @Override
    public Tournament createNewTournament() {

        Settings newSettings = new Settings(UUID.randomUUID());
        Tournament newTournament = new Tournament(UUID.randomUUID(), newSettings);
        newSettings.setTournament(newTournament);
        checkDummyPlayer(newTournament);
        tournamentRepo.saveOrUpdateTournament(newTournament);
        return newTournament;
    }
    @Override
    public Tournament startTournament(UUID tournamentIDToStart) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToStart);
        List<GameTable> tables = createGametables(tournament.getSettings().getTableCount());
        tournament.setPlaytables(tables);
        tournament.setStatus(TournamentStatus.RUNNING);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return tournament;
    }

    @Override
    public Tournament renameTournament(UUID tournamentIDToRename, String name) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToRename);
        tournament.setName(name);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return tournament;
    }
    @Override
    public List<Player> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToAdd);
        Player player = playerService.getPlayerById(participant);
        tournament.getParticipants().add(player);
        player.setStatus(PlayerStatus.IN_TOURNAMENT);
        checkDummyPlayer(tournament);
        return getTournamentParticipants(tournamentIDToAdd);
    }
    @Override
    public List<Player> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToRemove);
        Player player = playerService.getPlayerById(participant);
        tournament.getParticipants().remove(player);
        player.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
        checkDummyPlayer(tournament);
        return getTournamentParticipants(tournamentIDToRemove);
    }

    public void pauseOrUnpausePlayer(UUID playerToPause, boolean pausing) {
        Player p = playerService.getPlayerById(playerToPause);
        p.setStatus(pausing ? PlayerStatus.PAUSING_TOURNAMENT : PlayerStatus.IN_TOURNAMENT);

    }

    @Override
    public List<Player> getTournamentParticipants(UUID tournamentId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        List<Player> participants = new ArrayList<>();
        if (tournament != null) {
            participants.addAll(tournament.getParticipants());
            participants.addAll(tournament.getDummyPlayer());
        }
        return participants;
    }

    @Override
    public Settings changeTournamentSettings(Settings settings) {

        return null;
    }
    @Override
    public boolean isCurrentRoundComplete(UUID tournamentId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        return getNotFinishedMatchesInTournament(tournament).isEmpty();
    }

    @Override
    public List<Match> getMatchesForRound(int roundNo, UUID tournamentId) {
        return getMatchesInTournamentRound(roundNo, tournamentRepo.getTournament(tournamentId));
    }

    @Override
    public void startNewRound(UUID tournamentToStartNewRound) {
        Tournament tournament = tournamentRepo.getTournament(tournamentToStartNewRound);
        long runningMatches = getNotFinishedMatchesInTournament(tournament).size();
        if (runningMatches > 0) {
            return;
        }
        tournament.setCurrentRound(tournament.getCurrentRound() + 1);
        roundService.createNextMatches(tournament.getCurrentRound(), tournament.getUid());
        updateGameTableUsage(tournament);
    }

    @Override
    public void addNewMatchToTournament(UUID tournamentId, Match m) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        tournament.addMatch(m);
    }

    @Override
    public Tournament getTournamentById(UUID tournamentUID) {
        return tournamentRepo.getTournament(tournamentUID);
    }

    @Override
    public List<Player> getCurrentTournamentStandings(UUID tournamentUID) {
        int currentRound = tournamentRepo.getTournament(tournamentUID).getCurrentRound();
        return getTournamentStandingsForRound(tournamentUID, currentRound);
    }
    @Override
    public List<Player> getTournamentStandingsForRound(UUID tournamentUID, int round) {
        Tournament tournament = tournamentRepo.getTournament(tournamentUID);
        List<Player> standings = getTournamentParticipants(tournamentUID);
        Collections.sort(standings, new PlayerStandingsComparator(tournament, round));
        return standings;
    }

    private class PlayerStandingsComparator
        implements Comparator<Player> {

        private final int roundForScoring;
        private final Tournament tournament;

        public PlayerStandingsComparator(
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

            int p1Score = scoreService.getScoreForPlayerAndTournamentUntilRound(player1, tournament, roundForScoring);
            int p2Score = scoreService.getScoreForPlayerAndTournamentUntilRound(player2, tournament, roundForScoring);
            int score = p1Score - p2Score;
            if (score != 0) {
                return -Long.compare(scoreService.getScoreForPlayerAndTournamentUntilRound(player1, tournament, roundForScoring),
                        scoreService.getScoreForPlayerAndTournamentUntilRound(player2, tournament, roundForScoring));
            }

            int p1GoalDiff = scoreService.getGoalDiffForPlayerUntilRound(player1, tournament, roundForScoring);
            int p2GoalDiff = scoreService.getGoalDiffForPlayerUntilRound(player2, tournament, roundForScoring);
            int goalDiff = p1GoalDiff - p2GoalDiff;
            if (goalDiff != 0) {
                return -Integer.compare(p1GoalDiff,
                        p2GoalDiff);
            }

            int p1MatchesWon = scoreService.getMatchesWonCountForPlayerUntilRound(player1, tournament, roundForScoring);
            int p2MatchesWon = scoreService.getMatchesWonCountForPlayerUntilRound(player2, tournament, roundForScoring);
            int wonDiff = p1MatchesWon - p2MatchesWon;
            if (wonDiff != 0) {
                return -Integer.compare(p1MatchesWon, p2MatchesWon);
            }

            int p1MatchesDraw = scoreService.getMatchesDrawCountForPlayerUntilRound(player1, tournament, roundForScoring);
            int p2MatchesDraw = scoreService.getMatchesDrawCountForPlayerUntilRound(player2, tournament, roundForScoring);
            int drawDiff = p1MatchesDraw - p2MatchesDraw;
            if (drawDiff != 0) {
                return -Integer.compare(p1MatchesDraw, p2MatchesDraw);
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

    private List<Match> getNotFinishedMatchesInTournament(Tournament tournament) {
        return getMatchesInCurrentTournamentRound(tournament).stream().filter(m -> m.getStatus() != MatchStatus.FINISHED).collect(Collectors.toList());
    }

    private List<Match> getOngoingMatchesInTournament(Tournament tournament) {
        return getMatchesInCurrentTournamentRound(tournament).stream().filter(m -> m.getStatus() == MatchStatus.ONGOING).collect(Collectors.toList());
    }

    private List<Match> getMatchesInTournamentRound(int roundNo, Tournament tournament) {
        return tournament.getMatches().stream().filter(m -> m.getRoundNumber() == roundNo).collect(Collectors.toList());

    }
    private List<Match> getMatchesInCurrentTournamentRound(Tournament tournament) {
        return getMatchesInTournamentRound(tournament.getCurrentRound(), tournament);
    }

    private void updateGameTableUsage(Tournament tournament) {
        for (Match ongoing : getOngoingMatchesInTournament(tournament)) {
            for (GameTable gameTable : tournament.getPlaytables()) {
                if (gameTable.getStatus() == GameTableStatus.ACTIVE) {
                    ongoing.setTable(gameTable);
                    gameTable.setStatus(GameTableStatus.IN_USE);
                    break;
                }
            }
        }
    }

    private void checkDummyPlayer(Tournament tournament) {
        int usedDummies = tournament.getDummyPlayer().size();
        int activePlayerCount = (int) tournament.getParticipants().stream().filter(p -> p.getStatus() == PlayerStatus.IN_TOURNAMENT || p.getStatus() == PlayerStatus.PLAYING).count();
        int neededDummies = usedDummies % 4 == 0 ? 0 : 4 - activePlayerCount % 4;
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                Player removeDummy = tournament.getDummyPlayer().remove(tournament.getDummyPlayer().size() - 1);
                removeDummy.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                Player dummy = playerService.getNextOrNewDummyPlayer();
                tournament.getDummyPlayer().add(dummy);
            }
        }
    }

    private List<GameTable> createGametables(int tableCount) {
        List<GameTable> newTables = new ArrayList<>();
        for (int i = 0; i < tableCount; i++) {
            GameTable newTable = new GameTable();
            newTable.setStatus(GameTableStatus.ACTIVE);
            newTable.setTableNumber(i + 1);
            newTable.setId(UUID.randomUUID());
            newTables.add(newTable);
        }
        return newTables;
    }
}
