package zur.koeln.kickertool.application.handler;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.core.api.ITournamentService;

@Component
public class TournamentCommandHandler
    implements ITournamentCommandHandler {

    @Autowired
    private ITournamentService tournamentService;

    @Autowired
    private ModelMapper mapper;

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
    public boolean addParticipantToTournament(UUID tournamentIDToAdd, UUID player) {
        return tournamentService.addParticipantToTournament(tournamentIDToAdd, player);

    }

    @Override
    public boolean removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant) {
        return tournamentService.removeParticipantFromournament(tournamentIDToRemove, participant);
    }

}
