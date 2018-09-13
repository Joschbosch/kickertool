package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.base.ToolState;
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
    }
    
    private void startWithGUIFXML(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("uifxml/MainMenu.fxml"));
        loader.setControllerFactory(ctx::getBean);
        Parent mainMenu = loader.load();
    	FXMLGUIController fxmlGuiController = new FXMLGUIController(primaryStage, ToolState.MAIN_MENU);
        BackendController controller = ctx.getBean(BackendController.class);
        controller.setGuiController(fxmlGuiController);
        fxmlGuiController.init(ctx, mainMenu, 450, 450);
    }
}
