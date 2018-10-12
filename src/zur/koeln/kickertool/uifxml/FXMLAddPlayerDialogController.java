package zur.koeln.kickertool.uifxml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import zur.koeln.kickertool.api.player.Player;

@Component
public class FXMLAddPlayerDialogController {

	@FXML
	private GridPane playerPoolGrid;

	@Autowired
	private FXMLPlayerPoolManagementController playerPoolManagementController;

	private GridPane getPlayerPoolGrid() {
		return playerPoolGrid;
	}

	private FXMLPlayerPoolManagementController getPlayerPoolManagementController() {
		return playerPoolManagementController;
	}

	public void addPlayerPoolManagementGridPane(GridPane playerPoolManagementGridPane) {
		getPlayerPoolGrid().getChildren().clear();
		getPlayerPoolGrid().getChildren().add(playerPoolManagementGridPane);
	}
	
	public void init() {
		getPlayerPoolManagementController().loadPlayersNotInTournament();
	}

	public List<Player> getSelectedPlayer() {
		return getPlayerPoolManagementController().getSelectedPlayer();
	}

}
