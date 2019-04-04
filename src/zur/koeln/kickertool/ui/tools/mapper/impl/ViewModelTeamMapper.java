package zur.koeln.kickertool.ui.tools.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TeamDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.TeamDTOViewModel;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelTeamMapper implements IViewModelMapper<TeamDTO, TeamDTOViewModel>{

	@Autowired
	CustomModelMapper mapper;
	
	@Override
	public TeamDTOViewModel map(TeamDTO dto) {
		TeamDTOViewModel vm = new TeamDTOViewModel();
		
		vm.setPlayer1(getMapper().map(dto.getPlayer1(), PlayerDTOViewModel.class));
		vm.setPlayer2(getMapper().map(dto.getPlayer2(), PlayerDTOViewModel.class));
		
		return vm;
	}

}
