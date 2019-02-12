package zur.koeln.kickertool.ui.api;

public interface DialogClosedCallback<T> {
	
	void doAfterDialogClosed(T result);
	
}
