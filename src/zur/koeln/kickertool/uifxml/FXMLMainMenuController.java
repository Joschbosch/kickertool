package zur.koeln.kickertool.uifxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.TournamentController;

@Getter(value=AccessLevel.PRIVATE)
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
