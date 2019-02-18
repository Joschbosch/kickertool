package zur.koeln.kickertool.ui.vm;

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
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.vm.base.FXViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter
public class PlayerManagementViewModel extends FXViewModel{

	@Autowired 
	@Getter(value=AccessLevel.PRIVATE)
	IPlayerCommandHandler playerCommandHandler;
	
	@Autowired
	@Getter(value=AccessLevel.PRIVATE)
	CustomModelMapper modelMapper;

	private final ObservableList<PlayerViewModel> players = FXCollections.observableArrayList();
	
	public List<PlayerViewModel> loadAllPlayer() throws BackgroundTaskException{
		
		ListResponseDTO<PlayerDTO> response = getPlayerCommandHandler().getAllPlayer();
		
		checkResponse(response);
		
		return getModelMapper().map(response.getDtoValueList(), PlayerViewModel.class);
	}

	public PlayerViewModel updatePlayer(PlayerViewModel playerViewModel) throws BackgroundTaskException {
		
		SingleResponseDTO<PlayerDTO> response = getPlayerCommandHandler().updatePlayerName(playerViewModel.getUid(), playerViewModel.getFirstName(), playerViewModel.getLastName());
		
		checkResponse(response);
		
		return  getModelMapper().map(response.getDtoValue(), PlayerViewModel.class);
	}
	
	public PlayerViewModel insertNewPlayer(String firstName, String lastName) throws BackgroundTaskException {
		
		SingleResponseDTO<PlayerDTO> response = getPlayerCommandHandler().createNewPlayer(firstName, lastName);
		
		checkResponse(response);
		
		return getModelMapper().map(response.getDtoValue(), PlayerViewModel.class);
	}
	
	public List<PlayerViewModel> deletePlayer(List<PlayerViewModel> playerViewModels) throws BackgroundTaskException {
		
		List<PlayerViewModel> deletedPlayers = new ArrayList<>();
		
		for (PlayerViewModel playerViewModel : playerViewModels) {
			deletedPlayers.add(deletePlayer(playerViewModel));
		}
		
		return deletedPlayers;
		
	}
	
	private PlayerViewModel deletePlayer(PlayerViewModel playerViewModel) throws BackgroundTaskException {
		StatusOnlyDTO deletePlayer = getPlayerCommandHandler().deletePlayer(playerViewModel.getUid());
		
		checkResponse(deletePlayer);
		
		return playerViewModel;
	}

	@Override
	public ModelValidationResult validate() {
		
		return ModelValidationResult.empty();
	}

	public void updatePlayersList(PlayerViewModel updatedPlayerViewModel) {
		
		PlayerViewModel vmToUpdate = getPlayers().get(getPlayers().indexOf(updatedPlayerViewModel));
		vmToUpdate.setFirstName(updatedPlayerViewModel.getFirstName());
		vmToUpdate.setLastName(updatedPlayerViewModel.getLastName());
	}

}
