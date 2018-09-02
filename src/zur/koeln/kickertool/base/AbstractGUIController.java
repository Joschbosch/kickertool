package zur.koeln.kickertool.base;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.ui.GUIState;

@RequiredArgsConstructor
@Getter(value=AccessLevel.PROTECTED)	
public abstract class AbstractGUIController {

	@NonNull
    private Stage stage;

    @NonNull
    private GUIState state;

    private TournamentController controller;
    private Map<GUIState, Pane> createdPanes = new HashMap<>();
	
    public void init(TournamentController controller, Parent rootPane) {
        init(controller, rootPane, 1200, 800);
    }
    
    public void init(TournamentController controller, Parent rootPane, double width, double height) {
        this.controller = controller;
        stage.setTitle("Kicker APP");
        Scene mainScene = new Scene(rootPane, width, height);
        stage.setScene(mainScene);
        stage.show();
    }
    
    public abstract void switchStateTo(GUIState newState);
    
    public abstract void update();

}
