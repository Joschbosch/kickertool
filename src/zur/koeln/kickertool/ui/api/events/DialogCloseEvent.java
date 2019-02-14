package zur.koeln.kickertool.ui.api.events;

/**
 * Whenever a dialog is closed by clicking on ok, this interface is used.
 * 
 * @author Daniel Cleemann
 *
 */
public interface DialogCloseEvent<Result> {
	
	/**
	 * Gets called after the dialog is closed by clicking on ok.
	 */
	void doAfterDialogClosed(Result result);
	
}
