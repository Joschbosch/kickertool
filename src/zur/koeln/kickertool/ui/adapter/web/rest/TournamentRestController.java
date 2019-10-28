package zur.koeln.kickertool.ui.adapter.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.modelmapper.Converter;
import org.springframework.web.bind.annotation.*;

import zur.koeln.kickertool.core.application.api.IPlayerManagementService;
import zur.koeln.kickertool.core.application.api.ITournamentService;
import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.tournament.*;
import zur.koeln.kickertool.core.domain.service.tournament.api.PlayerRankingRow;
import zur.koeln.kickertool.ui.adapter.common.*;
import zur.koeln.kickertool.ui.adapter.common.base.*;
import zur.koeln.kickertool.ui.configuration.CustomModelMapper;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournament")
public class TournamentRestController {


    private final ITournamentService tournamentService;

    private final CustomModelMapper mapper;

    private final IPlayerManagementService playerService;

    @Inject
    public TournamentRestController(
        ITournamentService tournamentService,
        IPlayerManagementService playerService,
        CustomModelMapper mapper) {
        this.tournamentService = tournamentService;
        this.playerService = playerService;
        this.mapper = mapper;

    }

    @PostMapping("/createandstart")
    public SingleResponseDTO<TournamentDTO> createAndStartTournament(@RequestBody TournamentConfigurationDTO tournamentConfigDTO) {
        Tournament newTournament = tournamentService.createNewTournamentAndAddParticipants(tournamentConfigDTO.getName(), mapper.map(tournamentConfigDTO.getSelectedPlayer(), Player.class),
                mapper.map(tournamentConfigDTO.getSettings(), Settings.class));
        newTournament = tournamentService.startTournament(newTournament.getUid());
        return createSuccessfullDTO(newTournament);
    }

    @GetMapping("/{uuid}")
    public SingleResponseDTO<TournamentDTO> getCurrentTournament(@PathVariable UUID uuid) {
        return createSuccessfullDTO(tournamentService.getTournamentById(uuid));
    }

    @GetMapping("/getranking/{tournamentId}/{roundNo}")
    public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(@PathVariable UUID tournamentId, @PathVariable int roundNo) {
        return createSuccessfulRankingListResponse(tournamentService.getRankingByRound(tournamentId, roundNo));
    }

    @PostMapping("/postmatchresult/{tournamentId}")
    public StatusOnlyDTO enterOrChangeMatchResult(@PathVariable UUID tournamentId, @RequestBody MatchResultDTO result) {
        boolean accepted = tournamentService.enterOrChangeMatchResult(tournamentId, result.getMatchId(), result.getHomeScore(), result.getVisitingScore());
        StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
        statusOnlyDTO.setDtoStatus(accepted ? StatusDTO.SUCCESS : StatusDTO.VALIDATION_ERROR);
        if (!accepted) {
            ValidationDTO validation = new ValidationDTO();
            validation.addErrorMsg("Das Matchergebnis ist nicht zulässig"); //$NON-NLS-1$
            statusOnlyDTO.setValidation(validation);
        }
        return statusOnlyDTO;
    }

    @PutMapping("/addplayer/{tournamentId}/{playerId}")
    public ListResponseDTO<PlayerDTO> addPlayerToTournament(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        List<Player> participants = tournamentService.addParticipantToTournamentAndReturnParticipants(tournamentId, playerId);
        if (participants != null) {
            return createSuccessfulListResponse(participants);
        }
        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.VALIDATION_ERROR);
        response.setValidation(new ValidationDTO());
        response.getValidation().addErrorMsg("Spieler ist schon im Turnier."); //$NON-NLS-1$
        return response;
    }

    @PutMapping("/addplayers/{tournamentId}")
    public ListResponseDTO<PlayerDTO> addPlayerToTournament(@PathVariable UUID tournamentId, @RequestBody Map<String, List<UUID>> playerIds) {
        List<UUID> idList = playerIds.get("playerIds"); //$NON-NLS-1$
        List<Player> participants = new ArrayList<>();
        for (UUID uuid : idList) {
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

    @DeleteMapping("/removeplayer/{tournamentId}/{playerId}")
    public ListResponseDTO<PlayerDTO> removePlayerFromTournament(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        List<Player> participants = tournamentService.removeParticipantFromTournament(tournamentId, playerId);
        return createSuccessfulListResponse(participants);
    }

    @GetMapping("/nextround/{tournamentId}")
    public SingleResponseDTO<TournamentDTO> startNextRound(@PathVariable UUID tournamentId) {
        Tournament tournament = tournamentService.startNewRound(tournamentId);
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

    @PutMapping("/pauseplayer/{tournamentId}/{playerId}")
    public SingleResponseDTO<TournamentDTO> pausePlayer(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        Tournament tournament = tournamentService.pauseOrUnpausePlayer(tournamentId, playerId, true);
        return createSuccessfullDTO(tournament);
    }

    @PutMapping("/unpauseplayer/{tournamentId}/{playerId}")
    public SingleResponseDTO<TournamentDTO> unpausePlayer(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        Tournament tournament = tournamentService.pauseOrUnpausePlayer(tournamentId, playerId, false);
        return createSuccessfullDTO(tournament);
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
        for (Match tm : tournament.getMatches()) {
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
}
