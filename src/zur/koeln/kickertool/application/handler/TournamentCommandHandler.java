package zur.koeln.kickertool.application.handler;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.SettingsDTO;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.StatusDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.model.Settings;
import zur.koeln.kickertool.core.model.Tournament;

@Component
public class TournamentCommandHandler
    implements ITournamentCommandHandler {

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private CustomModelMapper mapper;

    @Override
    public SingleResponseDTO<TournamentDTO> createNewTournament(String tournamentName, List<PlayerDTO> participants, SettingsDTO settings) {
        Tournament newTournament = tournamentService.createAndStartNewTournament(tournamentName, mapper.map(participants, Player.class), mapper.map(settings, Settings.class));

        return createSuccessfullDTO(newTournament);
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
        List<Player> participants = tournamentService.removeParticipantFromournament(tournamentIDToRemove, participant);
        return createSuccessfulListResponse(participants);
    }
    
    @Override
    public SingleResponseDTO<TournamentDTO> startNextTournamentRound(UUID tournamentUUID) {
        Tournament tournament = tournamentService.startNewRound(tournamentUUID);
        return createSuccessfullDTO(tournament);
    }

    @Override
    public SingleResponseDTO<PlayerDTO> pauseOrUnpausePlayer(UUID playerId, boolean pausing) {
        Player player = tournamentService.pauseOrUnpausePlayer(playerId, pausing);
        SingleResponseDTO response = new SingleResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValue(mapper.map(player, PlayerDTO.class));
        return response;
    }
    private SingleResponseDTO<TournamentDTO> createSuccessfullDTO(Tournament tournament){
        SingleResponseDTO returnDTO = new SingleResponseDTO<>();
        returnDTO.setDtoStatus(StatusDTO.SUCCESS);
        returnDTO.setDtoValue(mapper.map(tournament, TournamentDTO.class));
        return returnDTO;   
    }
    
    private ListResponseDTO<PlayerDTO> createSuccessfulListResponse(List<Player> participants) {
        ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
        response.setDtoStatus(StatusDTO.SUCCESS);
        response.setDtoValueList(mapper.map(participants, PlayerDTO.class));
        return response;
    }

}
