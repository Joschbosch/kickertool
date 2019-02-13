package zur.koeln.kickertool.ui.api.events;

public interface DialogCloseEvent<Result> {
	
	void doAfterDialogClosed(Result result);
	
}
