package zur.koeln.kickertool.ui.api;

import javafx.fxml.FXML;

public interface IFXMLController {
	
	@FXML
	default void initialize() {
		setupControls();
		setupBindings();
	}
	
	default void setupControls() {
		// default
	}
	
	default void setupBindings() {
		// default
	}
	
	default void doAfterInitializationCompleted() {
		// default
	}
}
