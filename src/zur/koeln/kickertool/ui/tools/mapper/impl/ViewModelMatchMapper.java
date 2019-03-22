package zur.koeln.kickertool.ui.tools.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.MatchDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;
import zur.koeln.kickertool.ui.vm.GameTableViewModel;
import zur.koeln.kickertool.ui.vm.MatchViewModel;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelMatchMapper implements IViewModelMapper<MatchDTO, MatchViewModel>{

	@Autowired
	CustomModelMapper mapper;
	
	@Autowired
	ViewModelTeamMapper teamMapper;
	
	@Override
	public MatchViewModel map(MatchDTO dto) {
		MatchViewModel vm = new MatchViewModel();
		
		vm.setMatchID(dto.getMatchID());
		vm.setRoundNumber(dto.getRoundNumber());
		vm.setScoreHome(dto.getScoreHome());
		vm.setScoreVisiting(dto.getScoreVisiting());
		vm.setStatus(dto.getStatus());
		vm.setTournamentId(dto.getTournament().getUid());
		vm.setHomeTeam(getTeamMapper().map(dto.getHomeTeam()));
		vm.setVisitingTeam(getTeamMapper().map(dto.getVisitingTeam()));
		vm.setTable(getMapper().map(dto.getTable(), GameTableViewModel.class));
		
		return vm;
	}

}
