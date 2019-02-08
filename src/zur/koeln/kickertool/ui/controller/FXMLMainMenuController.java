package zur.koeln.kickertool.ui.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.DefaultDialogCallback;
import zur.koeln.kickertool.ui.api.IDialogCallback;
import zur.koeln.kickertool.ui.api.IFXMLController;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.service.Scenes;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLMainMenuController implements IFXMLController{
	
	@FXML
	Button btnStartNewTournament;
	
	@FXML
	Button btnPlayerManagement;
	
	@FXML
	Button btnContinueTournament;

	@FXML 
	StackPane rootStackPane;

	@FXML 
	public void onStartNewTournamentClicked() {
		// TODO implement
	}

	@FXML 
	public void onPlayerManagementClicked() {
		FXMLGuiService.getInstance().openDialogue(getRootStackPane(), Scenes.PLAYER_MANAGEMENT_DIALOGUE, new DefaultDialogCallback());
	}

	@FXML 
	public void OnContinueTournamentClicked() {
		// TODO implement
	} 
	

}
