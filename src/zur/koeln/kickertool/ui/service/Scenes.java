package zur.koeln.kickertool.ui.service;

import lombok.Getter;

@Getter
@SuppressWarnings("nls")
public enum Scenes {
	
	MAIN_MENU("MainMenuView.fxml", "Kickertool"),
	TOURNAMENT_CONTROLLING("TournamentMainView.fxml", "Kickertool");
	
	private String fxmlFile;
	private String title;
	
	private Scenes(String fxmlFile, String title) {
		this.fxmlFile = fxmlFile;
		this.title = title;
	}

}
