package zur.koeln.kickertool.ui.controller.vms;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerRankingRowDTO;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusDTO;
import zur.koeln.kickertool.application.handler.dtos.base.StatusOnlyDTO;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerRankingRowViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.TournamentDTOViewModel;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.tools.mapper.impl.ViewModelRankingMapper;
import zur.koeln.kickertool.ui.tools.mapper.impl.ViewModelTournamentMapper;

@Getter(value = AccessLevel.PRIVATE)
@Component
public class TournamentMainViewModel extends FXViewModel{

	@Autowired
	ITournamentCommandHandler tournamentHandler;
	
	@Autowired
	ViewModelTournamentMapper tournamentMapper;
	
	@Autowired
	ViewModelRankingMapper rankingMapper;
	
	public TournamentDTOViewModel startNewTournamentRound(UUID idTournament) throws BackgroundTaskException {
		SingleResponseDTO<TournamentDTO> dtoResponse = getTournamentHandler().startNextTournamentRound(idTournament);
		
		checkResponse(dtoResponse);
		
		return mapFromTournamentDTO(dtoResponse.getDtoValue());
	}	
	
	
	public TournamentDTOViewModel mapFromTournamentDTO(TournamentDTO tournamentDTO) {
		return getTournamentMapper().map(tournamentDTO);
	}
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

	public void upateMatchResult(MatchDTOViewModel matchDTOViewModel) {
		getTournamentHandler().enterOrChangeMatchResult(matchDTOViewModel.getTournamentId(), matchDTOViewModel.getMatchID(), matchDTOViewModel.getScoreHome(), matchDTOViewModel.getScoreVisiting());
	}
	
	public List<PlayerRankingRowViewModel> getPlayerRankings(UUID idTournament, int currentRound) throws BackgroundTaskException {
		ListResponseDTO<PlayerRankingRowDTO> listResponse = getTournamentHandler().getRankingForRound(idTournament, currentRound);
		
		checkResponse(listResponse);
		
		return getRankingMapper().map(listResponse.getDtoValueList());
	}
	
}
