package zur.koeln.kickertool.core.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.PlayerRankingComparator;
import zur.koeln.kickertool.core.kernl.PlayerRankingRow;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.model.entities.Match;
import zur.koeln.kickertool.core.model.entities.Settings;
import zur.koeln.kickertool.core.spi.ITournamentRepository;

@Named
public class TournamentService
    implements ITournamentService {

    private final ITournamentRepository tournamentRepo;

    private final IPlayerService playerService;


    @Inject
    public TournamentService(
        ITournamentRepository tournamentRepo,
        IPlayerService playerService) {
        this.tournamentRepo = tournamentRepo;
        this.playerService = playerService;
    }
    @Override
    public Tournament createNewTournament(String tournamentName, List<Player> participants, Settings settings) {

        Tournament newTournament = tournamentRepo.createNewTournament(tournamentName);
        newTournament.configureSettings(settings);
        newTournament.addParticipants(participants);
        tournamentRepo.saveOrUpdateTournament(newTournament);
        return newTournament;
    }
    @Override
    public Tournament startTournament(UUID tournamentIDToStart) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToStart);
        checkDummyPlayer(tournament);
        tournament.startTournament();
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
        tournament.addParticipant(player);
        checkDummyPlayer(tournament);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return getTournamentParticipants(tournamentIDToAdd);
    }

    @Override
    public List<Player> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participantId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToRemove);
        tournament.removeParticipant(playerService.getPlayerById(participantId));
        checkDummyPlayer(tournament);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return getTournamentParticipants(tournamentIDToRemove);
    }

    @Override
    public List<Player> getTournamentParticipants(UUID tournamentId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);

        return tournament.getAllParticipants();
    }

    @Override
    public boolean isCurrentRoundComplete(UUID tournamentId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        return tournament.isCurrentRoundComplete();
    }

    @Override
    public List<Match> getMatchesForRound(int roundNo, UUID tournamentId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        return tournament.getMatchesInTournamentRound(roundNo);
    }

    @Override
    public Tournament startNewRound(UUID tournamentToStartNewRound) {
        Tournament tournament = tournamentRepo.getTournament(tournamentToStartNewRound);
        if (!tournament.isCurrentRoundComplete()) {
            return null;
        }
        tournament.increaseRound();
        tournament.createNextMatches();
        tournament.updateGameTableUsage();
        tournamentRepo.saveOrUpdateTournament(tournament);
        return tournament;
    }

    private void checkDummyPlayer(Tournament tournament) {
        int usedDummies = tournament.getDummyPlayer().size();
        int activePlayerCount = tournament.getActivePlayerCount();
        int neededDummies = activePlayerCount % 4 == 0 ? 0 : 4 - activePlayerCount % 4;
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                tournament.removeLastDummyPlayer();
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
                tournament.addDummyPlayer(playerService.getDummyPlayer());
            }
        }
    }

    public List<PlayerRankingRow> getRankingByRound(UUID tournamentUID, int round) {
        Tournament tournament = tournamentRepo.getTournament(tournamentUID);
        List<Player> allParticipants = tournament.getAllParticipants();
        Collections.sort(allParticipants, new PlayerRankingComparator(tournament, round));

        List<PlayerRankingRow> ranking = new ArrayList<>();
        for (int i = 0; i < allParticipants.size(); i++) {
            PlayerRankingRow newRow = new PlayerRankingRow();
            Player player = allParticipants.get(i);
            newRow.setPlayer(player);
            newRow.setRank(i + 1);
            newRow.setScore((int) tournament.getScoreForPlayerInRound(player, round));

            newRow.setMatchesPlayed((int) tournament.getNoOfFinishedMatchesForPlayerInRound(player, round));
            newRow.setMatchesWon((int) tournament.getMatchesWonForPlayerInRound(player, round));
            newRow.setMatchesDraw((int) tournament.getMatchesDrawForPlayerInRound(player, round));
            newRow.setMatchesLost(newRow.getMatchesPlayed() - newRow.getMatchesWon() - newRow.getMatchesDraw());

            newRow.setGoals((int) tournament.getGoalsForPlayerInRound(player, round));
            newRow.setConcededGoals((int) tournament.getConcededGoalsForPlayerInRound(player, round));
            newRow.setGoaldiff(newRow.getGoals() - newRow.getConcededGoals());

            ranking.add(newRow);

        }
        return ranking;
    }

    @Override
    public void enterOrChangeMatchResult(UUID tournamentId, UUID matchID, int scoreHome, int scoreVisiting) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        tournament.addMatchResult(matchID, scoreHome, scoreVisiting);
        tournamentRepo.saveOrUpdateTournament(tournament);
    }
    @Override
    public Tournament getTournamentById(UUID tournamentUID) {
        return tournamentRepo.getTournament(tournamentUID);
    }

    @Override
    public Settings getDefaultSettings() {
        return new Settings();
    }
    @Override
    public Settings getSettings(UUID tournamentUid) {
        return tournamentRepo.getTournament(tournamentUid).getSettings();
    }


}
