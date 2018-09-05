package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.ui.GUIController;
import zur.koeln.kickertool.ui.GUIState;
import zur.koeln.kickertool.ui.MainMenuPane;
import zur.koeln.kickertool.uifxml.FXMLGUIController;

@SpringBootApplication
public class Main extends Application {
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws MatchException {
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
        BackendController controller = ctx.getBean(BackendController.class);
        controller.setGuiController(guiController);
        guiController.init(ctx, new MainMenuPane(controller));
        guiController.setController(controller);
    }

    private void startWithGUIFXML(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("uifxml/MainMenu.fxml"));
        loader.setControllerFactory(ctx::getBean);
        Parent mainMenu = loader.load();
    	FXMLGUIController fxmlGuiController = new FXMLGUIController(primaryStage, GUIState.MAIN_MENU);
        BackendController controller = ctx.getBean(BackendController.class);
        controller.setGuiController(fxmlGuiController);
        fxmlGuiController.init(ctx, mainMenu, 450, 450);
    }
}
