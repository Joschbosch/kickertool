package zur.koeln.kickertool.ui.api;

public interface IDialogCallback<T> {
	
	void doAfterDialogClosed(T resultObject);
	
}
