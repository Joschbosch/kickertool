package zur.koeln.kickertool.ui.tools.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.dialogs.vms.TournamentSettingsViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.GameTableDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.TournamentDTOViewModel;
import zur.koeln.kickertool.ui.tools.mapper.IViewModelMapper;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class ViewModelTournamentMapper implements IViewModelMapper<TournamentDTO, TournamentDTOViewModel> {

	@Autowired
	CustomModelMapper mapper;
	
	@Autowired
	ViewModelMatchMapper matchMapper;
	

	@Override
	public TournamentDTOViewModel map(TournamentDTO dto) {
		TournamentDTOViewModel vm = new TournamentDTOViewModel();

		vm.setUid(dto.getUid());
		vm.setName(dto.getName());
		vm.setStatus(dto.getStatus());
		vm.setCurrentRoundIndex(dto.getCurrentRound());

		vm.setSettings(getMapper().map(dto.getSettings(), TournamentSettingsViewModel.class));
		vm.setPlayers(getMapper().map(dto.getParticipants(), PlayerDTOViewModel.class));
		vm.setDummyPlayers(getMapper().map(dto.getDummyPlayer(), PlayerDTOViewModel.class));
		vm.setMatches(matchMapper.map(dto.getMatches()));
		vm.setPlaytables(getMapper().map(dto.getPlaytables(), GameTableDTOViewModel.class));
		
		return vm;
	}

}