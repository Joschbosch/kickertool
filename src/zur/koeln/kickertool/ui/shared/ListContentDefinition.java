package zur.koeln.kickertool.ui.shared;

import lombok.Getter;
import zur.koeln.kickertool.ui.controller.lists.MatchController;

@Getter
@SuppressWarnings("nls")
public enum ListContentDefinition {

	MATCH(MatchController.class, "Match.fxml");
	
	private Class fxmlControllerClass;
	private String fxmlFile;
	
	private ListContentDefinition(Class fxmlControllerClass, String fxmlFile) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
	}
	
}
