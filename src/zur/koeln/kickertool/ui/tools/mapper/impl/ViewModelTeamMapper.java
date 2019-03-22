package zur.koeln.kickertool.ui.tools.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TeamDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;
import zur.koeln.kickertool.ui.vm.PlayerViewModel;
import zur.koeln.kickertool.ui.vm.TeamViewModel;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelTeamMapper implements IViewModelMapper<TeamDTO, TeamViewModel>{

	@Autowired
	CustomModelMapper mapper;
	
	@Override
	public TeamViewModel map(TeamDTO dto) {
		TeamViewModel vm = new TeamViewModel();
		
		vm.setUid(dto.getUid());
		vm.setPlayer1(getMapper().map(dto.getPlayer1(), PlayerViewModel.class));
		vm.setPlayer2(getMapper().map(dto.getPlayer2(), PlayerViewModel.class));
		
		return vm;
	}

}
