package zur.koeln.kickertool.ui.service;

import lombok.Getter;

@Getter
public enum DialogContent {

	PLAYER_MANAGEMENT_DIALOGUE("PlayerManagementDialogContent.fxml", "Spielerverwaltung");
	
	private String fxmlFile;
	private String dialogTitle;
	private DialogContent(String fxmlFile, String dialogTitle) {
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
	}
	
	
}
