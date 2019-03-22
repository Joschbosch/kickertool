package zur.koeln.kickertool.ui.vm;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.tools.mapper.impl.TournamentMapper;
import zur.koeln.kickertool.ui.vm.base.FXViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Getter(value = AccessLevel.PRIVATE)
@Component
public class TournamentManagementViewModel extends FXViewModel{

	@Autowired
	ITournamentCommandHandler tournamentHandler;
	
	@Autowired
	TournamentMapper tournamentMapper;
	
	public TournamentViewModel startNewTournamentRound(UUID idTournament) throws BackgroundTaskException {
		SingleResponseDTO<TournamentDTO> dtoResponse = getTournamentHandler().startNextTournamentRound(idTournament);
		
		checkResponse(dtoResponse);
		
		return getTournamentMapper().map(dtoResponse.getDtoValue());
	}	
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}
	
	
}
