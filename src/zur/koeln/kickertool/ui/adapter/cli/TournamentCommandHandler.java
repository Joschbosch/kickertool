package zur.koeln.kickertool.ui.adapter.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.Converter;

import zur.koeln.kickertool.core.application.api.IPlayerManagementService;
import zur.koeln.kickertool.core.application.api.ITournamentService;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.tournament.*;
import zur.koeln.kickertool.core.domain.service.tournament.PlayerRankingRow;
import zur.koeln.kickertool.ui.adapter.cli.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.adapter.common.*;
import zur.koeln.kickertool.ui.adapter.common.base.*;
import zur.koeln.kickertool.ui.configuration.CustomModelMapper;

@Named
public class TournamentCommandHandler
    implements ITournamentCommandHandler {

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    private final IPlayerManagementService playerService;

    @Inject
    public TournamentCommandHandler(
        ITournamentService tournamentService,
        IPlayerManagementService playerService,
        CustomModelMapper mapper) {
        this.tournamentService = tournamentService;
        this.playerService = playerService;
        this.mapper = mapper;

    }

    @Override
    public SingleResponseDTO<TournamentDTO> createAndStartNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings) {
        Tournament newTournament = tournamentService.createNewTournamentAndAddParticipants(tournamentName, mapper.map(participants, Player.class), mapper.map(settings, Settings.class));
        newTournament = tournamentService.startTournament(newTournament.getUid());
        return createSuccessfullDTO(newTournament);
    }

    public SingleResponseDTO<TournamentDTO> getTournament(UUID tournamentUID) {
        return createSuccessfullDTO(tournamentService.getTournamentById(tournamentUID));
    }

    @Override
    public SingleResponseDTO<TournamentDTO> getTournamentById(UUID tournamentId) {
        return getTournament(tournamentId);
    }

    @Override
    public ListResponseDTO<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID player) {
        List<Player> participants = tournamentService.addParticipantToTournamentAndReturnParticipants(tournamentIDToAdd, player);
        if (participants != null) {
            return createSuccessfulListResponse(participants);
        }
        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.VALIDATION_ERROR);
        response.setValidation(new ValidationDTO());
        response.getValidation().addErrorMsg("Spieler ist schon im Turnier."); //$NON-NLS-1$
        return response;

    }

    @Override
    public ListResponseDTO<PlayerDTO> addParticipantsToTournament(UUID tournamentId, List<UUID> playerIds) {
        List<Player> participants = new ArrayList<>();
        for (UUID uuid : playerIds) {
            participants = tournamentService.addParticipantToTournamentAndReturnParticipants(tournamentId, uuid);
            if (participants == null) {
                ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
                response.setDtoStatus(StatusDTO.VALIDATION_ERROR);
                response.setValidation(new ValidationDTO());
                response.getValidation().addErrorMsg("Spieler ist schon im Turnier."); //$NON-NLS-1$
                return response;
            }
        }
        return createSuccessfulListResponse(participants);
    }

    @Override
    public ListResponseDTO<PlayerDTO> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participant) {
        List<Player> participants = tournamentService.removeParticipantFromTournament(tournamentIDToRemove, participant);
        return createSuccessfulListResponse(participants);
    }

    @Override
    public SingleResponseDTO<TournamentDTO> startNextTournamentRound(UUID tournamentUUID) {
        Tournament tournament = tournamentService.startNewRound(tournamentUUID);
        if (tournament == null) {
            SingleResponseDTO<TournamentDTO> failedDTO = new SingleResponseDTO<>();
            failedDTO.setDtoValue(null);
            failedDTO.setDtoStatus(StatusDTO.VALIDATION_ERROR);
            ValidationDTO validation = new ValidationDTO();
            validation.addErrorMsg("Die aktuelle Runde ist noch nicht abgeschlossen."); //$NON-NLS-1$
            failedDTO.setValidation(validation);
            return failedDTO;
        }
        return createSuccessfullDTO(tournament);
    }

    @Override
    public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentUUID, int round) {
        return createSuccessfulRankingListResponse(tournamentService.getRankingByRound(tournamentUUID, round));
    }
    @Override
    public StatusOnlyDTO enterOrChangeMatchResult(UUID tournamentUUID, UUID matchId, int scoreHome, int scoreVisiting) {
        boolean accepted = tournamentService.enterOrChangeMatchResult(tournamentUUID, matchId, scoreHome, scoreVisiting);
        StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
        statusOnlyDTO.setDtoStatus(accepted ? StatusDTO.SUCCESS : StatusDTO.VALIDATION_ERROR);
        if (!accepted) {
            ValidationDTO validation = new ValidationDTO();
            validation.addErrorMsg("Das Matchergebnis ist nicht zulässig"); //$NON-NLS-1$
            statusOnlyDTO.setValidation(validation);
        }
        return statusOnlyDTO;
    }

    private SingleResponseDTO<TournamentDTO> createSuccessfullDTO(Tournament tournament) {
        SingleResponseDTO returnDTO = new SingleResponseDTO<>();
        returnDTO.setDtoStatus(StatusDTO.SUCCESS);

        if (mapper.getTypeMap(Team.class, TeamDTO.class) == null) {
            Converter<Team, TeamDTO> teamConverter = context -> {
                TeamDTO newDTO = new TeamDTO();
                newDTO.setPlayer1(mapper.map(playerService.getPlayerById(context.getSource().getPlayer1Id()), PlayerDTO.class));
                newDTO.setPlayer2(mapper.map(playerService.getPlayerById(context.getSource().getPlayer2Id()), PlayerDTO.class));
                return newDTO;
            };

            mapper.createTypeMap(Team.class, TeamDTO.class).setConverter(teamConverter);
        }

        returnDTO.setDtoValue(mapper.map(tournament, TournamentDTO.class));
        ((TournamentDTO) returnDTO.getDtoValue()).getMatches().forEach(m -> m.setGameTableDescription(createTableDesc(m, tournament)));
        ((TournamentDTO) returnDTO.getDtoValue()).setCurrentRound(tournament.getCurrentRound());
        return returnDTO;
    }

    private String createTableDesc(MatchDTO m, Tournament tournament) {
        for( Match tm : tournament.getMatches()) {
            if (tm.getMatchID().equals(m.getMatchID())) {
                if (tm.getTable() != null) {
                    return String.valueOf(tm.getTable().getTableNumber());
                } else {
                    if (m.getStatus() == MatchStatus.PLANNED || m.getStatus() == MatchStatus.ONGOING) {
                        return "TBA"; //$NON-NLS-1$
                    } else if (m.getStatus() == MatchStatus.FINISHED) {
                        return "Finished"; //$NON-NLS-1$
                    }
                }
            }
        }
        return null;
    }

    private ListResponseDTO<PlayerRankingRowDTO> createSuccessfulRankingListResponse(List<PlayerRankingRow> ranking) {
        ListResponseDTO<PlayerRankingRowDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValueList(mapper.map(ranking, PlayerRankingRowDTO.class));
        return response;
    }

    private ListResponseDTO<PlayerDTO> createSuccessfulListResponse(List<Player> participants) {
        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValueList(mapper.map(participants, PlayerDTO.class));
        return response;
    }

    @Override
    public SingleResponseDTO<TournamentDTO> pauseOrUnpausePlayer(UUID tournamentUUID, UUID playerId, boolean pausing) {
        Tournament tournament = tournamentService.pauseOrUnpausePlayer(tournamentUUID, playerId, pausing);
        return createSuccessfullDTO(tournament);
    }
}
