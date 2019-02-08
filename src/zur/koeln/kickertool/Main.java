package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.service.Scenes;

@SpringBootApplication
public class Main extends Application {
	
	private static ConfigurableApplicationContext ctx;
	
    public static void main(String[] args) {
    	
    	ctx = SpringApplication.run(Main.class);
        launch(args);
        
    }

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		primaryStage.setOnCloseRequest(event -> {
		    Alert alert = new Alert(AlertType.CONFIRMATION, "Wirklich verlassen?", ButtonType.YES, ButtonType.NO);
		    alert.showAndWait();
		    if (alert.getResult() != ButtonType.YES) {
		        event.consume();
		    }
		});
		
        startWithGUIFXML(primaryStage);
        
	}
	
	private void startWithGUIFXML(Stage primaryStage) {
		
		FXMLGuiService.getInstance().initialize(ctx, primaryStage);
		FXMLGuiService.getInstance().switchScene(Scenes.MAIN_MENU);
		
    }

}
