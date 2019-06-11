package zur.koeln.kickertool.ui.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.PlayerRankingRowDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentConfigurationDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusOnlyDTO;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournament")
public class TournamentRestController {
    @Autowired
    @Getter(value = AccessLevel.PRIVATE)
    private ITournamentCommandHandler tournamentCommandHandler;

    @GetMapping("/createandstart")
    public SingleResponseDTO<TournamentDTO> createAndStartTournament(@RequestBody TournamentConfigurationDTO tournamentConfigDTO) {
        return tournamentCommandHandler.createAndStartNewTournament(tournamentConfigDTO.getName(), tournamentConfigDTO.getSelectedPlayer(), tournamentConfigDTO.getSettings());
    }

    @GetMapping("/getranking")
    public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentId, int roundNo) {
        return tournamentCommandHandler.getRankingForRound(tournamentId, roundNo);
    }

    @GetMapping("/enterresult")
    public StatusOnlyDTO enterOrChangeMatchResult(UUID tournamentId, UUID matchId, int scoreHome, int scoreVisiting) {
        return tournamentCommandHandler.enterOrChangeMatchResult(tournamentId, tournamentId, scoreHome, scoreVisiting);
    }

    @GetMapping("/addplayer")
    public ListResponseDTO<PlayerDTO> addPlayerToTournament(UUID tournamentId, UUID playerId) {
        return tournamentCommandHandler.addParticipantToTournament(tournamentId, playerId);
    }

    @GetMapping("/removeplayer")
    public ListResponseDTO<PlayerDTO> removePlayerFromTournament(UUID tournamentId, UUID playerId) {
        return tournamentCommandHandler.removeParticipantFromTournament(tournamentId, playerId);
    }
    @GetMapping("/nextround")
    public SingleResponseDTO<TournamentDTO> startNextRound(UUID tournamentId) {
        return tournamentCommandHandler.startNextTournamentRound(tournamentId);
    }
}
