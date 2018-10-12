package zur.koeln.kickertool.uifxml.service;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@Service
public class FXMLGUIservice {
	
	private Stage primaryStage;
	private ConfigurableApplicationContext ctx;
	private static final String APP_TITLE = "parcIT Kickerturnier Helferlein"; //$NON-NLS-1$
	private static final Image ICON = new Image(FXMLGUIservice.class.getResource("/images/icon.png").toString()); //$NON-NLS-1$
	
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
		
		FXMLLoader fxmlLoader = getFXMLLoader(fxmlGui);

		Scene newScene = new Scene(fxmlLoader.getRoot());
		getPrimaryStage().setScene(newScene);
		getPrimaryStage().sizeToScene();
        getPrimaryStage().centerOnScreen();
        getPrimaryStage().show();
		
	}
	
	public FXMLLoader getFXMLLoader(FXMLGUI fxmlGui) {
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlGui.getFxmlControllerClass().getResource(fxmlGui.getFxmlFile()));
		fxmlLoader.setControllerFactory(getCtx()::getBean);
		
		try {
			fxmlLoader.load();
			Parent pane = fxmlLoader.getRoot();
			pane.getStylesheets().clear();
			//pane.getStylesheets().add(fxmlGui.getFxmlControllerClass().getResource("/css/kickertool.css").toExternalForm()); //$NON-NLS-1$
		} catch (IOException e) {
			// Should never be thrown
			throw new RuntimeException(e);
		}
		
		return fxmlLoader;
	}

	private Stage getPrimaryStage() {
		return primaryStage;
	}

	private ConfigurableApplicationContext getCtx() {
		return ctx;
	}
	
	
}
