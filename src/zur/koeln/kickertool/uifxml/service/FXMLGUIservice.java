package zur.koeln.kickertool.uifxml.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@Service
public class FXMLGUIservice {
	
	private Stage primaryStage;
	private ConfigurableApplicationContext ctx;
	private static final String APP_TITLE = "parcIT Kickerturnier Helferlein"; //$NON-NLS-1$
	private static final Image ICON = new Image(FXMLGUIservice.class.getResource("/images/icon.png").toString()); //$NON-NLS-1$
	
	private List<Stage> otherStages = new ArrayList<>();
	
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

		prepareScene(getPrimaryStage(), fxmlLoader.getRoot());
		
	}
	
	private void prepareScene(Stage stage, Pane rootPane) {
		Scene newScene = new Scene(rootPane);
		stage.setScene(newScene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
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
	
	public void openAnotherStage(FXMLGUI fxmlGUI) {
		Stage otherStage = new Stage();
		otherStage.setTitle(APP_TITLE);
		otherStage.getIcons().add(ICON);
		
		getOtherStages().add(otherStage);
		
		FXMLLoader loader = getFXMLLoader(fxmlGUI);
		
		prepareScene(otherStage, loader.getRoot());
	}

	private Stage getPrimaryStage() {
		return primaryStage;
	}

	private ConfigurableApplicationContext getCtx() {
		return ctx;
	}

	private List<Stage> getOtherStages() {
		return otherStages;
	}
	
	
}
