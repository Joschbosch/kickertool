package zur.koeln.kickertool.uifxml;

public enum FXMLGUI {
	
	MAIN_MENU("MainMenu.fxml"),
	MATCH_ENTRY("MatchEntry.fxml"),
	MATCH_RESULT_DIALOG("MatchResultDialog.fxml"),
	PLAYER_POOL_MANAGEMENT("PlayerPoolManagement.fxml"),
	PLAYER_SELECTION("PlayerSelection.fxml"),
	TOURMANENT("Tournament.fxml"),
	TOURMANENT_INFO("TournamentInfo.fxml"),
	TOURNAMENT_CONFIGURATION("TournamentConfiguration.fxml"),
	ADD_PLAYER_DIALOG("AddPlayerDialog.fxml");
	
	private String fxmlFile;

	private FXMLGUI(String fxmlFile) {
		this.fxmlFile = fxmlFile;
	}

	public String getFxmlFile() {
		return fxmlFile;
	}
	
	
}
