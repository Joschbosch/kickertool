package zur.koeln.kickertool.ui.tasks;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;
import zur.koeln.kickertool.ui.api.IDialogCallback;
import zur.koeln.kickertool.ui.api.IDialogFXMLController;

public class DialogFinishTask {

	private DialogFinishTask() {
		//
	}
	
	public static ExecutorService startNewTask(IDialogFXMLController fxmlController, IDialogCallback cb) {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new Task() {

			@Override
			protected Object call() throws Exception {
				fxmlController.applyDialogOKClicked();
				return null;
			}
		});
		
		executor.submit(new Task() {

			@Override
			protected Object call() throws Exception {
				cb.doAfterDialogClosed(fxmlController.sendDialogResult());
				return null;
			}
		});
		
		return executor;
	}
	
}
