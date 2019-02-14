package zur.koeln.kickertool.ui.api;

import javafx.fxml.FXML;

/**
 * Basic interface for FXMLControllers. Defines an optional workflow for setting up the FXMLController.
 * 
 * @author Daniel Cleemann
 */
public interface FXMLController {
	
	/**
	 * Initialize method for FXML. Gets always called automatically and should not be overridden. <br>
	 * If you want to override it, be sure to follow some points:<br>
	 * 1) Be sure to always call super <br>
	 * 2) Do not execute any backgroundtasks, because controls might not have been fully initialized (use {@link #doAfterInitializationCompleted()} instead. <br>
	 * 3) Do not focus any controls (use {@link #doAfterInitializationCompleted()} instead. <br>
	 */
	@FXML
	default void initialize() {
		setupControls();
		setupBindings();
		setupListener();
	}
	
	/**
	 * Gets called during {@link #initialize()}.
	 */
	default void setupControls() {
		// default
	}
	
	/**
	 * Gets called during {@link #initialize()}
	 */
	default void setupBindings() {
		// default
	}
	
	/**
	 * Gets called during {@link #initialize()}
	 */
	default void setupListener() {
		// default
	}
	
	/**
	 * Gets called <b>after</b> {@link #initialize()} is done and the FXML document and the controller is fully
	 * loaded/initialized. This method is great, if you want for example request a focus to a control or load data from the backend.
	 */
	default void doAfterInitializationCompleted() {
		// default
	}
}
