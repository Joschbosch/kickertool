/**
 * 
 */
package zur.koeln.kickertool.ui;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.base.AbstractGUIController;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.ui.tournament.TournamentPane;

@Getter
@Setter
public class GUIController extends AbstractGUIController {

    private BackendController controller;

    public GUIController(Stage stage, GUIState state) {
		super(stage, state);
	}

    /**
     * @param newState
     */
    @Override
    public void switchStateTo(GUIState newState) {
        Pane newPane = null;
        if (getCreatedPanes().containsKey(newState)) {
            newPane = getCreatedPanes().get(newState);
        } else {
            switch (newState) {
            case MAIN_MENU:
                    newPane = new MainMenuPane(getController());
                break;
            case PLAYER_POOL:
                    newPane = new PlayerPoolManagementPane(getController());
                break;
            case NEW_TOURNAMENT_CONFIG:
                    newPane = new TournamentConfigurationPane(getController());
                break;
            case PLAYER_CONFIG:
                    newPane = new PlayerAssignmentPane(getController());
                break;
            case TOURNAMENT:
                    newPane = new TournamentPane(getController());
                break;
            default:
                break;
            }
            getCreatedPanes().put(newState, newPane);
        }
        getStage().getScene().setRoot(newPane);
    }

    /**
     * 
     */
    @Override
    public void update() {
        TournamentPane pane = ((TournamentPane) getCreatedPanes().get(GUIState.TOURNAMENT));
        pane.update();
    }

}
