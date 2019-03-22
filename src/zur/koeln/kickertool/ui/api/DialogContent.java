package zur.koeln.kickertool.ui.api;

import zur.koeln.kickertool.ui.controller.vms.base.ModelValidationResult;

/**
 * Interface for dialogs. Every dialog <b>has</b> to implement this.
 * When opening a dialog you can specify an initial content which will be provided by you 
 * and a result which you will get back, when leaving the dialog with ok.
 * 
 * @author Daniel Cleemann
 */
public interface DialogContent<InitialContent, Result> {
	
	/**
	 * Specifies the result, the user gets back, when closing the dialog with ok. <br/>
	 * Before sending the result, you might want to validate ({@link #validate()}) the result content.
	 */
	default Result sendResult() {
		return null;
	}
	
	/**
	 * The validation takes place, before the user closes the dialog with ok. If the validation fails,
	 * the dialog remains open and the user can correct his entries.
	 */
	default ModelValidationResult validate() {
		return ModelValidationResult.empty();
	}
	
	/**
	 * In the dialog you can set initial values to controls, which you can receive from the initial content.
	 */
	default void initializeDialogWithContent(InitialContent initialContent) {
		//
	}
}
