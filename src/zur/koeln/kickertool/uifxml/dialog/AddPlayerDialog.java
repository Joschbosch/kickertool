package zur.koeln.kickertool.uifxml.dialog;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import zur.koeln.kickertool.uifxml.FXMLAddPlayerDialogController;
import zur.koeln.kickertool.uifxml.FXMLGUIController;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;

public class AddPlayerDialog<R> extends Dialog<R> {

	public AddPlayerDialog(ConfigurableApplicationContext ctx) {

        setTitle("Spieler hinzuf\u00FCgen");
        
        FXMLLoader loader = new FXMLLoader(FXMLGUIController.class.getResource(FXMLGUI.ADD_PLAYER_DIALOG.getFxmlFile()));

        try {
        	loader.setControllerFactory(ctx::getBean);
        	DialogPane pane = loader.load();
        	FXMLAddPlayerDialogController dialogController = loader.getController();
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
