package zur.koeln.kickertool.ui.controller.dialogs.vms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.StatusOnlyDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;

@Component
@Getter
public class PlayerManagementViewModel extends FXViewModel{

	@Autowired 
	@Getter(value=AccessLevel.PRIVATE)
	IPlayerCommandHandler playerCommandHandler;
	
	@Autowired
	@Getter(value=AccessLevel.PRIVATE)
	CustomModelMapper modelMapper;

	private final ObservableList<PlayerDTOViewModel> players = FXCollections.observableArrayList();
	
	public List<PlayerDTOViewModel> loadAllPlayer() throws BackgroundTaskException{
		
		ListResponseDTO<PlayerDTO> response = getPlayerCommandHandler().getAllPlayer();
		
		checkResponse(response);
		
		return getModelMapper().map(response.getDtoValueList(), PlayerDTOViewModel.class);
	}

	public PlayerDTOViewModel updatePlayer(PlayerDTOViewModel playerViewModel) throws BackgroundTaskException {
		
		SingleResponseDTO<PlayerDTO> response = getPlayerCommandHandler().updatePlayerName(playerViewModel.getUid(), playerViewModel.getFirstName(), playerViewModel.getLastName());
		
		checkResponse(response);
		
		return  getModelMapper().map(response.getDtoValue(), PlayerDTOViewModel.class);
	}
	
	public PlayerDTOViewModel insertNewPlayer(String firstName, String lastName) throws BackgroundTaskException {
		
		SingleResponseDTO<PlayerDTO> response = getPlayerCommandHandler().createNewPlayer(firstName, lastName);
		
		checkResponse(response);
		
		return getModelMapper().map(response.getDtoValue(), PlayerDTOViewModel.class);
	}
	
	public List<PlayerDTOViewModel> deletePlayer(List<PlayerDTOViewModel> playerViewModels) throws BackgroundTaskException {
		
		List<PlayerDTOViewModel> deletedPlayers = new ArrayList<>();
		
		for (PlayerDTOViewModel playerViewModel : playerViewModels) {
			deletedPlayers.add(deletePlayer(playerViewModel));
		}
		
		return deletedPlayers;
		
	}
	
	private PlayerDTOViewModel deletePlayer(PlayerDTOViewModel playerViewModel) throws BackgroundTaskException {
		StatusOnlyDTO deletePlayer = getPlayerCommandHandler().deletePlayer(playerViewModel.getUid());
		
		checkResponse(deletePlayer);
		
		return playerViewModel;
	}

	@Override
	public ModelValidationResult validate() {
		
		return ModelValidationResult.empty();
	}

	public void updatePlayersList(PlayerDTOViewModel updatedPlayerViewModel) {
		
		PlayerDTOViewModel vmToUpdate = getPlayers().get(getPlayers().indexOf(updatedPlayerViewModel));
		vmToUpdate.setFirstName(updatedPlayerViewModel.getFirstName());
		vmToUpdate.setLastName(updatedPlayerViewModel.getLastName());
	}

}
