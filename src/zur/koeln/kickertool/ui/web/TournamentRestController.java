package zur.koeln.kickertool.ui.web;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.*;
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

    @PostMapping("/createandstart")
    public SingleResponseDTO<TournamentDTO> createAndStartTournament(@RequestBody TournamentConfigurationDTO tournamentConfigDTO) {
        return tournamentCommandHandler.createAndStartNewTournament(tournamentConfigDTO.getName(), tournamentConfigDTO.getSelectedPlayer(), tournamentConfigDTO.getSettings());
    }

    @GetMapping("/{uuid}")
    public SingleResponseDTO<TournamentDTO> getCurrentTournament(@PathVariable UUID uuid) {
        return getTournamentCommandHandler().getTournamentById(uuid);
    }

    @GetMapping("/getranking/{tournamentId}/{roundNo}")
    public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(@PathVariable UUID tournamentId, @PathVariable int roundNo) {
        return tournamentCommandHandler.getRankingForRound(tournamentId, roundNo);
    }

    @PostMapping("/postmatchresult/{tournamentId}")
    public StatusOnlyDTO enterOrChangeMatchResult(@PathVariable UUID tournamentId, @RequestBody MatchResultDTO result) {
        return tournamentCommandHandler.enterOrChangeMatchResult(tournamentId, result.getMatchId(), result.getHomeScore(), result.getVisitingScore());
    }

    @PutMapping("/addplayer/{tournamentId}/{playerId}")
    public ListResponseDTO<PlayerDTO> addPlayerToTournament(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        return tournamentCommandHandler.addParticipantToTournament(tournamentId, playerId);
    }

    @DeleteMapping("/removeplayer/{tournamentId}/{playerId}")
    public ListResponseDTO<PlayerDTO> removePlayerFromTournament(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        return tournamentCommandHandler.removeParticipantFromTournament(tournamentId, playerId);
    }

    @GetMapping("/nextround/{tournamentId}")
    public SingleResponseDTO<TournamentDTO> startNextRound(@PathVariable UUID tournamentId) {
        return tournamentCommandHandler.startNextTournamentRound(tournamentId);
    }

    @PutMapping("/pauseplayer/{tournamentId}/{playerId}")
    public SingleResponseDTO<TournamentDTO> pausePlayer(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        return getTournamentCommandHandler().pauseOrUnpausePlayer(tournamentId, playerId, true);
    }

    @PutMapping("/unpauseplayer/{tournamentId}/{playerId}")
    public SingleResponseDTO<TournamentDTO> unpausePlayer(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
        return getTournamentCommandHandler().pauseOrUnpausePlayer(tournamentId, playerId, false);
    }
}
