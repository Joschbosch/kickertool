package zur.koeln.kickertool.uifxml.service;

import zur.koeln.kickertool.uifxml.*;

@SuppressWarnings("nls")
public enum FXMLGUI {
	
	MAIN_MENU(FXMLMainMenuController.class, "MainMenu.fxml"),
	MATCH_ENTRY(FXMLMatchEntryController.class, "MatchEntry.fxml"),
	MATCH_RESULT_DIALOG(FXMLMatchResultDialogController.class, "MatchResultDialog.fxml"),
	PLAYER_POOL_MANAGEMENT(FXMLPlayerPoolManagementController.class, "PlayerPoolManagement.fxml"),
	PLAYER_SELECTION(FXMLPlayerSelectionController.class, "PlayerSelection.fxml"),
	TOURMANENT(FXMLTournamentController.class, "Tournament.fxml"),
	TOURMANENT_INFO(FXMLTournamentInfoController.class, "TournamentInfo.fxml"),
	TOURNAMENT_CONFIGURATION(FXMLTournamentConfigurationController.class, "TournamentConfiguration.fxml"),
	ADD_PLAYER_DIALOG(FXMLAddPlayerDialogController.class, "AddPlayerDialog.fxml");
	
	private Class fxmlControllerClass;
	private String fxmlFile;

	private FXMLGUI(Class fxmlControllerClass, String fxmlFile) {
		this.fxmlFile = fxmlFile;
		this.fxmlControllerClass = fxmlControllerClass;	
	}

	public String getFxmlFile() {
		return fxmlFile;
	}

	public Class getFxmlControllerClass() {
		return fxmlControllerClass;
	}
	
	
}
