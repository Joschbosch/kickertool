package zur.koeln.kickertool.ui.shared;

import lombok.Getter;
import zur.koeln.kickertool.ui.controller.dialogs.FXMLPlayerManagementDialogContentController;
import zur.koeln.kickertool.ui.controller.dialogs.FXMLPlayerNameDialogContentController;
import zur.koeln.kickertool.ui.controller.dialogs.FXMLTournamentConfigurationDialogContent;
import zur.koeln.kickertool.ui.controller.dialogs.FXMLTournamentSettingsDialogContentController;

@Getter
@SuppressWarnings("nls")
public enum DialogContent {

	PLAYER_MANAGEMENT_DIALOG(FXMLPlayerManagementDialogContentController.class, "PlayerManagementDialogContent.fxml", "Spielerverwaltung"),
	PLAYER_NAME_DIALOG(FXMLPlayerNameDialogContentController.class, "PlayerNameDialogContent.fxml", "Spielername", 250.0),
	TOURNAMENT_CONFIGURATION_DIALOG(FXMLTournamentConfigurationDialogContent.class, "TournamentConfigurationDialogContent.fxml", "Neues Turnier"),
	TOURNAMENT_SETTINGS_DIALOG(FXMLTournamentSettingsDialogContentController.class, "TournamentSettingsDialogContent.fxml", "Einstellungen", 550.0);
	
	private Class fxmlControllerClass;
	private String fxmlFile;
	private String dialogTitle;
	private Double preferredWidth;
	
	private DialogContent(Class fxmlControllerClass, String fxmlFile, String dialogTitle) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
	}
	
	private DialogContent(Class fxmlControllerClass, String fxmlFile, String dialogTitle, double prefWidth) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
		this.preferredWidth = Double.valueOf(prefWidth);
	}
	
}
