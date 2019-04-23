package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.application.handler.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.BackgroundTask;
import zur.koeln.kickertool.ui.controller.base.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.base.impl.DefaultDialogCloseEvent;
import zur.koeln.kickertool.ui.controller.dialogs.vms.TournamentConfigurationViewModel;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.shared.DialogContentDefinition;
import zur.koeln.kickertool.ui.shared.GUIEvents;
import zur.koeln.kickertool.ui.shared.SceneDefinition;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class MainMenuController extends AbstractController{
	
	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	ITournamentCommandHandler tournamentCommandHandler;

	@FXML
	Button btnStartNewTournament;
	
	@FXML
	Button btnPlayerManagement;
	
	@FXML
	Button btnContinueTournament;

	
	@FXML 
	public void onStartNewTournamentClicked() {
		openDialog(DialogContentDefinition.TOURNAMENT_CONFIGURATION_DIALOG, new DialogCloseEvent<TournamentConfigurationViewModel>() {

			@Override
			public void doAfterDialogClosed(TournamentConfigurationViewModel result) {
				startBackgroundTask(createNewTournamentTask(result));
			}
		});
	}
	
	private BackgroundTask createNewTournamentTask(TournamentConfigurationViewModel tournamentConfig) {

		return new BackgroundTask<TournamentDTO>() {

			@Override
			public TournamentDTO performTask() throws Exception {
				SingleResponseDTO<TournamentDTO> createNewTournament = getTournamentCommandHandler().createNewTournament(tournamentConfig.getTournamentName(), tournamentConfig.getPlayerDTOsForTournament(), tournamentConfig.getSettingsDTO());
				checkResponse(createNewTournament);
				return createNewTournament.getDtoValue();
			}

			@Override
			public void doOnSuccess(TournamentDTO result) {
				FXMLGuiService.getInstance().switchScene(SceneDefinition.TOURNAMENT_CONTROLLING, result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	@FXML 
	public void onPlayerManagementClicked() {
		openDialog(DialogContentDefinition.PLAYER_MANAGEMENT_DIALOG, new DefaultDialogCloseEvent());
	}

	@FXML 
	public void OnContinueTournamentClicked() {
		// TODO implement
	}

	@Override
	public void handleEvent(GUIEvents guiEvents, Object content) {
		// nothing to do here
	}

	@Override
	protected void registerEvents() {
		// nothing to do here
	} 
	

}
