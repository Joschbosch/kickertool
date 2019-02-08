package zur.koeln.kickertool.ui.service;

import lombok.Getter;

@Getter
@SuppressWarnings("nls")
public enum Scenes {
	
	MAIN_MENU("MainMenu.fxml");
	
	private String fxmlFile;

	private Scenes(String fxmlFile) {
		this.fxmlFile = fxmlFile;
	}
	
	
	
}
