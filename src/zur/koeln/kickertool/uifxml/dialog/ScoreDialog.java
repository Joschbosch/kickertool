package zur.koeln.kickertool.uifxml.dialog;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import zur.koeln.kickertool.api.tournament.Team;
import zur.koeln.kickertool.uifxml.FXMLGUI;
import zur.koeln.kickertool.uifxml.FXMLGUIController;
import zur.koeln.kickertool.uifxml.FXMLMatchResultDialogController;

public class ScoreDialog<R> extends Dialog<R> {
	
    public ScoreDialog(Team teamHome, Team teamVisit) {

        setTitle("Ergebnisse");
        
        FXMLLoader loader = new FXMLLoader(FXMLGUIController.class.getResource(FXMLGUI.MATCH_RESULT_DIALOG.getFxmlFile()));

        try {
        	DialogPane pane = loader.load();
        	
        	FXMLMatchResultDialogController dialogController = loader.getController();
        	
        	dialogController.init(teamHome, teamVisit);
			setDialogPane(pane);
			getDialogPane().setExpandableContent(null);
			
	        setResultConverter(dialogButton -> {
	            if (dialogButton == ButtonType.APPLY) {
	                return (R) dialogController.getEnteredResult();
	            }
	            return null;
	        });
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
