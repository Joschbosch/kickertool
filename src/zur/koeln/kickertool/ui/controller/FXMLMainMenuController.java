package zur.koeln.kickertool.ui.controller;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.AccessLevel;
import lombok.Getter;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLMainMenuController implements IFXMLController{
	
	@FXML
	private Button btnStartNewTournament;
	
	@FXML
	private Button btnPlayerManagement;
	
	@FXML
	private Button btnContinueTournament;

	@FXML 
	public void onStartNewTournamentClicked() {
		// TODO implement
	}

	@FXML 
	public void onPlayerManagementClicked() {
		// TODO implement
	}

	@FXML 
	public void OnContinueTournamentClicked() {
		// TODO implement
	} 
	

}
