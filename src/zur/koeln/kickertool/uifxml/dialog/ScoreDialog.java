package zur.koeln.kickertool.uifxml.dialog;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.StringConverter;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.tournament.content.Match;
import zur.koeln.kickertool.uifxml.FXMLGUIController;
import zur.koeln.kickertool.uifxml.FXMLMatchResultDialogController;

public class ScoreDialog<R> extends Dialog<R> {
	
	@SuppressWarnings("unchecked")
    public ScoreDialog(String teamHomeName, String teamVisitName) {

        ButtonType enterButton = new ButtonType("Enter", ButtonData.OK_DONE);
        setTitle("Ergebnisse");
        
        FXMLLoader loader = new FXMLLoader(FXMLGUIController.class.getResource("MatchResultDialog.fxml"));

        try {
        	DialogPane pane = loader.load();
        	FXMLMatchResultDialogController dialogController = loader.getController();
        	
        	dialogController.init(teamHomeName, teamVisitName);
			setDialogPane(pane);
			
			getDialogPane().getButtonTypes().addAll(enterButton, ButtonType.CANCEL);
			
	        setResultConverter(dialogButton -> {
	            if (dialogButton == enterButton) {
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

class IntegerConverter extends StringConverter<Integer> {

    @Override
    public String toString(Integer object) {
        return object + "";
    }

    @Override
    public Integer fromString(String string) {
        return Integer.valueOf(string);
    }
}
