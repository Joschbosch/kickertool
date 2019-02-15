package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.SettingsDTO;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
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
				SingleResponseDTO<TournamentDTO> createNewTournament = getTournamentCommandHandler().createNewTournament(result.getTournamentName(), result.getPlayerDTOsForTournament(), result.getSettingsDTO());
				System.out.println(createNewTournament);
			}
		});
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
