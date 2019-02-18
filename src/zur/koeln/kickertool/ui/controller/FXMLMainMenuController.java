package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.defaultimpl.DefaultDialogCloseEvent;
import zur.koeln.kickertool.ui.api.events.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.service.DialogContent;
import zur.koeln.kickertool.ui.vm.TournamentConfigurationViewModel;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLMainMenuController extends AbstractFXMLController{
	
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
		openDialog(DialogContent.TOURNAMENT_CONFIGURATION_DIALOG, new DialogCloseEvent<TournamentConfigurationViewModel>() {

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
				// TODO implement next steps
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	@FXML 
	public void onPlayerManagementClicked() {
		openDialog(DialogContent.PLAYER_MANAGEMENT_DIALOG, new DefaultDialogCloseEvent());
	}

	@FXML 
	public void OnContinueTournamentClicked() {
		// TODO implement
	} 
	

}
