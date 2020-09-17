package zur.koeln.kickertool.ui.rest.adapter;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.Getter;
import zur.koeln.kickertool.core.bl.model.misc.PlayerRankingRow;
import zur.koeln.kickertool.core.bl.model.player.Player;
import zur.koeln.kickertool.core.bl.model.tournament.Match;
import zur.koeln.kickertool.core.bl.model.tournament.MatchStatus;
import zur.koeln.kickertool.core.bl.model.tournament.Settings;
import zur.koeln.kickertool.core.bl.model.tournament.Team;
import zur.koeln.kickertool.core.bl.model.tournament.Tournament;
import zur.koeln.kickertool.core.ports.ui.IPlayerManagementService;
import zur.koeln.kickertool.core.ports.ui.ITournamentService;
import zur.koeln.kickertool.ui.common.ListResponseDTO;
import zur.koeln.kickertool.ui.common.SingleResponseDTO;
import zur.koeln.kickertool.ui.common.StatusDTO;
import zur.koeln.kickertool.ui.common.StatusOnlyDTO;
import zur.koeln.kickertool.ui.common.ValidationDTO;
import zur.koeln.kickertool.ui.rest.adapter.tools.CustomModelMapper;
import zur.koeln.kickertool.ui.rest.model.MatchResultDTO;
import zur.koeln.kickertool.ui.rest.model.MatchDTO;
import zur.koeln.kickertool.ui.rest.model.PlayerDTO;
import zur.koeln.kickertool.ui.rest.model.PlayerRankingRowDTO;
import zur.koeln.kickertool.ui.rest.model.TeamDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentConfigurationDTO;
import zur.koeln.kickertool.ui.rest.model.TournamentDTO;

@Getter
@Component
public class TournamentRestAdapter {
	private final IPlayerManagementService playerService;

	private final ITournamentService tournamentService;

	private final CustomModelMapper mapper;

	@Inject
	public TournamentRestAdapter(IPlayerManagementService playerService, ITournamentService tournamentService,
			CustomModelMapper mapper) {
		this.playerService = playerService;
		this.tournamentService = tournamentService;
		this.mapper = mapper;

	}

	public SingleResponseDTO<TournamentDTO> createAndStartTournament(
			@RequestBody TournamentConfigurationDTO tournamentConfigDTO) {
		Tournament newTournament = tournamentService.createNewTournamentAndAddParticipants(
				tournamentConfigDTO.getName(), mapper.map(tournamentConfigDTO.getSelectedPlayer(), Player.class),
				mapper.map(tournamentConfigDTO.getSettings(), Settings.class));
		newTournament = tournamentService.startTournament(newTournament.getUid());
		return createSuccessfullDTO(newTournament);
	}

	public SingleResponseDTO<TournamentDTO> getCurrentTournament(UUID uuid) {

		return createSuccessfullDTO(tournamentService.getTournamentById(uuid));
	}

	public ListResponseDTO<PlayerRankingRowDTO> getRankingForRound(UUID tournamentId, int roundNo) {

		return createSuccessfulRankingListResponse(tournamentService.getRankingByRound(tournamentId, roundNo));
	}

	public StatusOnlyDTO enterOrChangeMatchResult(UUID tournamentId, MatchResultDTO result) {
		boolean accepted = tournamentService.enterOrChangeMatchResult(tournamentId, result.getMatchId(),
				result.getHomeScore(), result.getVisitingScore());
		StatusOnlyDTO statusOnlyDTO = new StatusOnlyDTO();
		statusOnlyDTO.setDtoStatus(accepted ? StatusDTO.SUCCESS : StatusDTO.VALIDATION_ERROR);
		if (!accepted) {
			ValidationDTO validation = new ValidationDTO();
			validation.addErrorMsg("Das Matchergebnis ist nicht zulaessig"); //$NON-NLS-1$
			statusOnlyDTO.setValidation(validation);
		}
		return statusOnlyDTO;
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

	private SingleResponseDTO<TournamentDTO> createSuccessfullDTO(Tournament tournament) {
		SingleResponseDTO returnDTO = new SingleResponseDTO<>();
		returnDTO.setDtoStatus(StatusDTO.SUCCESS);

		if (mapper.getTypeMap(Team.class, TeamDTO.class) == null) {
			Converter<Team, TeamDTO> teamConverter = context -> {
				TeamDTO newDTO = new TeamDTO();
				newDTO.setPlayer1(
						mapper.map(playerService.getPlayerById(context.getSource().getPlayer1Id()), PlayerDTO.class));
				newDTO.setPlayer2(
						mapper.map(playerService.getPlayerById(context.getSource().getPlayer2Id()), PlayerDTO.class));
				return newDTO;
			};

			mapper.createTypeMap(Team.class, TeamDTO.class).setConverter(teamConverter);
		}

		returnDTO.setDtoValue(mapper.map(tournament, TournamentDTO.class));
		((TournamentDTO) returnDTO.getDtoValue()).getMatches()
				.forEach(m -> m.setGameTableDescription(createTableDesc(m, tournament)));
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

	public ListResponseDTO<PlayerDTO> addPlayerToTournament(UUID tournamentId, UUID playerId) {
		List<Player> participants = tournamentService.addParticipantToTournamentAndReturnParticipants(tournamentId,
				playerId);
		if (participants != null) {
			return createSuccessfulListResponse(participants);
		}
		ListResponseDTO<PlayerDTO> response = new ListResponseDTO<>();
		response.setDtoStatus(StatusDTO.VALIDATION_ERROR);
		response.setValidation(new ValidationDTO());
		response.getValidation().addErrorMsg("Spieler ist schon im Turnier."); //$NON-NLS-1$
		return response;
	}

	public ListResponseDTO<PlayerDTO> removePlayerFromTournament(UUID tournamentId, UUID playerId) {
		List<Player> participants = tournamentService.removeParticipantFromTournament(tournamentId, playerId);
		return createSuccessfulListResponse(participants);
	}

	public SingleResponseDTO<TournamentDTO> startNextRound(UUID tournamentId) {
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

	public SingleResponseDTO<TournamentDTO> pausePlayer(UUID tournamentId, UUID playerId, boolean pause) {
		Tournament tournament = tournamentService.pauseOrUnpausePlayer(tournamentId, playerId, pause);
		return createSuccessfullDTO(tournament);
	}

}
