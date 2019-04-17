package zur.koeln.kickertool.ui.tools.mapper.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.TeamDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.TeamDTOViewModel;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelTeamMapper implements IViewModelMapper<TeamDTO, TeamDTOViewModel> {

	@Autowired
	CustomModelMapper mapper;

	private PlayerDTO getPlayerDTO(UUID playerUUID, List<PlayerDTO> playerDTOs) {
		return playerDTOs.stream().filter(player -> player.getUid().equals(playerUUID)).findFirst().get();
	}

	@Override
	public TeamDTOViewModel map(TeamDTO dto, List<?>... optionalMatchLists) {
		
		List<PlayerDTO> playerDTOs = (List<PlayerDTO>) optionalMatchLists[0];
		
		TeamDTOViewModel vm = new TeamDTOViewModel();

		vm.setPlayer1(getMapper().map(getPlayerDTO(dto.getPlayer1Id(), playerDTOs), PlayerDTOViewModel.class));
		vm.setPlayer2(getMapper().map(getPlayerDTO(dto.getPlayer2Id(), playerDTOs), PlayerDTOViewModel.class));

		return vm;
	}
	
}
