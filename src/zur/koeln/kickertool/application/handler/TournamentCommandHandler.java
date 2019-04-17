package zur.koeln.kickertool.application.handler;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.PlayerRankingRowDTO;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusOnlyDTO;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.core.logic.PlayerRankingRow;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.model.entities.Settings;

@Component
public class TournamentCommandHandler
    implements ITournamentCommandHandler {

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private CustomModelMapper mapper;

    @Override
    public SingleResponseDTO<TournamentDTO> createNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings) {
        Tournament newTournament = tournamentService.createNewTournament(tournamentName, mapper.map(participants, Player.class), mapper.map(settings, Settings.class));
        return createSuccessfullDTO(newTournament);
    }

    public SingleResponseDTO<TournamentDTO> createAndStartNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings) {
        Tournament newTournament = tournamentService.createNewTournament(tournamentName, mapper.map(participants, Player.class), mapper.map(settings, Settings.class));
        newTournament = tournamentService.startTournament(newTournament.getUid());
        return createSuccessfullDTO(newTournament);
    }

    public SingleResponseDTO<TournamentDTO> getTournament(UUID tournamentUID) {
        return createSuccessfullDTO(tournamentService.getTournamentById(tournamentUID));
    }

    @Override
    public SingleResponseDTO<TournamentDTO> startTournament(UUID tournamentIDToStart) {
        Tournament tournament =tournamentService.startTournament(tournamentIDToStart);
        return createSuccessfullDTO(tournament);

    }

    @Override
    public SingleResponseDTO<TournamentDTO> renameTournament(UUID tournamentIDToRename, String name) {
        Tournament tournament = tournamentService.renameTournament(tournamentIDToRename, name);
        return createSuccessfullDTO(tournament);

    }

    @Override
    public ListResponseDTO<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID player) {
        List<Player> participants = tournamentService.addParticipantToTournament(tournamentIDToAdd, player);

        return createSuccessfulListResponse(participants);

    }

    @Override
    public ListResponseDTO<PlayerDTO> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant) {
        List<Player> participants = tournamentService.removeParticipantFromTournament(tournamentIDToRemove, participant);
        return createSuccessfulListResponse(participants);
    }

    @Override
    public SingleResponseDTO<TournamentDTO> startNextTournamentRound(UUID tournamentUUID) {
        Tournament tournament = tournamentService.startNewRound(tournamentUUID);
        return createSuccessfullDTO(tournament);
    }

    @Override
    public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentUUID, int round) {
        return createSuccessfulRankingListResponse(tournamentService.getRankingByRound(tournamentUUID, round));
    }

    public StatusOnlyDTO enterOrChangeMatchResult(UUID tournamentUUID, UUID matchId, int scoreHome, int scoreVisiting) {
        tournamentService.enterOrChangeMatchResult(tournamentUUID, matchId, scoreHome, scoreVisiting);
        StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
        statusOnlyDTO.setDtoStatus(StatusDTO.SUCCESS);
        return statusOnlyDTO;
    }

    private SingleResponseDTO<TournamentDTO> createSuccessfullDTO(Tournament tournament){
        SingleResponseDTO returnDTO = new SingleResponseDTO<>();
        returnDTO.setDtoStatus(StatusDTO.SUCCESS);
        returnDTO.setDtoValue(mapper.map(tournament, TournamentDTO.class));
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

}
