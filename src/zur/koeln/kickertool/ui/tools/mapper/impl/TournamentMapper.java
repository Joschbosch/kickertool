package zur.koeln.kickertool.ui.tools.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.tools.mapper.IMapperDtoToVM;
import zur.koeln.kickertool.ui.vm.GameTableViewModel;
import zur.koeln.kickertool.ui.vm.PlayerViewModel;
import zur.koeln.kickertool.ui.vm.TournamentSettingsViewModel;
import zur.koeln.kickertool.ui.vm.TournamentViewModel;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class TournamentMapper implements IMapperDtoToVM<TournamentDTO, TournamentViewModel> {

	@Autowired
	CustomModelMapper mapper;
	
	@Autowired
	MatchMapper matchMapper;
	

	@Override
	public TournamentViewModel map(TournamentDTO dto) {
		TournamentViewModel vm = new TournamentViewModel();

		vm.setUid(dto.getUid());
		vm.setName(dto.getName());
		vm.setStatus(dto.getStatus());
		vm.setCurrentRoundIndex(dto.getCurrentRound());

		vm.setSettings(getMapper().map(dto.getSettings(), TournamentSettingsViewModel.class));
		vm.setPlayers(getMapper().map(dto.getParticipants(), PlayerViewModel.class));
		vm.setDummyPlayers(getMapper().map(dto.getDummyPlayer(), PlayerViewModel.class));
		vm.setMatches(matchMapper.map(dto.getMatches()));
		vm.setPlaytables(getMapper().map(dto.getPlaytables(), GameTableViewModel.class));
		
		return vm;
	}

}
