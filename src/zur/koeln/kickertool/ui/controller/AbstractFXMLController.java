package zur.koeln.kickertool.ui.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.base.IFXLabelFloatControl;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.DialogClosedCallback;
import zur.koeln.kickertool.ui.api.IFXMLController;
import zur.koeln.kickertool.ui.api.IFXMLDialogContent;
import zur.koeln.kickertool.ui.service.DialogContent;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Getter(value=AccessLevel.PRIVATE)
@Setter(value=AccessLevel.PUBLIC)
public class AbstractFXMLController implements IFXMLController{
	
	@FXML 
	StackPane rootStackPane;

	@Override
	public void initialize() {
		IFXMLController.super.initialize();
	}
	
	protected void startBackgroundTask(BackgroundTask backgroundTask) {
		
		JFXDialog loadingDialog = createLoadingDialog();
		loadingDialog.show();
		
		Task<Object> task = new Task<Object>() {
			
			@Override
			protected Object call() throws Exception {
				return backgroundTask.performTask();
			}
		};
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				loadingDialog.close();
				backgroundTask.doOnSuccess(task.getValue());
			}
		});
		
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				loadingDialog.close();
				backgroundTask.doOnFailure(task.getException());
			}
		});
	
		new Thread(task).start();
		
	}
	
	protected void openDialogue(DialogContent dialogContent, DialogClosedCallback dialogClosedCallback) {

		try {
			FXMLLoader fxmlSceneLoader = FXMLGuiService.getInstance().getFXMLDialogLoader(dialogContent);
			Pane pane = fxmlSceneLoader.load();
			AbstractFXMLController dialogContentController = fxmlSceneLoader.getController();
			dialogContentController.setRootStackPane(getRootStackPane());
				
			if (!IFXMLDialogContent.class.isInstance(dialogContentController)) {
				throw new IllegalArgumentException("Der Dialog " + dialogContent + " implementiert nicht das Interface " + IFXMLDialogContent.class.getSimpleName());
			}
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			dialogLayout.setHeading(new Text(dialogContent.getDialogTitle()));
			dialogLayout.setBody(pane);

			JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogLayout, DialogTransition.CENTER, false);

			JFXButton btnOK = new JFXButton("OK");
			btnOK.setPrefWidth(100.0);
			btnOK.setOnAction(event -> {
				
				IFXMLDialogContent iFxmlDialogContent = (IFXMLDialogContent) dialogContentController;
				
				ModelValidationResult validate = iFxmlDialogContent.validate();
				
				if (validate.hasValidationMessages()) {
					showModelValidationError("Unvollständige Eingaben", validate);
				} else {
					dialog.close();
					dialogClosedCallback.doAfterDialogClosed(iFxmlDialogContent.sendResult());
				}
				
			});
			
			JFXButton btnCancel = new JFXButton("Abbrechen");
			btnCancel.setPrefWidth(100.0);
			btnCancel.setOnAction(event -> dialog.close());
			
			dialogLayout.setActions(btnCancel, btnOK);
			
			if (dialogContent.getPreferredWidth() != null) {
				dialogLayout.setMinWidth(dialogContent.getPreferredWidth().doubleValue());
				dialogLayout.setMaxWidth(dialogContent.getPreferredWidth().doubleValue());
				dialogLayout.setPrefWidth(dialogContent.getPreferredWidth().doubleValue());
			}
			
			dialog.show();
			
			FXMLGuiService.getInstance().startAfterInitializationTask(dialogContentController);
			

		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}

	}
	
	private JFXDialog createLoadingDialog() {

		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setBody(new JFXSpinner());

		double size = 150.0;
		
		dialogContent.setMinWidth(size);
		dialogContent.setMaxWidth(size);
		dialogContent.setPrefWidth(size);
		
		return new JFXDialog(getRootStackPane(), dialogContent, DialogTransition.CENTER, false);
	}
	
	protected void showError(Throwable exception) {
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		
		Label headerText = new Label("Ein Fehler ist aufgetreten!");
		headerText.setStyle("-fx-text-fill: red;");
		
		dialogContent.setHeading(headerText);
		dialogContent.setBody(new Text(exception.getMessage()));

		JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogContent, DialogTransition.CENTER, false);
		
		JFXButton btnOK = new JFXButton("OK");
		btnOK.setPrefWidth(100.0);
		btnOK.setOnAction(event -> dialog.close());
		
		dialogContent.setActions(btnOK);
		
		dialog.show();
		
	}

	protected void showModelValidationError(String title, ModelValidationResult validationResult) {
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		
		dialogContent.setHeading(new Text(title));
		dialogContent.setBody(new Text(validationResult.toString()));

		JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogContent, DialogTransition.CENTER, false);
		
		JFXButton btnOK = new JFXButton("OK");
		btnOK.setPrefWidth(100.0);
		btnOK.setOnAction(event -> dialog.close());
		
		dialogContent.setActions(btnOK);
		
		dialog.show();
		
	}
	
}
