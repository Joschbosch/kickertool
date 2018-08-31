package zur.koeln.kickertool.uifxml;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import zur.koeln.kickertool.TournamentController;
import javafx.event.ActionEvent;

public class FXMLMainMenuController {
	@FXML
	private Button btnCreateNewTournament;
	@FXML
	private Button btnManagePlayers;
	@FXML
	private TextField txtTournamentName;

	// Event Listener on Button[#btnCreateNewTournament].onAction
	@FXML
	public void onCreateNewTournamentClicked(ActionEvent event) {
		TournamentController.getInstance().createNewTournament(txtTournamentName.getText());
	}
	// Event Listener on Button[#btnManagePlayers].onAction
	@FXML
	public void onManagePlayersClicked(ActionEvent event) {
		TournamentController.getInstance().showPlayerPoolManagement();
	}
}
