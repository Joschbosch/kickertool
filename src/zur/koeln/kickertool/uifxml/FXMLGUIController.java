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
        getStage().setWidth(newPane.getPrefWidth());
		
	}
	
	private Parent loadFXMLParent(GUIState newState) throws IOException {
        FXMLLoader loader = null;
		switch (newState) {
		case MAIN_MENU:
                loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                break;
		case NEW_TOURNAMENT_CONFIG:
                loader = new FXMLLoader(getClass().getResource("TournamentConfiguration.fxml"));
                break;
		case PLAYER_CONFIG:
                loader = new FXMLLoader(getClass().getResource("PlayerSelection.fxml"));
                break;
		case PLAYER_POOL:
                loader = new FXMLLoader(getClass().getResource("PlayerPoolManagement.fxml"));
                break;
		case TOURNAMENT:
			break;
		default:
			return null;
		}
        loader.setControllerFactory(getCtx()::getBean);

        return loader.load();
	}

	@Override
	public void update() {	
		//
	}

	
}
