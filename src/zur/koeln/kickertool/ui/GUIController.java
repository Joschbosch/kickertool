/**
 * 
 */
package zur.koeln.kickertool.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.ui.tournament.TournamentPane;

@RequiredArgsConstructor
@Setter
public class GUIController {

    @NonNull
    private Stage stage;

    @NonNull
    private GUIState state;

    private TournamentController controller;

    private Map<GUIState, Pane> createdPanes = new HashMap<>();

    /**
     * @param controller2
     * 
     */
    public void init(TournamentController controller) {
        this.controller = controller;
        stage.setTitle("Kicker APP");
        Pane root = new MainMenuPane(controller);
        Scene mainScene = new Scene(root, 1200, 800);
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * @param newState
     */
    public void switchStateTo(GUIState newState) {
        Pane newPane = null;
        if (createdPanes.containsKey(newState)) {
            newPane = createdPanes.get(newState);
        } else {
            switch (newState) {
            case MAIN_MENU:
                newPane = new MainMenuPane(controller);
                break;
            case PLAYER_POOL:
                newPane = new PlayerPoolManagementPane(controller);
                break;
            case NEW_TOURNAMENT_CONFIG:
                newPane = new TournamentConfigurationPane(controller);
                break;
            case PLAYER_CONFIG:
                newPane = new PlayerAssignmentPane(controller);
                break;
            case TOURNAMENT:
                newPane = new TournamentPane(controller);
                break;
            default:
                break;
            }
            createdPanes.put(newState, newPane);
        }
        stage.getScene().setRoot(newPane);
    }

    /**
     * 
     */
    public void update() {
        TournamentPane pane = ((TournamentPane) createdPanes.get(GUIState.TOURNAMENT));
        pane.update();
    }

}
