package zur.koeln.kickertool.ui.controller;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.defaultimpl.DefaultBackgroundTask;
import zur.koeln.kickertool.ui.service.DialogContent;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLMainMenuController extends AbstractFXMLController{
	
	@FXML
	Button btnStartNewTournament;
	
	@FXML
	Button btnPlayerManagement;
	
	@FXML
	Button btnContinueTournament;

	
	@FXML 
	public void onStartNewTournamentClicked() {
		// TODO implement
	}

	@FXML 
	public void onPlayerManagementClicked() {
		openDialogue(DialogContent.PLAYER_MANAGEMENT_DIALOGUE, new DefaultBackgroundTask());
	}

	@FXML 
	public void OnContinueTournamentClicked() {
		// TODO implement
	} 
	

}
