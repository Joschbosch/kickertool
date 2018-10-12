package zur.koeln.kickertool.uifxml.service;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Service
public class FXMLGUIservice {
	
	private Stage primaryStage;
	private ConfigurableApplicationContext ctx;
	private final static String APP_TITLE = "parcIT Kickerturnier Helferlein"; //$NON-NLS-1$
	private final static Image ICON = new Image(FXMLGUIservice.class.getResource("/images/icon.png").toString()); //$NON-NLS-1$
	
	public void init(Stage newPrimaryStage, ConfigurableApplicationContext newCtx) {
		primaryStage = newPrimaryStage;
		ctx = newCtx;
		
        getPrimaryStage().setTitle(APP_TITLE);
        getPrimaryStage().getIcons().add(ICON);
	}
	
	public void showMainMenu() {
		switchToScene(FXMLGUI.MAIN_MENU);
	}
	
	public void switchToScene(FXMLGUI fxmlGui) {
		
		try {
			FXMLLoader fxmlLoader = getFXMLLoader(fxmlGui);
			Pane rootPane = (Pane) fxmlLoader.load();
			
			Scene newScene = new Scene(rootPane);
			getPrimaryStage().setScene(newScene);
			getPrimaryStage().sizeToScene();
	        getPrimaryStage().centerOnScreen();
	        getPrimaryStage().show();
		} catch (IOException e) {
			// Should never get thrown
			e.printStackTrace();
		}
		
	}
	
	public FXMLLoader getFXMLLoader(FXMLGUI fxmlGui) {
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlGui.getFxmlControllerClass().getResource(fxmlGui.getFxmlFile()));
		fxmlLoader.setControllerFactory(getCtx()::getBean);
		return fxmlLoader;
	}

	private Stage getPrimaryStage() {
		return primaryStage;
	}

	private ConfigurableApplicationContext getCtx() {
		return ctx;
	}
	
	
}
