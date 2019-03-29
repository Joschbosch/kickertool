package zur.koeln.kickertool.ui.tools.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.MatchDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.shared.vms.GameTableDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelMatchMapper implements IViewModelMapper<MatchDTO, MatchDTOViewModel>{

	@Autowired
	CustomModelMapper mapper;
	
	@Autowired
	ViewModelTeamMapper teamMapper;
	
	@Override
	public MatchDTOViewModel map(MatchDTO dto) {
		MatchDTOViewModel vm = new MatchDTOViewModel();
		
		vm.setMatchID(dto.getMatchID());
		vm.setRoundNumber(dto.getRoundNumber());
		vm.setScoreHome(dto.getScoreHome());
		vm.setScoreVisiting(dto.getScoreVisiting());
		vm.setStatus(dto.getStatus());
		vm.setTournamentId(dto.getTournament().getUid());
		vm.setHomeTeam(getTeamMapper().map(dto.getHomeTeam()));
		vm.setVisitingTeam(getTeamMapper().map(dto.getVisitingTeam()));
		vm.setTable(getMapper().map(dto.getTable(), GameTableDTOViewModel.class));
		
		return vm;
	}

}