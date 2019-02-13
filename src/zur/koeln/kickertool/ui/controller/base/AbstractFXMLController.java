package zur.koeln.kickertool.ui.controller.base;

import java.io.IOException;
import java.util.List;

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
import javafx.scene.layout.VBox;
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
import zur.koeln.kickertool.ui.vm.base.ILabel;
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
	
	protected <Content, Result> void openDialog(DialogContent dialogContent, Content initialContent, DialogClosedCallback<Result> dialogClosedCallback) {

		try {
			FXMLLoader fxmlSceneLoader = FXMLGuiService.getInstance().getFXMLDialogLoader(dialogContent);
			Pane pane = fxmlSceneLoader.load();
			AbstractFXMLController dialogContentController = fxmlSceneLoader.getController();
			dialogContentController.setRootStackPane(getRootStackPane());
				
			if (!IFXMLDialogContent.class.isInstance(dialogContentController)) {
				throw new IllegalArgumentException("Der Dialog " + dialogContent + " implementiert nicht das Interface " + IFXMLDialogContent.class.getSimpleName());
			}
			
			IFXMLDialogContent<Content, Result> fxmlDialogContent = (IFXMLDialogContent) dialogContentController;
			
			if (initialContent != null) {
				fxmlDialogContent.initContent(initialContent);
			}
			
			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			dialogLayout.setHeading(new Text(dialogContent.getDialogTitle()));
			dialogLayout.setBody(pane);

			JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogLayout, DialogTransition.CENTER, false);

			JFXButton btnOK = new JFXButton("OK");
			btnOK.setDefaultButton(true);
			btnOK.setPrefWidth(100.0);
			btnOK.setOnAction(event -> {

				ModelValidationResult validate = fxmlDialogContent.validate();
				
				if (validate.hasValidationMessages()) {
					showModelValidationError("Unvollständige Eingaben", validate);
				} else {
					dialog.close();
					dialogClosedCallback.doAfterDialogClosed(fxmlDialogContent.sendResult());
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
	
	protected <Void, Result> void openDialog(DialogContent dialogContent, DialogClosedCallback<Result> dialogClosedCallback) {

		openDialog(dialogContent, null, dialogClosedCallback);

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
		btnOK.setDefaultButton(true);
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
		btnOK.setDefaultButton(true);
		btnOK.setOnAction(event -> dialog.close());
		
		dialogContent.setActions(btnOK);
		
		dialog.show();
		
	}
	
	public void showConfirmationDialog(String title, String subTitle, List<ILabel> items, DialogClosedCallback<Boolean> closedCallback) {
		
		JFXDialogLayout dialogContent = new JFXDialogLayout();
		
		dialogContent.setHeading(new Text(title));
		
		VBox contentBox = new VBox();
		contentBox.getChildren().add(new Text(subTitle));
		contentBox.setSpacing(5.0);
		
		if (items != null) {
			items.forEach(item -> contentBox.getChildren().add(new Text("- " + item.getLabel())));
		}
		
		dialogContent.setBody(contentBox);
		JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogContent, DialogTransition.CENTER, false);
		
		JFXButton btnOK = new JFXButton("Ja");
		btnOK.setPrefWidth(100.0);
		btnOK.setDefaultButton(true);
		btnOK.setOnAction(event -> {
			dialog.close();
			closedCallback.doAfterDialogClosed(Boolean.TRUE);
		});
		
		JFXButton btnCancel = new JFXButton("Nein");
		btnCancel.setPrefWidth(100.0);
		btnCancel.setOnAction(event -> {
			dialog.close();
			closedCallback.doAfterDialogClosed(Boolean.FALSE);
		});
		
		dialogContent.setActions(btnCancel, btnOK);
		
		dialog.show();	
		
	}
	
}
