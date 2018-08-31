package zur.koeln.kickertool;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.ui.GUIController;
import zur.koeln.kickertool.ui.GUIState;

public class Main extends Application {

    public static void main(String[] args) throws MatchException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
    	//startWithGUIFXML(primaryStage);
    	startWithGUICode(primaryStage);
    }
   
    
    private void startWithGUICode(Stage primaryStage) {
        GUIController guiController = new GUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentController controller = new TournamentController(guiController);
        guiController.init(controller);
    }

    private void startWithGUIFXML(Stage primaryStage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("uifxml/MainMenu.fxml"));
    	
    	Scene mainMenuScene = new Scene(root, 450, 400);
    	
    	primaryStage.setTitle("parcIT Kickertool");
    	primaryStage.setScene(mainMenuScene);
    	primaryStage.show();
    }
}
