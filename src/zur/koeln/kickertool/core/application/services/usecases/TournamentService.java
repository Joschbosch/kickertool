package zur.koeln.kickertool.core.application.services.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.application.api.IPlayerManagementService;
import zur.koeln.kickertool.core.application.api.ITournamentService;
import zur.koeln.kickertool.core.application.services.usecases.spi.ITournamentRepository;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Match;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Settings;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;
import zur.koeln.kickertool.core.domain.service.tournament.api.ITournamentFactory;
import zur.koeln.kickertool.core.domain.service.tournament.api.ITournamentParticipantManager;
import zur.koeln.kickertool.core.domain.service.tournament.api.ITournamentRoundService;
import zur.koeln.kickertool.core.domain.service.tournament.api.PlayerRankingRow;

@Named
public class TournamentService
    implements ITournamentService {

    private final ITournamentRepository tournamentRepo;

    private final IPlayerManagementService playerService;

    private final ITournamentFactory tournamentFactory;

    private final ITournamentParticipantManager participantManager;

    private final ITournamentRoundService tournamentRoundService;

    @Inject
    public TournamentService(
        ITournamentRepository tournamentRepo,
        IPlayerManagementService playerService,
        ITournamentFactory tournamentFactory,
        ITournamentParticipantManager participantManager,
        ITournamentRoundService tournamentRoundService) {
        this.tournamentRepo = tournamentRepo;
        this.playerService = playerService;
        this.tournamentFactory = tournamentFactory;
        this.participantManager = participantManager;
        this.tournamentRoundService = tournamentRoundService;
    }

    @Override
    public Tournament createNewTournamentAndAddParticipants(String tournamentName, List<Player> participants, Settings settings) {

        Tournament newTournament = tournamentFactory.createNewTournament(tournamentName);
        newTournament.configureSettings(settings);
        tournamentRepo.saveOrUpdateTournament(newTournament);

        participantManager.addParticipantsToTournament(participants, newTournament);
        List<Player> newDummyPlayer = participantManager.checkDummyPlayer(newTournament, participants, playerService.getDummyPlayers());
        playerService.storeNewDummyPlayer(newDummyPlayer);
        tournamentRepo.saveOrUpdateTournament(newTournament);
        return newTournament;
    }

    @Override
    public Tournament startTournament(UUID tournamentIDToStart) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToStart);
        participantManager.checkDummyPlayer(tournament, getTournamentParticipants(tournamentIDToStart), playerService.getDummyPlayers());
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
    public List<Player> addParticipantToTournamentAndReturnParticipants(UUID tournamentIDToAdd, UUID participant) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToAdd);
        Player playerById = playerService.getPlayerById(participant);
        participantManager.addParticipantToTournament(tournament, playerById, getTournamentParticipants(tournamentIDToAdd));
        tournamentRepo.saveOrUpdateTournament(tournament);
        playerService.setPlayerStatus(participant, PlayerStatus.IN_TOURNAMENT);
        return getTournamentParticipants(tournamentIDToAdd);
    }

    @Override
    public List<Player> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participantId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToRemove);
        participantManager.removeFromTournament(tournament, participantId, getTournamentParticipants(tournamentIDToRemove));
        List<Player> newDummyPlayer = participantManager.checkDummyPlayer(tournament, getTournamentParticipants(tournamentIDToRemove), playerService.getDummyPlayers());
        playerService.storeNewDummyPlayer(newDummyPlayer);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return getTournamentParticipants(tournamentIDToRemove);
    }

    @Override
    public List<Player> getTournamentParticipants(UUID tournamentId) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        List<UUID> allParticipantIds = tournament.getAllParticipants();
        List<Player> participants = new ArrayList<>();
        allParticipantIds.forEach(id -> {

            Player playerById = playerService.getPlayerById(id);
            if (playerById != null) {
                participants.add(playerById);
            } else {
                tournament.removeParticipant(id);
            }
        });
        return participants;
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
        boolean roundStarted = tournamentRoundService.startNewRound(tournament, getTournamentParticipants(tournament.getUid()));
        if (roundStarted) {
            tournamentRepo.saveOrUpdateTournament(tournament);
            return tournament;
        }
        return null;
    }

    @Override
    public boolean enterOrChangeMatchResult(UUID tournamentId, UUID matchID, int scoreHome, int scoreVisiting) {
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        boolean accepted = tournament.addMatchResult(matchID, scoreHome, scoreVisiting);

        if (accepted) {
            tournamentRepo.saveOrUpdateTournament(tournament);
        }
        return accepted;
    }
    @Override
    public Tournament getTournamentById(UUID tournamentUID) {
        Tournament tournament = tournamentRepo.getTournament(tournamentUID);
        Map<UUID, Player> playerInTournament = playerService.getPlayersMapByIds(tournament.getAllParticipants());
        participantManager.updateParticipantsStatus(tournament, playerInTournament);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return tournament;
    }

    @Override
    public Settings getDefaultSettings() {
        return new Settings();
    }
    @Override
    public Settings getSettings(UUID tournamentUid) {
        return tournamentRepo.getTournament(tournamentUid).getSettings();
    }
    @Override
    public Tournament pauseOrUnpausePlayer(UUID tournamentId, UUID playerToPause, boolean pausing) {
        playerService.setPlayerStatus(playerToPause, pausing ? PlayerStatus.PAUSING_TOURNAMENT : PlayerStatus.IN_TOURNAMENT);
        Tournament tournament = tournamentRepo.getTournament(tournamentId);
        List<Player> newDummyPlayer = participantManager.checkDummyPlayer(tournament, getTournamentParticipants(tournamentId), playerService.getDummyPlayers());
        playerService.storeNewDummyPlayer(newDummyPlayer);
        tournamentRepo.saveOrUpdateTournament(tournament);
        return tournament;
    }

    @Override
    public List<PlayerRankingRow> getRankingByRound(UUID tournamentUID, int round) {
        Tournament tournament = getTournamentById(tournamentUID);
        List<Player> tournamentParticipants = getTournamentParticipants(tournamentUID);
        return tournamentRoundService.getRankingByRound(tournament, tournamentParticipants, round);
    }

}
