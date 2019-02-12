package zur.koeln.kickertool.ui.vm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.StatusDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;

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
	
	public List<PlayerViewModel> loadPlayersToList() throws BackgroundTaskException{
		
		ListResponseDTO<PlayerDTO> response = getPlayerCommandHandler().getAllPlayer();
		
		if (response.getDtoStatus() != StatusDTO.SUCCESS) {
			throw new BackgroundTaskException(response.getValidation().toString());
		}
		
		return getModelMapper().map(response.getDtoValueList(), PlayerViewModel.class);
	}

	public void updatePlayer(PlayerViewModel playerViewModel) throws BackgroundTaskException {
		
		SingleResponseDTO<PlayerDTO> response = getPlayerCommandHandler().updatePlayerName(playerViewModel.getUid(), playerViewModel.getFirstName(), playerViewModel.getLastName());
		
		if (response.getDtoStatus() != StatusDTO.SUCCESS) {
			throw new BackgroundTaskException(response.getValidation().toString());
		}
		
	}
	
}
