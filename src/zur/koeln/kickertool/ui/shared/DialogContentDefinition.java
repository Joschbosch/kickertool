package zur.koeln.kickertool.ui.shared;

import lombok.Getter;
import zur.koeln.kickertool.ui.controller.dialogs.PlayerManagementDialogController;
import zur.koeln.kickertool.ui.controller.dialogs.PlayerNameEditDialogController;
import zur.koeln.kickertool.ui.controller.dialogs.TournamentConfigurationDialogController;
import zur.koeln.kickertool.ui.controller.dialogs.TournamentSettingsDialogController;

@Getter
@SuppressWarnings("nls")
public enum DialogContentDefinition {

	PLAYER_MANAGEMENT_DIALOG(PlayerManagementDialogController.class, "PlayerManagementDialogContent.fxml", "Spielerverwaltung"),
	PLAYER_NAME_DIALOG(PlayerNameEditDialogController.class, "PlayerNameDialogContent.fxml", "Spielername", 250.0),
	TOURNAMENT_CONFIGURATION_DIALOG(TournamentConfigurationDialogController.class, "TournamentConfigurationDialogContent.fxml", "Neues Turnier"),
	TOURNAMENT_SETTINGS_DIALOG(TournamentSettingsDialogController.class, "TournamentSettingsDialogContent.fxml", "Einstellungen", 550.0);
	
	private Class fxmlControllerClass;
	private String fxmlFile;
	private String dialogTitle;
	private Double preferredWidth;
	
	private DialogContentDefinition(Class fxmlControllerClass, String fxmlFile, String dialogTitle) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
	}
	
	private DialogContentDefinition(Class fxmlControllerClass, String fxmlFile, String dialogTitle, double prefWidth) {
		this.fxmlControllerClass = fxmlControllerClass;
		this.fxmlFile = fxmlFile;
		this.dialogTitle = dialogTitle;
		this.preferredWidth = Double.valueOf(prefWidth);
	}
	
}
