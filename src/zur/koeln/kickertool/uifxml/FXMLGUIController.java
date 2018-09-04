package zur.koeln.kickertool.uifxml;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import zur.koeln.kickertool.base.AbstractGUIController;
import zur.koeln.kickertool.ui.GUIState;

public class FXMLGUIController extends AbstractGUIController{

	public FXMLGUIController(Stage stage, GUIState state) {
		super(stage, state);
	}

	@Override
	public void switchStateTo(GUIState newState) {
		
        Pane newPane = null;
        if (getCreatedPanes().containsKey(newState)) {
            newPane = getCreatedPanes().get(newState);
        } else {
        	try {
				newPane = (Pane) loadFXMLParent(newState);
			} catch (IOException e) {
				e.printStackTrace();
			}
            getCreatedPanes().put(newState, newPane);
        }
        getStage().getScene().setRoot(newPane);
		
	}
	
	private Parent loadFXMLParent(GUIState newState) throws IOException {
		
		switch (newState) {
		case MAIN_MENU:
			return FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		case NEW_TOURNAMENT_CONFIG:
			return FXMLLoader.load(getClass().getResource("TournamentConfiguration.fxml"));
		case PLAYER_CONFIG:
			break;
		case PLAYER_POOL:
			return FXMLLoader.load(getClass().getResource("PlayerPoolManagement.fxml"));
		case TOURNAMENT:
			break;
		default:
			return null;
		}
		
		return null;
	}

	@Override
	public void update() {	
		
	}

	
}
