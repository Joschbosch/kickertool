package zur.koeln.kickertool.application.handler;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.modelmapper.Converter;

import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.*;
import zur.koeln.kickertool.application.handler.dtos.base.*;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.PlayerRankingRow;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.model.entities.Settings;
import zur.koeln.kickertool.core.model.valueobjects.Team;

@Named
public class TournamentCommandHandler
    implements ITournamentCommandHandler {

    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    private final IPlayerService playerService;

    @Inject
    public TournamentCommandHandler(
        ITournamentService tournamentService,
        IPlayerService playerService,
        CustomModelMapper mapper) {
        this.tournamentService = tournamentService;
        this.playerService = playerService;
        this.mapper = mapper;

    }

    @Override
    public SingleResponseDTO<TournamentDTO> createAndStartNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings) {
        Tournament newTournament = tournamentService.createNewTournament(tournamentName, mapper.map(participants, Player.class), mapper.map(settings, Settings.class));
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
        List<Player> participants = tournamentService.addParticipantToTournament(tournamentIDToAdd, player);
        if (participants != null) {
            return createSuccessfulListResponse(participants);
        }
        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.VALIDATION_ERROR);
        response.setValidation(new ValidationDTO());
        response.getValidation().addErrorMsg("Spieler ist schon im Turnier.");
        return response;

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
            validation.addErrorMsg("Die aktuelle Runde ist noch nicht abgeschlossen.");
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
            validation.addErrorMsg("Das Matchergebnis ist nicht zulässig");
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
        ((TournamentDTO) returnDTO.getDtoValue()).setCurrentRound(tournament.getCurrentRound());
        return returnDTO;
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
