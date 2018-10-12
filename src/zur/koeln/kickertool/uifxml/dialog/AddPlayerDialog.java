package zur.koeln.kickertool.uifxml.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import zur.koeln.kickertool.uifxml.FXMLAddPlayerDialogController;
import zur.koeln.kickertool.uifxml.FXMLPlayerPoolManagementController;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;

public class AddPlayerDialog<R> extends Dialog<R> {

	private FXMLGUIservice fxmlGuiService;

	public AddPlayerDialog(FXMLGUIservice newFxmlGuiService) {
		this.fxmlGuiService = newFxmlGuiService;
	}
	
	public void init() {
		setTitle("Spieler hinzuf\u00FCgen");

		FXMLLoader dialogloader = getFxmlGuiService().getFXMLLoader(FXMLGUI.ADD_PLAYER_DIALOG);
		FXMLLoader playerPoolManagementLoader = getFxmlGuiService().getFXMLLoader(FXMLGUI.PLAYER_POOL_MANAGEMENT);
		
		FXMLAddPlayerDialogController dialogController = dialogloader.getController();
		
		dialogController.init();
		dialogController.addPlayerPoolManagementGridPane(playerPoolManagementLoader.getRoot());
		
		setDialogPane(dialogloader.getRoot());
		getDialogPane().setExpandableContent(null);

		setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.APPLY) {
				return (R) dialogController.getSelectedPlayer();
			}
			return null;
		});
	}

	private FXMLGUIservice getFxmlGuiService() {
		return fxmlGuiService;
	}

}
