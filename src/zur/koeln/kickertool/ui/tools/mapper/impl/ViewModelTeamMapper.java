package zur.koeln.kickertool.ui.tools.mapper.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.dtos.TeamDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.shared.vms.TeamDTOViewModel;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelTeamMapper implements IViewModelMapper<TeamDTO, TeamDTOViewModel> {

	@Autowired
	CustomModelMapper mapper;

    private UUID getPlayerDTO(UUID playerUUID, List<UUID> playerDTOs) {
        return playerDTOs.stream().filter(player -> player.equals(playerUUID)).findFirst().get();
	}

	@Override
	public TeamDTOViewModel map(TeamDTO dto, List<?>... optionalMatchLists) {

        List<UUID> playerIds = (List<UUID>) optionalMatchLists[0];

		TeamDTOViewModel vm = new TeamDTOViewModel();

        vm.setPlayer1(dto.getPlayer1Id());
        vm.setPlayer2(dto.getPlayer2Id());

		return vm;
	}

}
