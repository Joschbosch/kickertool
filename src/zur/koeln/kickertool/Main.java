package zur.koeln.kickertool;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.ui.GUIController;
import zur.koeln.kickertool.ui.GUIState;
import zur.koeln.kickertool.ui.MainMenuPane;
import zur.koeln.kickertool.uifxml.FXMLGUIController;

public class Main extends Application {

    public static void main(String[] args) throws MatchException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        startWithGUIFXML(primaryStage);
        //startWithGUICode(primaryStage);
    }
   
    
    private void startWithGUICode(Stage primaryStage) {
        GUIController guiController = new GUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentController controller = new TournamentController(guiController);
        guiController.init(controller, new MainMenuPane(controller));
    }

    private void startWithGUIFXML(Stage primaryStage) throws IOException {
    	
    	Parent mainMenu = FXMLLoader.load(getClass().getResource("uifxml/MainMenu.fxml"));
    	
    	FXMLGUIController fxmlGuiController = new FXMLGUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentController controller = new TournamentController(fxmlGuiController);
        fxmlGuiController.init(controller, mainMenu, 450, 450);
    }
}
