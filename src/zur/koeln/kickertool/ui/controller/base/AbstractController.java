package zur.koeln.kickertool.ui.controller.base;

import java.io.IOException;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;

import javafx.concurrent.Task;
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
import zur.koeln.kickertool.application.api.dtos.base.DTO;
import zur.koeln.kickertool.ui.controller.base.vm.ILabel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.shared.DialogContentDefinition;
import zur.koeln.kickertool.ui.tools.DTOVerifcationUtils;

@Getter(value=AccessLevel.PRIVATE)
@Setter(value=AccessLevel.PUBLIC)
@SuppressWarnings("nls")
public class AbstractController<PAYLOAD> implements Controller<PAYLOAD> {
	
	@FXML 
	StackPane rootStackPane;
	
	@Getter(value=AccessLevel.PROTECTED)
	private PAYLOAD payload;

	@FXML
	@Override
	public void initialize() {
		Controller.super.initialize();
	}
	
	/**
	 * Starts a new background task. You can be sure, that the GUI remains responsive, while performing the task.
	 */
	protected void startBackgroundTask(BackgroundTask backgroundTask) {
		
		JFXDialog loadingDialog = createLoadingDialog();
		loadingDialog.show();
		
		Task<Object> task = new Task<Object>() {
			
			@Override
			protected Object call() throws Exception {
				return backgroundTask.performTask();
			}
		};
		
		task.setOnSucceeded(event -> {
			loadingDialog.close();
			backgroundTask.doOnSuccess(task.getValue());
		});
		
		task.setOnFailed(event -> {
			loadingDialog.close();
			backgroundTask.doOnFailure(task.getException());
		});
	
		new Thread(task).start();
		
	}
	
	/**
	 * Opens a dialog with an initial content. If you don't want to provide any initial content, use {@link #openDialog(DialogContentDefinition, DialogCloseEvent)} instead.
	 */
	protected <Content, Result> void openDialog(DialogContentDefinition dialogContent, Content initialContent, DialogCloseEvent<Result> dialogClosedCallback) {

		try {
			FXMLLoader fxmlSceneLoader = FXMLGuiService.getInstance().getFXMLDialogLoader(dialogContent);
			Pane pane = fxmlSceneLoader.load();
			AbstractController dialogContentController = fxmlSceneLoader.getController();
			dialogContentController.setRootStackPane(getRootStackPane());
				
			if (!DialogContent.class.isInstance(dialogContentController)) {
				throw new IllegalArgumentException("Der Dialog " + dialogContent + " implementiert nicht das Interface " + DialogContent.class.getSimpleName());
			}
			
			DialogContent<Content, Result> fxmlDialogContent = (DialogContent) dialogContentController;
			
			if (initialContent != null) {
				fxmlDialogContent.initializeDialogWithContent(initialContent);
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
	
	/**
	 * Opens a dialog without any initial content.
	 */
	protected <Void, Result> void openDialog(DialogContentDefinition dialogContent, DialogCloseEvent<Result> dialogClosedCallback) {

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
	
	public void showConfirmationDialog(String title, String subTitle, List<ILabel> items, DialogCloseEvent<Boolean> closedCallback) {
		
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
	
	protected void checkResponse(DTO baseDto) throws BackgroundTaskException {
		
		DTOVerifcationUtils.checkResponse(baseDto);
		
	}


	@Override
	public void setPayload(PAYLOAD payload) {
		this.payload = payload;
	}}
