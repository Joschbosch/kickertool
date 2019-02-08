package zur.koeln.kickertool.ui.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.IFXMLController;
import zur.koeln.kickertool.ui.service.DialogContent;
import zur.koeln.kickertool.ui.service.FXMLGuiService;

@Getter(value=AccessLevel.PRIVATE)
public class AbstractFXMLController implements IFXMLController{
	
	@FXML 
	StackPane rootStackPane;

	protected void startBackgroundTask(BackgroundTask backgroundTask) {
		
		JFXDialog loadingDialog = getLoadingDialog();
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
				backgroundTask.doOnSucceed(task.getValue());
			}
		});
		
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				loadingDialog.close();
				backgroundTask.doOnFailure();
			}
		});
	
		new Thread(task).start();
		
	}
	
	private void startBackgroundTaskAndCloseDialogOnSucceed(BackgroundTask backgroundTask, JFXDialog dialog) {
		
		JFXDialog loadingDialog = getLoadingDialog();
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
				backgroundTask.doOnSucceed(task.getValue());
				dialog.close();
			}
		});
		
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				loadingDialog.close();
				backgroundTask.doOnFailure();
			}
		});
	
		new Thread(task).start();
		
	}
	
	protected void openDialogue(DialogContent dialogContent, BackgroundTask taskAfterOkClicked) {

		Pane pane;
		try {
			FXMLLoader fxmlSceneLoader = FXMLGuiService.getInstance().getFXMLLoader(dialogContent);
			pane = fxmlSceneLoader.load();

			JFXDialogLayout dialogLayout = new JFXDialogLayout();
			dialogLayout.setHeading(new Text(dialogContent.getDialogTitle()));
			dialogLayout.setBody(pane);

			JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogLayout, DialogTransition.CENTER, false);

			JFXButton btnOK = new JFXButton("OK");
			btnOK.setPrefWidth(100.0);
			btnOK.setOnAction(event -> startBackgroundTaskAndCloseDialogOnSucceed(taskAfterOkClicked, dialog));

			JFXButton btnCancel = new JFXButton("Abbrechen");
			btnCancel.setPrefWidth(100.0);
			btnCancel.setOnAction(event -> dialog.close());
			
			dialogLayout.setActions(btnCancel, btnOK);
			dialog.show();

		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}

	}
	
	private JFXDialog getLoadingDialog() {

		JFXDialogLayout dialogContent = new JFXDialogLayout();
		dialogContent.setBody(new JFXSpinner());

		JFXDialog dialog = new JFXDialog(getRootStackPane(), dialogContent, DialogTransition.CENTER, false);
		dialog.setPrefWidth(300);
		dialog.setPrefHeight(300);
		return dialog;
	}

	
}
