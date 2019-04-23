package zur.koeln.kickertool.ui.service;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.Controller;
import zur.koeln.kickertool.ui.controller.base.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.base.DialogConfirmationResponse;
import zur.koeln.kickertool.ui.controller.base.impl.IDialogConfirmationCloseEvent;
import zur.koeln.kickertool.ui.shared.DialogContentDefinition;
import zur.koeln.kickertool.ui.shared.ListContentDefinition;
import zur.koeln.kickertool.ui.shared.SceneDefinition;

@SuppressWarnings("nls")
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

	private static final String APP_TITLE = "Kickertool"; //$NON-NLS-1$

	public void initialize(ConfigurableApplicationContext ctx, Stage primaryStage) {
		setCtx(ctx);
		setPrimaryStage(primaryStage);
		registerCloseEvent(primaryStage);
	}

	private void registerCloseEvent(Stage stage) {
		stage.setOnCloseRequest(event -> {

			event.consume();
			AbstractController abstrFxmlController = (AbstractController) stage.getScene().getUserData();

			abstrFxmlController.showConfirmationDialog("Beenden", "Wollen Sie wirklich beenden?", null, new IDialogConfirmationCloseEvent() {
				
				@Override
				public void doAfterDialogClosed(DialogConfirmationResponse response) {
					if (response.isAccepted()) {
						Platform.exit();
					}
				}
			});

		});
	}
	
	public void switchScene(SceneDefinition newScene) {

		switchScene(newScene, null);

	}

	public void switchScene(SceneDefinition newScene, Object payload) {

		try {
			closeCurrentController();
			
			FXMLLoader fxmlLoader = getFXMLSceneLoader(newScene);
			Pane pane = fxmlLoader.load();
			Scene scene = new Scene(pane);
			scene.setUserData(fxmlLoader.getController());
			prepareStage(getPrimaryStage(), scene, fxmlLoader.getController(), payload);
		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}
	}

	private void closeCurrentController() {
		Scene currentScene = getPrimaryStage().getScene();
	
		if (currentScene != null) {
			Controller currentController = (Controller) currentScene.getUserData();
			currentController.close();
		}
	}

	private void prepareStage(Stage stage, Scene newScene, Controller fxmlController, Object payload) {
		stage.setScene(newScene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
		stage.setTitle(APP_TITLE);

		startAfterInitializationTask(fxmlController, payload);
	}

	public void startAfterInitializationTask(Controller fxmlController, Object payload) {

		// We can't use Platform.runLater() here, because this call is still too early for e.g. focus a control.
		// Thus we use a normal Task instead.
		
		Task<Void> initTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// We let the thread sleep so that the GUI can be rendered meanwhile
				Thread.sleep(250);
				return null;
			}
		};

		initTask.setOnSucceeded(event -> fxmlController.doAfterInitializationCompleted(payload));

		new Thread(initTask).start();

	}
	
	public FXMLLoader getLoadedFXMLLoader(ListContentDefinition listContent, Object payLoad) {

		FXMLLoader loader = new FXMLLoader(listContent.getFxmlControllerClass().getResource(listContent.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		
		try {
			loader.load();
			Controller controller = loader.getController();
			
			startAfterInitializationTask(controller, payLoad);
			
		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}
		
		return loader;

	}


	public FXMLLoader getFXMLSceneLoader(SceneDefinition scene) {

		FXMLLoader loader = new FXMLLoader(scene.getFxmlControllerClass().getResource(scene.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;

	}

	public FXMLLoader getFXMLDialogLoader(DialogContentDefinition dialogContent) {

		FXMLLoader loader = new FXMLLoader(dialogContent.getFxmlControllerClass().getResource(dialogContent.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;

	}

}
