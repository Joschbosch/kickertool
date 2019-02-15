package zur.koeln.kickertool.ui.vm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.vm.base.FXViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter
public class TournamentConfigurationViewModel extends FXViewModel {

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	IPlayerCommandHandler playerCommandHandler;

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	CustomModelMapper modelMapper;
	
	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	TournamentSettingsViewModel settingsVm;

	private final ObservableList<PlayerViewModel> availablePlayers = FXCollections.observableArrayList();
	private final ObservableList<PlayerViewModel> playersForTournament = FXCollections.observableArrayList();

	@Override
	public ModelValidationResult validate() {

		ModelValidationResult validation = ModelValidationResult.empty();

		if (getPlayersForTournament().isEmpty()) {
			validation.addValidationMessage("Bitte wählen Sie Spieler aus");
		}

		validation.addValidationResult(getSettingsVm().validate());
		
		return validation;
	}

	public List<PlayerViewModel> loadAllPlayer() throws BackgroundTaskException {

		ListResponseDTO<PlayerDTO> response = getPlayerCommandHandler().getAllPlayer();

		checkResponse(response);

		return getModelMapper().map(response.getDtoValueList(), PlayerViewModel.class);
	}
}
