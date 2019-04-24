package zur.koeln.kickertool.ui.controller.dialogs;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.DialogContent;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.dialogs.vms.MatchResultViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;
import zur.koeln.kickertool.ui.shared.GUIEvents;

@Component
@Getter(value=AccessLevel.PRIVATE)
@SuppressWarnings("nls")
public class MatchResultEditDialogController extends AbstractController implements DialogContent<MatchDTOViewModel, MatchResultViewModel>{

	@FXML
	private JFXTextField txtResultTeam1;
	
	@FXML
	private JFXTextField txtResultTeam2;

	@Override
	public void initializeDialogWithContent(MatchDTOViewModel initialContent) {
		
		getTxtResultTeam1().setPromptText(String.valueOf(initialContent.getScoreHome()));
		getTxtResultTeam2().setPromptText(String.valueOf(initialContent.getScoreVisiting()));
		createTextNumberFormatter();
	}
	
	private void createTextNumberFormatter() {
		getTxtResultTeam1().setTextFormatter(createIntegerTextFormatter());
		getTxtResultTeam1().setTextFormatter(createIntegerTextFormatter());
		
	}
	
	private TextFormatter<Integer> createIntegerTextFormatter() {
		return new TextFormatter<>(new IntegerStringConverter());
	}
	
	@Override
	public MatchResultViewModel sendResult() {
		
		int scoreHome = Integer.valueOf(getTextFromTextField(getTxtResultTeam1())).intValue();
		int scoreVisiting = Integer.valueOf(getTextFromTextField(getTxtResultTeam2())).intValue();
		
		return new MatchResultViewModel(scoreHome, scoreVisiting);
	}
	
	private String getTextFromTextField(JFXTextField textField) {
		return textField.getText().isEmpty() ? textField.getPromptText() : textField.getText();
	}
	
	@Override
	public ModelValidationResult validateBeforeClose() {
		
		ModelValidationResult valResult = new ModelValidationResult();
		
		if (getTxtResultTeam1().getText() == null) {
			valResult.addValidationMessage("Ergebnis für Team 1 muss eingegeben werden.");
		}
		
		if (getTxtResultTeam2().getText() == null) {
			valResult.addValidationMessage("Ergebnis für Team 2 muss eingegeben werden.");
		}
		
		return valResult;
		
	}
	
	@Override
	public void handleEvent(GUIEvents guiEvents, Object content) {
		// nothing to do here
	}

	@Override
	protected void registerEvents() {
		// nothing to do here
	} 
}
