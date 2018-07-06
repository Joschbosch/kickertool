package zur.koeln.kickertool;

import javafx.application.Application;
import javafx.stage.Stage;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.ui.GUIController;
import zur.koeln.kickertool.ui.GUIState;

public class Main extends Application {

    public static void main(String[] args) throws MatchException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GUIController guiController = new GUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentController controller = new TournamentController(guiController);
        guiController.init(controller);
    }

}
