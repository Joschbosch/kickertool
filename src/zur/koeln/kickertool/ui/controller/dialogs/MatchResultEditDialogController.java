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
		
		getTxtResultTeam1().setText(String.valueOf(initialContent.getScoreHome()));
		getTxtResultTeam2().setText(String.valueOf(initialContent.getScoreVisiting()));
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
		
		int scoreHome = Integer.valueOf(getTxtResultTeam1().getText()).intValue();
		int scoreVisiting = Integer.valueOf(getTxtResultTeam2().getText()).intValue();
		
		return new MatchResultViewModel(scoreHome, scoreVisiting);
		
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
}
