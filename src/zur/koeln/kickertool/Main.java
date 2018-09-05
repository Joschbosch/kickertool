package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws MatchException {
        //        ctx = new AnnotationConfigApplicationContext(KickerToolConfiguration.class);
        ctx = SpringApplication.run(Main.class);

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        startWithGUIFXML(primaryStage);
        //        startWithGUICode(primaryStage);
    }
    
    private void startWithGUICode(Stage primaryStage) {
        GUIController guiController = new GUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentControllerService controller = ctx.getBean(TournamentControllerService.class);
        controller.setGuiController(guiController);
        guiController.init(controller, ctx, new MainMenuPane(controller));
    }

    private void startWithGUIFXML(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("uifxml/MainMenu.fxml"));
        loader.setControllerFactory(ctx::getBean);
        Parent mainMenu = loader.load();
    	FXMLGUIController fxmlGuiController = new FXMLGUIController(primaryStage, GUIState.MAIN_MENU);
        TournamentControllerService controller = ctx.getBean(TournamentControllerService.class);
        controller.setGuiController(fxmlGuiController);
        fxmlGuiController.init(controller, ctx, mainMenu, 450, 450);
    }
}
