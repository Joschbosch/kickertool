package zur.koeln.kickertool.application.handler;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;

@Component
public class TournamentCommandHandler
    implements ITournamentCommandHandler {

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private CustomModelMapper mapper;

    @Override
    public TournamentDTO createNewTournament() {
        return mapper.map(tournamentService.createNewTournament(), TournamentDTO.class);
    }

    @Override
    public TournamentDTO startTournament(UUID tournamentIDToStart) {
        return mapper.map(tournamentService.startTournament(tournamentIDToStart), TournamentDTO.class);

    }

    @Override
    public TournamentDTO renameTournament(UUID tournamentIDToRename, String name) {
        return mapper.map(tournamentService.renameTournament(tournamentIDToRename, name), TournamentDTO.class);

    }

    @Override
    public List<PlayerDTO> addParticipantToTournament(UUID tournamentIDToAdd, UUID player) {
        return mapper.map(tournamentService.addParticipantToTournament(tournamentIDToAdd, player), PlayerDTO.class);

    }

    @Override
    public List<PlayerDTO> removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant) {
        return mapper.map(tournamentService.removeParticipantFromournament(tournamentIDToRemove, participant), PlayerDTO.class);
    }

}
