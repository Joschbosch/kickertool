package zur.koeln.kickertool.ui.tools.mapper.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.dtos.PlayerRankingRowDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerRankingRowViewModel;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelRankingMapper implements IViewModelMapper<PlayerRankingRowDTO, PlayerRankingRowViewModel> {

	@Autowired
	CustomModelMapper mapper;
	
	@Override
	public PlayerRankingRowViewModel map(PlayerRankingRowDTO dto, List<?>... optionalMatchLists) {
		
		PlayerRankingRowViewModel newVm = new PlayerRankingRowViewModel();
		
		newVm.getNameProperty().set(dto.getPlayer().getFirstName() + " " + dto.getPlayer().getLastName());
		newVm.getRankProperty().set(dto.getRank());
		newVm.getScoreProperty().set(dto.getScore());
		
		return newVm;
	}

}
