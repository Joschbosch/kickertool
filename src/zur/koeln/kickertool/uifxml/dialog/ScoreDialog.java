package zur.koeln.kickertool.uifxml.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import zur.koeln.kickertool.api.tournament.Team;
import zur.koeln.kickertool.uifxml.FXMLMatchResultDialogController;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;

public class ScoreDialog<R> extends Dialog<R> {

	private FXMLGUIservice fxmlGuiService;

	public ScoreDialog(FXMLGUIservice newFxmlGuiService) {
		this.fxmlGuiService = newFxmlGuiService;
	}
	
	public void init(Team teamHome, Team teamVisit, int goalsToWin) {

		setTitle("Ergebnisse");

		FXMLLoader loader = getFxmlGuiService().getFXMLLoader(FXMLGUI.MATCH_RESULT_DIALOG);

		FXMLMatchResultDialogController dialogController = loader.getController();

		dialogController.init(teamHome, teamVisit, goalsToWin);
		setDialogPane(loader.getRoot());
		getDialogPane().setExpandableContent(null);

		setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.APPLY) {
				return (R) dialogController.getEnteredResult();
			}
			return null;
		});

	}

	private FXMLGUIservice getFxmlGuiService() {
		return fxmlGuiService;
	}

}
