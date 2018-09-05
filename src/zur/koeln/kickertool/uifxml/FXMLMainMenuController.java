package zur.koeln.kickertool.uifxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.TournamentControllerService;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLMainMenuController {
	
    @Autowired
    private TournamentControllerService controller;
	@FXML
	private Button btnCreateNewTournament;
	@FXML
	private Button btnManagePlayers;
	@FXML
	private TextField txtTournamentName;

	@FXML
	public void initialize() {
		
		getBtnCreateNewTournament().disableProperty().bind(Bindings.greaterThan(1, getTxtTournamentName().textProperty().length()));
		
	}
	
	// Event Listener on Button[#btnCreateNewTournament].onAction
	@FXML
	public void onCreateNewTournamentClicked(ActionEvent event) {
        controller.createNewTournament(txtTournamentName.getText());
	}
	// Event Listener on Button[#btnManagePlayers].onAction
	@FXML
	public void onManagePlayersClicked(ActionEvent event) {
        controller.showPlayerPoolManagement();
	}
}
