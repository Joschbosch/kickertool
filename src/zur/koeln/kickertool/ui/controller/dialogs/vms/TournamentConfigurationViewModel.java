package zur.koeln.kickertool.ui.controller.dialogs.vms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.api.ITournamentConfigCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.dtos.SettingsDTO;
import zur.koeln.kickertool.application.handler.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;

@SuppressWarnings("nls")
@Component
@Getter
public class TournamentConfigurationViewModel extends FXViewModel {

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	IPlayerCommandHandler playerCommandHandler;
	
	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	ITournamentConfigCommandHandler settingsCommandHandler;

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	CustomModelMapper modelMapper;
	
	@Autowired
	@Setter(value = AccessLevel.PRIVATE)
	TournamentSettingsViewModel settingsVm;

	private StringProperty tournamentNameProperty = new SimpleStringProperty();
	
	private final ObservableList<PlayerDTOViewModel> availablePlayers = FXCollections.observableArrayList();
	private final ObservableList<PlayerDTOViewModel> playersForTournament = FXCollections.observableArrayList();

	@Override
	public ModelValidationResult validate() {

		ModelValidationResult validation = ModelValidationResult.empty();

		if (getTournamentName() == null || getTournamentName().isEmpty()) {
			validation.addValidationMessage("Bitte geben Sie ein Turniernamen ein.");
		}
		
		if (getPlayersForTournament().isEmpty()) {
			validation.addValidationMessage("Bitte w�hlen Sie Spieler aus.");
		}

		validation.addValidationResult(getSettingsVm().validate());
		
		return validation;
	}

	public List<PlayerDTOViewModel> loadAllPlayer() throws BackgroundTaskException {

		ListResponseDTO<PlayerDTO> response = getPlayerCommandHandler().getAllPlayer();

		checkResponse(response);

		return getModelMapper().map(response.getDtoValueList(), PlayerDTOViewModel.class);
	}
	
	public String getTournamentName() {
		return getTournamentNameProperty().get();
	}

	public void setDefaultSettings(TournamentSettingsViewModel defaultSettings) {
		setSettingsVm(defaultSettings);
	}

	public TournamentSettingsViewModel loadDefaultSettings() throws BackgroundTaskException {
		SingleResponseDTO<SettingsDTO> defaultSettings = getSettingsCommandHandler().getDefaultSettings();
		
		checkResponse(defaultSettings);

		return getModelMapper().map(defaultSettings.getDtoValue(), TournamentSettingsViewModel.class);
	}
	
	public List<PlayerDTO> getPlayerDTOsForTournament() {
		
		return getModelMapper().map(getPlayersForTournament(), PlayerDTO.class);
		
	}
	
	public SettingsDTO getSettingsDTO() {
		return getModelMapper().map(getSettingsVm(), SettingsDTO.class);
	}
}
