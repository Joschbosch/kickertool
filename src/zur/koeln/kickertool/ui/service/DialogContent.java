package zur.koeln.kickertool.ui.service;

import lombok.Getter;

@Getter
@SuppressWarnings("nls")
public enum DialogContent {

	PLAYER_MANAGEMENT_DIALOGUE("PlayerManagementDialogContent.fxml", "Spielerverwaltung"),
	PLAYER_NAME_DIALOGUE("PlayerNameDialogContent.fxml", "Spielername", 250.0);
	
	private String fxmlFile;
	private String dialogTitle;
	private Double preferredWidth;
	
	private DialogContent(String fxmlFile, String dialogTitle) {
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
	}
	
	private DialogContent(String fxmlFile, String dialogTitle, double prefWidth) {
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
		this.preferredWidth = Double.valueOf(prefWidth);
	}
	
}
