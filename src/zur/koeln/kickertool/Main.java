package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import zur.koeln.kickertool.base.TournamentControllerService;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.ui.GUIController;
import zur.koeln.kickertool.ui.GUIState;
import zur.koeln.kickertool.ui.MainMenuPane;
import zur.koeln.kickertool.uifxml.FXMLGUIController;

@SpringBootApplication
public class Main extends Application {
    private static ApplicationContext ctx;

    public static void main(String[] args) throws MatchException {
        ctx = new AnnotationConfigApplicationContext(KickerToolConfiguration.class);
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //        startWithGUIFXML(primaryStage);
        startWithGUICode(primaryStage);
    }
    
    private void startWithGUICode(Stage primaryStage) {
        GUIController guiController = new GUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentControllerService controller = ctx.getBean(TournamentControllerService.class);
        controller.setGuiController(guiController);
        guiController.init(controller, new MainMenuPane(controller));
    }

    private void startWithGUIFXML(Stage primaryStage) throws IOException {
    	
    	Parent mainMenu = FXMLLoader.load(getClass().getResource("uifxml/MainMenu.fxml"));
    	
    	FXMLGUIController fxmlGuiController = new FXMLGUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentControllerService controller = ctx.getBean(TournamentControllerService.class);
        controller.setGuiController(fxmlGuiController);
        fxmlGuiController.init(controller, mainMenu, 450, 450);
    }
}
