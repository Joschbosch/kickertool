package zur.koeln.kickertool.ui.controller.vms;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.shared.vms.TournamentDTOViewModel;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.tools.mapper.impl.ViewModelTournamentMapper;

@Getter(value = AccessLevel.PRIVATE)
@Component
public class TournamentMainViewModel extends FXViewModel{

	@Autowired
	ITournamentCommandHandler tournamentHandler;
	
	@Autowired
	ViewModelTournamentMapper tournamentMapper;
	
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
	
	
}
