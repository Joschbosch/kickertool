package zur.koeln.kickertool.ui.shared;

import lombok.Getter;
import zur.koeln.kickertool.ui.controller.MainMenuController;
import zur.koeln.kickertool.ui.controller.TournamentMainController;

@Getter
@SuppressWarnings("nls")
public enum Scenes {
	
	MAIN_MENU(MainMenuController.class, "MainMenu.fxml", "Kickertool"),
	TOURNAMENT_CONTROLLING(TournamentMainController.class, "TournamentMain.fxml", "Kickertool");
	
	private Class fxmlControllerClass;
	private String fxmlFile;
	private String title;
	
	private Scenes(Class fxmlControllerClass, String fxmlFile, String title) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
		this.title = title;
	}

}
