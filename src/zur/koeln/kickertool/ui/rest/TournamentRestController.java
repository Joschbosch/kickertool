package zur.koeln.kickertool.ui.rest;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zur.koeln.kickertool.ui.common.ListResponseDTO;
import zur.koeln.kickertool.ui.common.SingleResponseDTO;
import zur.koeln.kickertool.ui.common.StatusOnlyDTO;
import zur.koeln.kickertool.ui.rest.adapter.TournamentRestAdapter;
import zur.koeln.kickertool.ui.rest.model.MatchResultDTO;
import zur.koeln.kickertool.ui.rest.model.PlayerDTO;
import zur.koeln.kickertool.ui.rest.model.PlayerRankingRowDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentConfigurationDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentDTO;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tournament")
public class TournamentRestController {

	private TournamentRestAdapter restAdapter;

	@Inject
	public TournamentRestController(TournamentRestAdapter restAdapter) {
		this.restAdapter = restAdapter;

	}

	@PostMapping("/createandstart")
	public SingleResponseDTO<TournamentDTO> createAndStartTournament(
			@RequestBody TournamentConfigurationDTO tournamentConfigDTO) {

		return restAdapter.createAndStartTournament(tournamentConfigDTO);
	}

	@GetMapping("/{uuid}")
	public SingleResponseDTO<TournamentDTO> getCurrentTournament(@PathVariable UUID uuid) {
		return restAdapter.getCurrentTournament(uuid);
	}

	@GetMapping("/getranking/{tournamentId}/{roundNo}")
	public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(@PathVariable UUID tournamentId,
			@PathVariable int roundNo) {
		return restAdapter.getRankingForRound(tournamentId, roundNo);
	}

	@PostMapping("/postmatchresult/{tournamentId}")
	public StatusOnlyDTO enterOrChangeMatchResult(@PathVariable UUID tournamentId, @RequestBody MatchResultDTO result) {

		return restAdapter.enterOrChangeMatchResult(tournamentId, result);
	}

	@PutMapping("/addplayer/{tournamentId}/{playerId}")
	public ListResponseDTO<PlayerDTO> addPlayerToTournament(@PathVariable UUID tournamentId,
			@PathVariable UUID playerId) {

		return restAdapter.addPlayerToTournament(tournamentId, playerId);
	}

	@DeleteMapping("/removeplayer/{tournamentId}/{playerId}")
	public ListResponseDTO<PlayerDTO> removePlayerFromTournament(@PathVariable UUID tournamentId,
			@PathVariable UUID playerId) {
		return restAdapter.removePlayerFromTournament(tournamentId, playerId);
	}

	@GetMapping("/nextround/{tournamentId}")
	public SingleResponseDTO<TournamentDTO> startNextRound(@PathVariable UUID tournamentId) {
		return restAdapter.startNextRound(tournamentId);
	}

	@PutMapping("/pauseplayer/{tournamentId}/{playerId}")
	public SingleResponseDTO<TournamentDTO> pausePlayer(@PathVariable UUID tournamentId, @PathVariable UUID playerId) {
		return restAdapter.pausePlayer(tournamentId, playerId, true);
	}

	@PutMapping("/unpauseplayer/{tournamentId}/{playerId}")
	public SingleResponseDTO<TournamentDTO> unpausePlayer(@PathVariable UUID tournamentId,
			@PathVariable UUID playerId) {
		return restAdapter.pausePlayer(tournamentId, playerId, false);
	}

}
