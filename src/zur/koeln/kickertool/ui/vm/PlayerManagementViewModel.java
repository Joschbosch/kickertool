package zur.koeln.kickertool.ui.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;

@Component
@Getter
public class PlayerManagementViewModel {

	@Autowired 
	@Getter(value=AccessLevel.PRIVATE)
	IPlayerCommandHandler playerCommandHandler;
	
	@Autowired
	@Getter(value=AccessLevel.PRIVATE)
	CustomModelMapper modelMapper;

	private final ObservableList<PlayerViewModel> players = FXCollections.observableArrayList();
	
	public void loadPlayersToList() {
		getPlayers().clear();
		
		getPlayers().addAll(getModelMapper().map(getPlayerCommandHandler().getAllPlayer(), PlayerViewModel.class));

	}

	public void updatePlayer(PlayerViewModel playerViewModel) {
		
		getPlayerCommandHandler().updatePlayerName(playerViewModel.getUid(), playerViewModel.getFirstName(), playerViewModel.getLastName());
	
	}
	
}
