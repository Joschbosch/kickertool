package zur.koeln.kickertool.ui.controller.vms;

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
import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.api.dtos.SettingsDTO;
import zur.koeln.kickertool.application.api.dtos.base.ListResponseDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.api.ITournamentSettingsCommandHandler;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.vms.base.FXViewModel;
import zur.koeln.kickertool.ui.controller.vms.base.ModelValidationResult;
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
	ITournamentSettingsCommandHandler settingsCommandHandler;

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	CustomModelMapper modelMapper;
	
	@Autowired
	@Setter(value = AccessLevel.PRIVATE)
	TournamentSettingsViewModel settingsVm;

	private StringProperty tournamentNameProperty = new SimpleStringProperty();
	
	private final ObservableList<PlayerViewModel> availablePlayers = FXCollections.observableArrayList();
	private final ObservableList<PlayerViewModel> playersForTournament = FXCollections.observableArrayList();

	@Override
	public ModelValidationResult validate() {

		ModelValidationResult validation = ModelValidationResult.empty();

		if (getTournamentName() == null || getTournamentName().isEmpty()) {
			validation.addValidationMessage("Bitte geben Sie ein Turniernamen ein.");
		}
		
		if (getPlayersForTournament().isEmpty()) {
			validation.addValidationMessage("Bitte wählen Sie Spieler aus.");
		}

		validation.addValidationResult(getSettingsVm().validate());
		
		return validation;
	}

	public List<PlayerViewModel> loadAllPlayer() throws BackgroundTaskException {

		ListResponseDTO<PlayerDTO> response = getPlayerCommandHandler().getAllPlayer();

		checkResponse(response);

		return getModelMapper().map(response.getDtoValueList(), PlayerViewModel.class);
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
