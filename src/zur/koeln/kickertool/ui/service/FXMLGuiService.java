package zur.koeln.kickertool.ui.service;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.api.IFXMLController;

public class FXMLGuiService {

	private static FXMLGuiService instance;

	public static FXMLGuiService getInstance() {

		if (instance == null) {
			instance = new FXMLGuiService();
		}

		return instance;
	}

	@Getter(value = AccessLevel.PRIVATE)
	@Setter(value = AccessLevel.PRIVATE)
	private ConfigurableApplicationContext ctx;

	@Getter(value = AccessLevel.PRIVATE)
	@Setter(value = AccessLevel.PRIVATE)
	private Stage primaryStage;

	private static final String PREFIX = "../"; //$NON-NLS-1$

	public void initialize(ConfigurableApplicationContext ctx, Stage primaryStage) {
		setCtx(ctx);
		setPrimaryStage(primaryStage);
	}

	public void switchScene(Scenes newScene) {

		try {
			FXMLLoader fxmlLoader = getFXMLSceneLoader(newScene);
			Pane pane = fxmlLoader.load();
			prepareStage(getPrimaryStage(), pane, fxmlLoader.getController());
		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}
	}

	private void prepareStage(Stage stage, Pane pane, IFXMLController fxmlController) {
		Scene newScene = new Scene(pane);
		stage.setScene(newScene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
		
		startAfterInitializationTask(fxmlController);
	}
	
	public void startAfterInitializationTask(IFXMLController fxmlController) {
		
		Task<Void> initTask = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				// We let the thread sleep so that the GUI can be rendered meanwhile
				Thread.sleep(250);
				return null;
			}
		};
		
		initTask.setOnSucceeded(event -> fxmlController.doAfterInitializationCompleted());
		
		new Thread(initTask).start();
	}

	public FXMLLoader getFXMLSceneLoader(Scenes scene) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + scene.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;

	}
	
	public FXMLLoader getFXMLDialogLoader(DialogContent dialogContent) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + dialogContent.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;

	}

}
