package zur.koeln.kickertool.ui.api;

import javafx.fxml.FXML;

public interface IFXMLController {
	
	@FXML
	default void initialize() {
		registerListener();
	}
	
	@FXML
	default void registerListener() {
		// default
	}
}
