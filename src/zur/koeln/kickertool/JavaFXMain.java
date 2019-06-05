package zur.koeln.kickertool;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.shared.SceneDefinition;

@SpringBootApplication
public class JavaFXMain extends Application {
	
	private static ConfigurableApplicationContext ctx;
	
    public static void main(String[] args) {
    	
    	ctx = SpringApplication.run(SpringWebMain.class);
        launch(args);
        
    }

	@Override
	public void start(Stage primaryStage) throws IOException {
		
		FXMLGuiService.getInstance().initialize(ctx, primaryStage);
		FXMLGuiService.getInstance().switchScene(SceneDefinition.MAIN_MENU);
        
	}
	
}
