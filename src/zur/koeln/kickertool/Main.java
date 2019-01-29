package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.uifxml.FXMLGUIController;

@SpringBootApplication
public class Main extends Application {
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(Main.class);

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION, "Wirklich verlassen?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() != ButtonType.YES) {
                    event.consume();
                }
            }

        });
        startWithGUIFXML(primaryStage);
    }
    
    private void startWithGUIFXML(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("uifxml/MainMenu.fxml"));
        loader.setControllerFactory(ctx::getBean);
        Parent mainMenu = loader.load();
    	FXMLGUIController fxmlGuiController = new FXMLGUIController(primaryStage, ToolState.MAIN_MENU);
        BasicBackendController controller = ctx.getBean(BasicBackendController.class);
        controller.setGuiController(fxmlGuiController);
        fxmlGuiController.init(ctx, mainMenu, 450, 450);
    }
}
