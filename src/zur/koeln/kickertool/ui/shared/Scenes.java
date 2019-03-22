package zur.koeln.kickertool.ui.shared;

import lombok.Getter;
import zur.koeln.kickertool.ui.controller.FXMLMainMenuViewController;
import zur.koeln.kickertool.ui.controller.FXMLTournamentMainViewController;

@Getter
@SuppressWarnings("nls")
public enum Scenes {
	
	MAIN_MENU(FXMLMainMenuViewController.class, "MainMenuView.fxml", "Kickertool"),
	TOURNAMENT_CONTROLLING(FXMLTournamentMainViewController.class, "TournamentMainView.fxml", "Kickertool");
	
	private Class fxmlControllerClass;
	private String fxmlFile;
	private String title;
	
	private Scenes(Class fxmlControllerClass, String fxmlFile, String title) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
		this.title = title;
	}

}
