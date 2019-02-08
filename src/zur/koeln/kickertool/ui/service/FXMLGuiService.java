package zur.koeln.kickertool.ui.service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ConfigurableApplicationContext;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.api.IDialogCallback;
import zur.koeln.kickertool.ui.api.IDialogFXMLController;
import zur.koeln.kickertool.ui.tasks.DialogFinishTask;

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
	private static final String FXMLPARTS_PREFIX = "../fxmlparts/"; //$NON-NLS-1$

	public void initialize(ConfigurableApplicationContext ctx, Stage primaryStage) {
		setCtx(ctx);
		setPrimaryStage(primaryStage);
	}

	public void switchScene(Scenes newScene) {

		try {
			FXMLLoader fxmlLoader = getFXMLLoader(newScene);
			Pane pane = fxmlLoader.load();
			prepareStage(getPrimaryStage(), pane);
		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}
	}

	public void openDialogue(StackPane stackPane, Scenes contentScene, IDialogCallback dialogCallback) {

		Pane pane;
		try {
			FXMLLoader fxmlSceneLoader = getFXMLLoader(contentScene);
			pane = fxmlSceneLoader.load();
			IDialogFXMLController dialogFXMLController = fxmlSceneLoader.getController();

			JFXDialogLayout dialogContent = new JFXDialogLayout();
			dialogContent.setHeading(new Text(contentScene.getTitle()));
			dialogContent.setBody(pane);

			JFXDialog dialog = new JFXDialog(stackPane, dialogContent, DialogTransition.CENTER, false);

			JFXButton btnOK = new JFXButton("OK");
			btnOK.setPrefWidth(100.0);
			btnOK.setOnAction(event -> {
				JFXDialog loadingDialog = getLoadingDialog(stackPane);
				loadingDialog.show();
				Task<Void> runner = new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						ExecutorService taskService = DialogFinishTask.startNewTask(dialogFXMLController, dialogCallback);
						taskService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
						loadingDialog.close();
						dialog.close();
						return null;
					}
					
				};
				
			});

			JFXButton btnCancel = new JFXButton("Abbrechen");
			btnCancel.setOnAction(event -> dialog.close());
			btnCancel.setPrefWidth(100.0);
			dialogContent.setActions(btnCancel, btnOK);

			dialog.show();

		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}

	}

	private JFXDialog getLoadingDialog(StackPane stackPane) {

		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setBody(new JFXSpinner());

		JFXDialog dialog = new JFXDialog(stackPane, dialogContent, DialogTransition.CENTER, false);
		dialog.setPrefWidth(300);
		dialog.setPrefHeight(300);
		return dialog;
	}

	private void prepareStage(Stage stage, Pane pane) {
		Scene newScene = new Scene(pane);
		stage.setScene(newScene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
	}

	private FXMLLoader getFXMLLoader(Scenes scene) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + scene.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;

	}

}
