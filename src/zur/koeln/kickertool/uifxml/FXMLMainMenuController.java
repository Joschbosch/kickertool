package zur.koeln.kickertool.uifxml;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.BackendController;

@Getter(value=AccessLevel.PRIVATE)
@Component
public class FXMLMainMenuController implements UpdateableUIComponent {
	
    @Autowired
    private BackendController backendController;
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
        backendController.createNewTournament(txtTournamentName.getText());
	}
	// Event Listener on Button[#btnManagePlayers].onAction
	@FXML
	public void onManagePlayersClicked(ActionEvent event) {
//        backendController.showPlayerPoolManagement();
		try {
			backendController.importAndStartTournament("Test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		//
	}
}
